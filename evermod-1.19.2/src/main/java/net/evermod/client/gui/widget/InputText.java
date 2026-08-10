package net.evermod.client.gui.widget;

import java.util.function.Consumer;
import java.util.function.IntPredicate;
import net.evermod.client.gui.AbstractComponent;
import net.evermod.client.gui.EverGraphics;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

/**
 * Customizable text input widget supporting character filtering, text selection,
 * double-click word selection, clipboard operations, and custom rendering styles.
 *
 * @author Wipodev
 */
public class InputText extends AbstractComponent {
  private String value = "";
  private String placeholder = "";
  private int maxLength = 32;
  private int cursorPosition = 0;
  private int highlightPos = 0;
  private int displayPos = 0;
  private boolean focused = false;
  private int backgroundColor = 0x80000000;
  private int borderColor = 0xFF555555;
  private int focusedBorderColor = 0xFF55FF55;
  private int textColor = 0xFFFFFFFF;
  private int placeholderColor = 0xFF888888;
  private int highlightColor = 0xFF0078D7;
  private int cursorBlinkSpeed = 500;
  private IntPredicate filter = c -> (c >= 32 && c != 127) || c == '§';
  private Consumer<String> onValueChange;
  private static long ibeamCursor = 0;
  private boolean isHoveredLastFrame = false;
  private long lastClickTime = 0;

  /**
   * Constructs a default {@code InputText} instance with default dimensions (120x18) at position (0,0).
   */
  public InputText() {
    super(0, 0, 120, 18);
    initCursorHandle();
  }

  /**
   * Constructs an {@code InputText} instance with specified dimensions and position.
   *
   * @param x the X coordinate of the component
   * @param y the Y coordinate of the component
   * @param width the width of the component
   * @param height the height of the component
   */
  public InputText(int x, int y, int width, int height) {
    super(x, y, width, height);
    initCursorHandle();
  }

  /**
   * Initializes the GLFW native I-beam cursor handle if not already created.
   */
  private static void initCursorHandle() {
    if (ibeamCursor == 0) {
      ibeamCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    }
  }

  /**
   * Sets the current text value of the input field, applying filters and length constraints.
   *
   * @param value the new string value to set
   * @return this instance for method chaining
   */
  public InputText setValue(String value) {
    String newValue = value != null ? value : "";
    if (this.filter != null) {
      StringBuilder filtered = new StringBuilder();
      for (char c : newValue.toCharArray()) {
        if (this.filter.test(c)) {
          filtered.append(c);
        }
      }
      newValue = filtered.toString();
    }

    if (newValue.length() > this.maxLength) {
      newValue = newValue.substring(0, this.maxLength);
    }

    this.value = newValue;
    this.setCursorPosition(this.value.length());
    this.setHighlightPos(this.cursorPosition);
    return this;
  }

  /**
   * Alias for {@link #setValue(String)} to support fluent API usage.
   *
   * @param value the new string value to set
   * @return this instance for method chaining
   */
  public InputText value(String value) {
    return setValue(value);
  }

  /**
   * Gets the current text value contained within the input field.
   *
   * @return the string value
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Retrieves the currently selected portion of text.
   *
   * @return the selected substring, or an empty string if no selection is active
   */
  public String getSelectedText() {
    int start = Math.min(this.cursorPosition, this.highlightPos);
    int end = Math.max(this.cursorPosition, this.highlightPos);
    return this.value.substring(start, end);
  }

  /**
   * Sets the placeholder text displayed when the field value is empty.
   *
   * @param placeholder the placeholder text
   * @return this instance for method chaining
   */
  public InputText setPlaceholder(String placeholder) {
    this.placeholder = placeholder != null ? placeholder : "";
    return this;
  }

  /**
   * Alias for {@link #setPlaceholder(String)} to support fluent API usage.
   *
   * @param placeholder the placeholder text
   * @return this instance for method chaining
   */
  public InputText placeholder(String placeholder) {
    return setPlaceholder(placeholder);
  }

  /**
   * Sets the maximum character limit for this input field.
   *
   * @param maxLength the maximum allowable string length
   * @return this instance for method chaining
   */
  public InputText setMaxLength(int maxLength) {
    this.maxLength = Math.max(1, maxLength);
    if (this.value.length() > this.maxLength) {
      setValue(this.value.substring(0, this.maxLength));
    }
    return this;
  }

  /**
   * Sets the character validation filter predicate.
   *
   * @param filter an {@link IntPredicate} testing integer character code points
   * @return this instance for method chaining
   */
  public InputText setFilter(IntPredicate filter) {
    this.filter = filter;
    return this;
  }

  /**
   * Sets the cursor blink interval rate in milliseconds.
   *
   * @param blinkSpeedMs the blink speed in milliseconds
   * @return this instance for method chaining
   */
  public InputText setCursorBlinkSpeed(int blinkSpeedMs) {
    this.cursorBlinkSpeed = Math.max(100, blinkSpeedMs);
    return this;
  }

  /**
   * Sets the callback listener invoked whenever the field value changes.
   *
   * @param onValueChange a {@link Consumer} accepting the new string value
   * @return this instance for method chaining
   */
  public InputText setOnValueChange(Consumer<String> onValueChange) {
    this.onValueChange = onValueChange;
    return this;
  }

  /**
   * Sets the focus state of this input field.
   *
   * @param focused {@code true} to focus, {@code false} to unfocus
   * @return this instance for method chaining
   */
  public InputText setFocused(boolean focused) {
    this.focused = focused;
    return this;
  }

  /**
   * Checks whether this input field currently has focus.
   *
   * @return {@code true} if focused, {@code false} otherwise
   */
  public boolean isFocused() {
    return this.focused;
  }

  /**
   * Updates the cursor position and adjusts text scrolling if necessary.
   *
   * @param position the target character index for the cursor
   */
  public void setCursorPosition(int position) {
    this.cursorPosition = Math.max(0, Math.min(position, this.value.length()));
    scrollToCursor();
  }

  /**
   * Sets the highlight anchor position used for text selection range calculation.
   *
   * @param position the character index anchor
   */
  public void setHighlightPos(int position) {
    this.highlightPos = Math.max(0, Math.min(position, this.value.length()));
  }

  /**
   * Inserts text at the current cursor position or replaces active text selection.
   *
   * @param textInserted the text snippet to insert
   */
  public void insertText(String textInserted) {
    int start = Math.min(this.cursorPosition, this.highlightPos);
    int end = Math.max(this.cursorPosition, this.highlightPos);

    if (textInserted == null || textInserted.isEmpty()) {
      if (start != end) {
        String leftPart = this.value.substring(0, start);
        String rightPart = this.value.substring(end);
        this.value = leftPart + rightPart;
        setCursorPosition(start);
        setHighlightPos(start);
        notifyValueChange();
      }
      return;
    }

    int availableSpace = this.maxLength - (this.value.length() - (end - start));
    if (availableSpace <= 0) {
      return;
    }

    StringBuilder builder = new StringBuilder();
    for (char c : textInserted.toCharArray()) {
      if (this.filter == null || this.filter.test(c)) {
        builder.append(c);
      }
    }

    String filteredInsert = builder.toString();
    if (filteredInsert.isEmpty()) {
      return;
    }

    if (filteredInsert.length() > availableSpace) {
      filteredInsert = filteredInsert.substring(0, availableSpace);
    }

    String leftPart = this.value.substring(0, start);
    String rightPart = this.value.substring(end);
    this.value = leftPart + filteredInsert + rightPart;

    int newCursor = start + filteredInsert.length();
    setCursorPosition(newCursor);
    setHighlightPos(newCursor);
    notifyValueChange();
  }

  /**
   * Deletes characters or active selections in the specified direction.
   *
   * @param direction negative to delete backwards (Backspace), positive to delete forwards (Delete)
   */
  public void deleteText(int direction) {
    if (this.cursorPosition != this.highlightPos) {
      insertText("");
      return;
    }

    if (direction < 0 && this.cursorPosition > 0) {
      int targetPos = Screen.hasControlDown() ? getWordPosition(-1) : this.cursorPosition - 1;
      String leftPart = this.value.substring(0, targetPos);
      String rightPart = this.value.substring(this.cursorPosition);
      this.value = leftPart + rightPart;
      setCursorPosition(targetPos);
      setHighlightPos(targetPos);
      notifyValueChange();
    } else if (direction > 0 && this.cursorPosition < this.value.length()) {
      int targetPos = Screen.hasControlDown() ? getWordPosition(1) : this.cursorPosition + 1;
      String leftPart = this.value.substring(0, this.cursorPosition);
      String rightPart = this.value.substring(targetPos);
      this.value = leftPart + rightPart;
      scrollToCursor();
      notifyValueChange();
    }
  }

  /**
   * Calculates the target character index when jumping word-by-word.
   *
   * @param direction negative for previous word start, positive for next word start
   * @return the calculated character index
   */
  public int getWordPosition(int direction) {
    int currentPos = this.cursorPosition;
    boolean isBackwards = direction < 0;

    if (!isBackwards) {
      int length = this.value.length();
      currentPos = this.value.indexOf(' ', currentPos);
      if (currentPos == -1) {
        currentPos = length;
      } else {
        while (currentPos < length && this.value.charAt(currentPos) == ' ') {
          currentPos++;
        }
      }
    } else {
      while (currentPos > 0 && this.value.charAt(currentPos - 1) == ' ') {
        currentPos--;
      }
      while (currentPos > 0 && this.value.charAt(currentPos - 1) != ' ') {
        currentPos--;
      }
    }
    return currentPos;
  }

  /**
   * Adjusts the visible horizontal text offset to ensure the cursor remains visible within bounds.
   */
  private void scrollToCursor() {
    Font font = Minecraft.getInstance().font;
    int maxTextWidth = this.width - 8;

    if (this.displayPos > this.cursorPosition) {
      this.displayPos = this.cursorPosition;
    }

    String textToCursor =
        this.value.substring(this.displayPos, Math.min(this.cursorPosition, this.value.length()));
    int cursorOffset = font.width(textToCursor);

    if (cursorOffset > maxTextWidth) {
      this.displayPos = this.cursorPosition - font
          .plainSubstrByWidth(this.value.substring(0, this.cursorPosition), maxTextWidth, true)
          .length();
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!isVisible() || !isEnabled()) {
      setFocused(false);
      return false;
    }

    boolean wasClicked = isMouseOver(mouseX, mouseY);
    setFocused(wasClicked);

    if (wasClicked && button == 0) {
      Font font = Minecraft.getInstance().font;
      int innerX = Math.max(0, (int) mouseX - (this.x + 4));
      int maxTextWidth = this.width - 8;

      String visiblePart =
          font.plainSubstrByWidth(this.value.substring(this.displayPos), maxTextWidth);
      int calculatedOffset = getNearestCursorOffset(font, visiblePart, innerX);
      int newPos = this.displayPos + calculatedOffset;

      long currentTime = Util.getMillis();
      if (currentTime - this.lastClickTime < 250L) {
        selectWordAt(newPos);
      } else {
        setCursorPosition(newPos);
        if (!Screen.hasShiftDown()) {
          setHighlightPos(newPos);
        }
      }
      this.lastClickTime = currentTime;
      return true;
    }

    return wasClicked;
  }

  /**
   * Selects an entire word surrounding the specified character index.
   *
   * @param index the target index within the text string
   */
  private void selectWordAt(int index) {
    if (this.value.isEmpty()) {
      return;
    }
    int start = Math.min(index, this.value.length() - 1);
    while (start > 0 && Character.isLetterOrDigit(this.value.charAt(start - 1))) {
      start--;
    }
    int end = index;
    while (end < this.value.length() && Character.isLetterOrDigit(this.value.charAt(end))) {
      end++;
    }
    setCursorPosition(end);
    setHighlightPos(start);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (isVisible() && isEnabled() && this.focused && button == 0) {
      Font font = Minecraft.getInstance().font;
      int innerX = Math.max(0, (int) mouseX - (this.x + 4));
      int maxTextWidth = this.width - 8;

      String visiblePart =
          font.plainSubstrByWidth(this.value.substring(this.displayPos), maxTextWidth);
      int calculatedOffset = getNearestCursorOffset(font, visiblePart, innerX);

      setCursorPosition(this.displayPos + calculatedOffset);
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  /**
   * Computes the nearest character position offset based on pixel mouse coordinate X.
   *
   * @param font the client font instance
   * @param text the visible portion of text
   * @param clickX relative pixel X position within the box
   * @return character index offset
   */
  private int getNearestCursorOffset(Font font, String text, int clickX) {
    int currentWidth = 0;

    for (int i = 0; i < text.length(); i++) {
      int charWidth = font.width(text.substring(i, i + 1));
      if (clickX < currentWidth + (charWidth / 2)) {
        return i;
      }
      currentWidth += charWidth;
    }

    return text.length();
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (!isVisible() || !isEnabled() || !this.focused) {
      return false;
    }

    if (Screen.isSelectAll(keyCode)) {
      setCursorPosition(this.value.length());
      setHighlightPos(0);
      return true;
    }

    if (Screen.isCopy(keyCode)) {
      Minecraft.getInstance().keyboardHandler.setClipboard(getSelectedText());
      return true;
    }

    if (Screen.isPaste(keyCode)) {
      String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard().strip();
      if (clipboard != null && !clipboard.isEmpty()) {
        insertText(clipboard);
      }
      return true;
    }

    if (Screen.isCut(keyCode)) {
      String selected = getSelectedText();
      if (!selected.isEmpty()) {
        Minecraft.getInstance().keyboardHandler.setClipboard(selected);
        insertText("");
      }
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
      deleteText(-1);
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_DELETE) {
      deleteText(1);
      return true;
    }

    boolean isSelecting = Screen.hasShiftDown() || Screen.hasAltDown();

    if (keyCode == GLFW.GLFW_KEY_LEFT) {
      int target = Screen.hasControlDown() ? getWordPosition(-1) : this.cursorPosition - 1;
      setCursorPosition(target);
      if (!isSelecting) {
        setHighlightPos(target);
      }
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_RIGHT) {
      int target = Screen.hasControlDown() ? getWordPosition(1) : this.cursorPosition + 1;
      setCursorPosition(target);
      if (!isSelecting) {
        setHighlightPos(target);
      }
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_HOME) {
      setCursorPosition(0);
      if (!isSelecting) {
        setHighlightPos(0);
      }
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_END) {
      setCursorPosition(this.value.length());
      if (!isSelecting) {
        setHighlightPos(this.value.length());
      }
      return true;
    }

    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (!isVisible() || !isEnabled() || !this.focused) {
      return false;
    }

    if (this.filter == null || this.filter.test(codePoint)) {
      insertText(Character.toString(codePoint));
      return true;
    }

    return super.charTyped(codePoint, modifiers);
  }

  /**
   * Triggers the value change listener if one has been attached.
   */
  private void notifyValueChange() {
    if (this.onValueChange != null) {
      this.onValueChange.accept(this.value);
    }
  }

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      if (isHoveredLastFrame) {
        resetMouseCursor();
      }
      return;
    }

    boolean hovered = isMouseOver(mouseX, mouseY) && isEnabled();
    if (hovered && !isHoveredLastFrame) {
      long window = Minecraft.getInstance().getWindow().getWindow();
      GLFW.glfwSetCursor(window, ibeamCursor);
      isHoveredLastFrame = true;
    } else if (!hovered && isHoveredLastFrame) {
      resetMouseCursor();
    }

    int currentBorderColor = this.focused ? this.focusedBorderColor : this.borderColor;
    graphics.drawBorderedRect(this.x, this.y, this.width, this.height,
        this.backgroundColor, currentBorderColor);

    Font font = graphics.getFont();
    int paddingX = 4;
    int maxTextWidth = this.width - (paddingX * 2);
    int textY = this.y + (this.height - font.lineHeight) / 2;

    if (this.displayPos > this.value.length()) {
      this.displayPos = this.value.length();
    }

    graphics.activateScissor(this.x + 2, this.y + 1, this.x + this.width - 2,
        this.y + this.height - 1);

    if (this.value.isEmpty() && !this.placeholder.isEmpty()) {
      String visiblePlaceholder = font.plainSubstrByWidth(this.placeholder, maxTextWidth);
      graphics.drawString(visiblePlaceholder, this.x + paddingX, textY, this.placeholderColor,
          false);
    } else {
      String rawVisibleText = this.value.substring(this.displayPos);
      String trimmedVisibleText = font.plainSubstrByWidth(rawVisibleText, maxTextWidth);

      int selStart = Math.min(this.cursorPosition, this.highlightPos);
      int selEnd = Math.max(this.cursorPosition, this.highlightPos);

      if (selStart != selEnd) {
        int renderStart = Math.max(selStart, this.displayPos);
        int renderEnd = Math.min(selEnd, this.displayPos + trimmedVisibleText.length());

        if (renderStart < renderEnd) {
          String textBeforeSel = this.value.substring(this.displayPos, renderStart);
          String selectedText = this.value.substring(renderStart, renderEnd);

          int highlightX1 = this.x + paddingX + font.width(textBeforeSel);
          int highlightX2 = highlightX1 + font.width(selectedText);

          graphics.drawRect(highlightX1, textY - 1, highlightX2, textY + font.lineHeight + 1,
              this.highlightColor);
        }
      }

      graphics.drawString(trimmedVisibleText, this.x + paddingX, textY, this.textColor, true);
    }

    boolean isBlinkVisible = (System.currentTimeMillis() / this.cursorBlinkSpeed) % 2 == 0;
    if (this.focused && isBlinkVisible && this.cursorPosition == this.highlightPos) {
      int safeCursorPos =
          Math.max(this.displayPos, Math.min(this.cursorPosition, this.value.length()));
      String textBeforeCursor = this.value.substring(this.displayPos, safeCursorPos);
      int cursorX = this.x + paddingX + font.width(textBeforeCursor);

      if (cursorX <= this.x + this.width - paddingX) {
        graphics.drawRect(cursorX, textY - 1, cursorX + 1, textY + font.lineHeight + 1, 0xFFFFFFFF);
      }
    }
    graphics.deactivateScissor();
  }

  /**
   * Resets the native window mouse cursor to the default arrow icon.
   */
  private void resetMouseCursor() {
    long window = Minecraft.getInstance().getWindow().getWindow();
    GLFW.glfwSetCursor(window, 0);
    isHoveredLastFrame = false;
  }
}
