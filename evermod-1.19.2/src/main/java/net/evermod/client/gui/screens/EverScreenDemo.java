package net.evermod.client.gui.screens;

import net.evermod.client.gui.layout.Box;
import net.evermod.client.gui.layout.Column;
import net.evermod.client.gui.layout.LayoutAlignment;
import net.evermod.client.gui.layout.Row;
import net.evermod.client.gui.layout.Space;
import net.evermod.client.gui.widget.Label;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;


/**
 * Diagnostic Screen used to test EverUI rendering, hierarchy, and components.
 *
 * @author Wipodev
 */
public class EverScreenDemo extends EverScreen {

  /**
   * Constructs the diagnostic screen.
   */
  public EverScreenDemo() {
    super("EverUI Layout Demonstration");
  }

  @Override
  protected void setupUI() {
    // 1. Root BOX occupying 80% of screen size, centered horizontally and vertically
    int boxWidth = (int) (this.width * 0.8F);
    int boxHeight = (int) (this.height * 0.8F);
    int boxX = getCenterX() - (boxWidth / 2);
    int boxY = getCenterY() - (boxHeight / 2);

    Box mainBox = new Box(boxX, boxY, boxWidth, boxHeight)
        .padding(12)
        .align(LayoutAlignment.CENTER, LayoutAlignment.CENTER);
    mainBox.backgroundColor(0xD0121218); // Dark gray container background

    // 2. Main vertical layout inside Box
    Column rootColumn = new Column()
        .gap(10)
        .fillMaxSize(boxWidth - 24, boxHeight - 24);

    // Box Header Label
    Label boxTitle = new Label(
        Component.literal("=== MAIN BOX CONTAINER ===").withStyle(ChatFormatting.GOLD,
            ChatFormatting.BOLD)).shadow(true);

    // 3. ROW Container holding two equal Columns side by side
    int availableRowWidth = boxWidth - 24;
    int availableRowHeight = boxHeight - 70;

    Row contentRow = new Row()
        .gap(12)
        .fillMaxSize(availableRowWidth, availableRowHeight);
    contentRow.backgroundColor(0x60222233); // Subtle blueish background for Row

    // 4. Left Column inside Row
    int halfColumnWidth = (availableRowWidth - 12) / 2;

    Column leftColumn = new Column()
        .gap(6)
        .padding(8)
        .fillMaxSize(halfColumnWidth, availableRowHeight);
    leftColumn.backgroundColor(0x801A3A5C); // Dark blue for Left Column

    leftColumn.addChild(new Label("--- COLUMN 1 (LEFT) ---").color(0xFF55FFFF).shadow(true));
    leftColumn.addChild(Space.height(4)); // Vertical spacing
    leftColumn.addChild(new Label("• Auto-calculated Y pos").color(0xFFFFFFFF));
    leftColumn.addChild(new Label("• Managed by Column flow").color(0xFFAAAAAA));

    // 5. Right Column inside Row
    Column rightColumn = new Column()
        .gap(6)
        .padding(8)
        .fillMaxSize(halfColumnWidth, availableRowHeight);
    rightColumn.backgroundColor(0x805C1A3A); // Dark red/purple for Right Column

    rightColumn.addChild(new Label("--- COLUMN 2 (RIGHT) ---").color(0xFFFF55FF).shadow(true));
    rightColumn.addChild(Space.height(4)); // Vertical spacing
    rightColumn.addChild(new Label("• Automatic horizontal Row gap").color(0xFFFFFFFF));
    rightColumn.addChild(new Label("• No manual setX / setY used").color(0xFF55FF55));

    // Assembly tree structure
    contentRow.addChild(leftColumn);
    contentRow.addChild(rightColumn);

    rootColumn.addChild(boxTitle);
    rootColumn.addChild(contentRow);

    mainBox.addChild(rootColumn);

    // Mount root container to screen
    this.add(mainBox);
  }
}
