@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul
cd /d "%~dp0"

:: ==============================================
:: EverMod Framework CLI - All-in-One
:: ==============================================
:: Uso:
::   evermod -r all
::   evermod -r SilentMask
::   evermod -c newmod 1.19.2
::   evermod -a newmod 1.19.2
:: ==============================================

:: === Detectar flag principal ===
set "action=%~1"
set "arg1=%~2"
set "arg2=%~3"
set "arg3=%~4"

if "%action%"=="" (
    call :generalHelp
    exit /b
)

:: Normalizar flags (case insensitive)
set "flag="
if /I "%action%"=="-r"  set "flag=repomix"
if /I "%action%"=="--repomix" set "flag=repomix"
if /I "%action%"=="-c"  set "flag=create"
if /I "%action%"=="--create" set "flag=create"
if /I "%action%"=="-a"  set "flag=add"
if /I "%action%"=="--add" set "flag=add"
if /I "%action%"=="-u"  set "flag=update"
if /I "%action%"=="--update" set "flag=update"
if /I "%action%"=="-h" set "flag=help"
if /I "%action%"=="--help" set "flag=help"


if "%flag%"=="" (
    echo ❌ Flag desconocida: %action%
    echo.
    call :generalHelp
    exit /b
)

if "%flag%"=="repomix"  goto repomix
if "%flag%"=="create"   goto create
if "%flag%"=="add"      goto add
if "%flag%"=="update"   goto update
if "%flag%"=="help"   goto help


:: ==============================================
:repomix
:: ==============================================
set "MODULES=mods\SilentMask"
set "target=%arg1%"
if "%target%"=="" set "target=all"

if /I "%target%"=="all" (
    echo ============================================
    echo 📦 Generando Repomix para todos los modulos...
    echo ============================================
    for %%M in (%MODULES%) do (
        set "MODULE=%%M"
        call :runRepomix
    )
    exit /b
)

:: --- Modo individual ---
set "found=false"
for %%M in (%MODULES%) do (
    for %%N in (%%~nxM) do (
        if /I "%%~nN"=="%target%" (
            set "MODULE=%%M"
            set "found=true"
            call :runRepomix
        )
    )
)
if "%found%"=="false" (
    echo ⚠️  No se encontró el módulo "%target%"
)
exit /b

:: ==============================================
:runRepomix
:: ==============================================
for %%N in (%MODULE%) do set "NAME=%%~nxN"
set "OUTPUT=%MODULE%\repomix-%NAME%.xml"
echo 🧩 Procesando %NAME% ...
call npx repomix "%MODULE%" -o "%OUTPUT%"
echo ✅ Archivo generado en "%OUTPUT%"
echo.
goto :eof


:: ==============================================
:create
:: ==============================================
set "modName=%arg1%"
set "mcVersion=%arg2%"
if "%modName%"=="" set "modName=newmod"
if "%mcVersion%"=="" set "mcVersion=1.19.2"

set "TEMPLATES=templatesMDK"
set "CONFIG=%TEMPLATES%\versions.json"
set "MODS_DIR=mods\%modName%"

if not exist "%CONFIG%" (
    echo ❌ No se encontró el archivo %CONFIG%.
    exit /b
)

:: === Leer JSON con PowerShell ===
for /f "delims=" %%A in ('powershell -NoProfile -Command ^
    "$cfg = Get-Content '%CONFIG%' | ConvertFrom-Json;" ^
    "if (-not $cfg.'%mcVersion%') { " ^
    "Write-Host 'NO_VERSION'; " ^
    "exit 0 } " ^
    "$v = $cfg.'%mcVersion%'; " ^
    "Write-Host ('TEMPLATE=' + $v.template); " ^
    "Write-Host ('FORGE_VER=' + $v.forge_version); " ^
    "Write-Host ('FORGE_MAJOR=' + $v.forge_version_mayor); " ^
    "Write-Host ('MC_RANGE=' + $v.minecraft_version_range)"') do (
    echo %%A | find "=" >nul && set "%%A"
)

if "%TEMPLATE%"=="" (
    echo ❌ Versión de Minecraft "%mcVersion%" no soportada.
    echo.
    echo 📘 Versiones disponibles:
    powershell -NoProfile -Command ^
        "(Get-Content '%CONFIG%' | ConvertFrom-Json).PSObject.Properties.Name | ForEach-Object { ' - Minecraft ' + $_ }"
    echo.
    echo 💡 Uso: ./evermod [nombre_mod] [version_minecraft]
    echo 💡 Ejemplo: ./evermod SilentMask 1.19.2
    exit /b
)

if exist "%MODS_DIR%" (
    echo ⚠️  Ya existe un mod llamado "%modName%" en "%MODS_DIR%"
    exit /b
)
mkdir "%MODS_DIR%" >nul

copy "%TEMPLATES%\%TEMPLATE%" "%MODS_DIR%\build.gradle" >nul
copy "%TEMPLATES%\gradle.properties.template" "%MODS_DIR%\gradle.properties" >nul

set "propsFile=%MODS_DIR%\gradle.properties"

:: Detectar versión para systemProp
for /f "tokens=1,2,3 delims=." %%a in ("%mcVersion%") do (
    set "major=%%a"
    set "minor=%%b"
    set "patch=%%c"
)
set "useSystemProp=false"
if "%major%"=="1" if %minor% GEQ 22 set "useSystemProp=true"
if "%major%"=="1" if %minor%==21 if "%patch%" GEQ "4" set "useSystemProp=true"

if "%useSystemProp%"=="true" (
    set "sysBlock=# In the case that Gradle needs to fork to recompile, this will set the memory for that process.`nsystemProp.net.minecraftforge.gradle.repo.recompile.fork=true`nsystemProp.net.minecraftforge.gradle.repo.recompile.fork.args=-Xmx5G`n`n# Opts-out of ForgeGradle automatically adding mavenCentral(), Forge`s maven and MC libs maven to the repositories block`nsystemProp.net.minecraftforge.gradle.repo.attach=false"
) else (
    set "sysBlock="
)

if "%sysBlock%"=="" (
    powershell -NoProfile -Command ^
        "(Get-Content '%propsFile%') | Where-Object { $_ -notmatch '\[systemProp\]' } | Set-Content '%propsFile%' -Encoding UTF8"
) else (
    powershell -NoProfile -Command ^
        "$t = Get-Content '%propsFile%' -Raw;" ^
        "$sys = '%sysBlock%'.Replace('`n', [Environment]::NewLine);" ^
        "$t = $t -replace '\[systemProp\]', $sys;" ^
        "Set-Content '%propsFile%' $t -Encoding UTF8"
)

:: 🔁 Reemplazar los demás marcadores SIEMPRE
powershell -NoProfile -Command ^
    "$t = Get-Content '%propsFile%' -Raw;" ^
    "$t = $t -replace '\[mcv\]', '%mcVersion%' -replace '\[mcvr\]', '%MC_RANGE%' -replace '\[fv\]', '%FORGE_VER%' -replace '\[fm\]', '%FORGE_MAJOR%' -replace '\[mid\]', '%modName%';" ^
    "Set-Content '%propsFile%' $t -Encoding UTF8"

echo ✅ Mod "%modName%" creado para Minecraft %mcVersion% (Forge %FORGE_VER%)
echo 📂 Ubicación: %MODS_DIR%
exit /b


:: ==============================================
:add
:: ==============================================
set "user=%arg1%"
set "modName=%arg2%"
set "mcVersion=%arg3%"
if "%modName%"=="" (
    echo ❌ Debes indicar el nombre del mod.
    exit /b
)
if "%mcVersion%"=="" set "mcVersion=1.19.2"

echo 📦 Agregando "%modName%" como submódulo...
git submodule add https://github.com/%user%/%modName%.git mods/%modName%
if errorlevel 1 (
    echo ❌ No se pudo agregar el submódulo.
    exit /b
)
echo ✅ Submódulo agregado correctamente.
exit /b


:: ==============================================
:update
:: ==============================================
echo 🔄 Función de actualización del framework próximamente.
exit /b

:: ==============================================
:help
:: ==============================================
set "helpTarget=%arg1%"
if "%helpTarget%"=="" goto generalHelp

if /I "%helpTarget%"=="r"  goto helpRepomix
if /I "%helpTarget%"=="repomix" goto helpRepomix
if /I "%helpTarget%"=="c"  goto helpCreate
if /I "%helpTarget%"=="create" goto helpCreate
if /I "%helpTarget%"=="a"  goto helpAdd
if /I "%helpTarget%"=="add" goto helpAdd
if /I "%helpTarget%"=="u"  goto helpUpdate
if /I "%helpTarget%"=="update" goto helpUpdate

echo ❌ Comando de ayuda no reconocido: %helpTarget%
echo Usa: evermod -h [comando]
echo Ejemplo: evermod -h add
goto :eof


:generalHelp
echo ⚙️  Uso: evermod [opción]
echo.
echo  -c, --create ^<nombre_mod^> ^<version_MC^>                Crea un nuevo mod en el workspace
echo  -a, --add ^<usuario_github^> ^<nombre_mod^> ^<version_MC^>  Agrega un mod como submódulo Git
echo  -r, --repomix ^<proyecto^>                              Ejecuta repomix en uno o todos los mods ^(default: all^)
echo  -u, --update                                          Próximamente Actualiza el framework o templates
echo  -h, --help                                            Muestra la ayuda especifica del comando
echo.
echo 💡 Ejemplo: evermod --create SilentMask 1.19.2
echo 💡 Ver ayuda específica: evermod -h add
goto :eof

:helpRepomix
echo ==============================================
echo 🧩  AYUDA: REPOMIX
echo ==============================================
echo Este comando ejecuta la herramienta Repomix, que empaqueta el código
para un módulo o para todos los mods del workspace en un solo archivo XML.
echo.
echo  🔹  Permite generar un resumen legible por IA o documentar el proyecto completo.
echo  🔹  Se ejecuta sobre cada mod configurado o sobre un mod individual.
echo.
echo  Uso:
  echo     evermod -r [mod]  ^|  evermod --repomix [mod]
echo.
echo  Ejemplos:
  echo     evermod -r all          ^(Genera repomix para todos los mods^)
  echo     evermod -r SilentMask   ^(Genera solo para SilentMask^)
echo.
echo  Resultado:
  echo     Crea un archivo llamado repomix-[nombre_mod].xml dentro del mod.
goto :eof

:helpCreate
echo ==============================================
echo 🛠️  AYUDA: CREATE
echo ==============================================
echo Este comando crea un nuevo mod a partir de una plantilla MDK.
echo.
echo  🔹  Copia el build.gradle y gradle.properties adecuados según la versión de Minecraft.
echo  🔹  Sustituye los marcadores por los valores correspondientes (mod_id, versiones, etc.).
echo  🔹  Soporta las versiones registradas en templatesMDK/versions.json.
echo.
echo  Uso:
  echo     evermod -c ^<nombre_mod^> ^<version_MC^>
echo.
echo  Ejemplos:
  echo     evermod -c SilentMask 1.19.2
  echo     evermod --create NewAdventure 1.21
echo.
echo  Resultado:
  echo     Crea una carpeta en mods/^<nombre_mod^> con los archivos base del mod.
goto :eof

:helpAdd
echo ==============================================
echo 🔗  AYUDA: ADD
echo ==============================================
echo Este comando agrega un mod externo alojado en GitHub como submódulo del workspace.
echo.
echo  🔹  Clona el repositorio dentro de la carpeta mods/.
echo  🔹  Permite mantener el mod sincronizado con su repositorio original.
echo.
echo  Uso:
  echo     evermod -a ^<usuario_github^> ^<nombre_mod^> ^<version_MC^>
echo.
echo  Ejemplo:
  echo     evermod -a wipodev SilentMask 1.21
echo.
echo  Resultado:
  echo     Agrega el submódulo en mods/^<nombre_mod^> y lo registra en .gitmodules.
goto :eof

:helpUpdate
echo ==============================================
echo 🔄  AYUDA: UPDATE
echo ==============================================
echo Este comando (en desarrollo) actualizará el framework EverMod y las plantillas MDK.
echo.
echo  🔹  Descargará las últimas plantillas desde el repositorio central.
echo  🔹  Sincronizará los archivos base sin afectar los mods existentes.
echo.
echo  Uso:
  echo     evermod -u  ^|  evermod --update
echo.
echo  Estado actual:
  echo     En desarrollo. Será implementado en futuras versiones del framework.
goto :eof
