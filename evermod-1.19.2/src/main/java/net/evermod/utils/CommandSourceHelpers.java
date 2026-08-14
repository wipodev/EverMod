package net.evermod.utils;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public final class CommandSourceHelpers {

  private CommandSourceHelpers() {}

  public static void sendSuccess(CommandSourceStack source, Component component) {
    source.sendSuccess(component, false);
  }
}
