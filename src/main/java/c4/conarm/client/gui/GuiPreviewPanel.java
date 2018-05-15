/*
 * Copyright (c) 2018 <C4>
 *
 * This Java class is distributed as a part of the Construct's Armory mod.
 * Construct's Armory is open source and distributed under the GNU General Public License v3.
 * View the source code and license file on github: https://github.com/TheIllusiveC4/ConstructsArmory
 */

package c4.conarm.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.inventory.Container;
import slimeknights.mantle.client.gui.GuiMultiModule;
import slimeknights.tconstruct.tools.common.client.module.GuiInfoPanel;

public class GuiPreviewPanel extends GuiInfoPanel {

    public GuiPreviewPanel(GuiMultiModule parent, Container container, int xSize, int ySize) {
        super(parent, container);
        this.xSize = xSize;
        this.ySize = ySize;
    }
}