package net.evermod.world.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public enum EverCreativeTab {
  BREWING(CreativeModeTabs.INGREDIENTS), //
  BUILDING_BLOCKS(CreativeModeTabs.BUILDING_BLOCKS), //
  COMBAT(CreativeModeTabs.COMBAT), //
  DECORATIONS(CreativeModeTabs.NATURAL_BLOCKS), //
  FOOD(CreativeModeTabs.FOOD_AND_DRINKS), //
  MISC(CreativeModeTabs.INGREDIENTS), //
  REDSTONE(CreativeModeTabs.REDSTONE_BLOCKS), //
  SEARCH(CreativeModeTabs.SEARCH), //
  TOOLS(CreativeModeTabs.TOOLS_AND_UTILITIES), //
  TRANSPORTATION(CreativeModeTabs.TOOLS_AND_UTILITIES); //

  private final ResourceKey<CreativeModeTab> vanillaTab;

  EverCreativeTab(ResourceKey<CreativeModeTab> vanillaTab) {
    this.vanillaTab = vanillaTab;
  }

  public ResourceKey<CreativeModeTab> getVanillaTab() {
    return this.vanillaTab;
  }
}
