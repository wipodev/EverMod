package net.evermod.utils;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandSourceHelpers {

  public static void sendSuccess(CommandSourceStack source, Component component) {
    source.sendSuccess(component, false);
  }
}
