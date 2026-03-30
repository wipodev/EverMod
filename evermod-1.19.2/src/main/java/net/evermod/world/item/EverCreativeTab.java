package net.evermod.world.item;

import net.minecraft.world.item.CreativeModeTab;

public enum EverCreativeTab {
  BREWING(CreativeModeTab.TAB_BREWING), //
  BUILDING_BLOCKS(CreativeModeTab.TAB_BUILDING_BLOCKS), //
  COMBAT(CreativeModeTab.TAB_COMBAT), //
  DECORATIONS(CreativeModeTab.TAB_DECORATIONS), //
  FOOD(CreativeModeTab.TAB_FOOD), //
  MISC(CreativeModeTab.TAB_MISC), //
  REDSTONE(CreativeModeTab.TAB_REDSTONE), //
  SEARCH(CreativeModeTab.TAB_SEARCH), //
  TOOLS(CreativeModeTab.TAB_TOOLS), //
  TRANSPORTATION(CreativeModeTab.TAB_TRANSPORTATION); //

  private final CreativeModeTab vanillaTab;

  EverCreativeTab(CreativeModeTab vanillaTab) {
    this.vanillaTab = vanillaTab;
  }

  public CreativeModeTab getVanillaTab() {
    return this.vanillaTab;
  }
}
