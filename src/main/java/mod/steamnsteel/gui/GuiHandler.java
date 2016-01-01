/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.gui;

import mod.steamnsteel.client.gui.CupolaGui;
import mod.steamnsteel.inventory.CupolaContainer;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public enum GuiHandler implements IGuiHandler
{
    INSTANCE;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        final ModGuis gui = ModGuis.fromId(id);
        switch(gui)
        {
            case CUPOLA:
                final CupolaTE te = (CupolaTE) world.getTileEntity(new BlockPos(x, y, z));
                return new CupolaContainer(player.inventory, te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        final ModGuis gui = ModGuis.fromId(id);
        switch(gui)
        {
            case CUPOLA:
                final CupolaTE te = (CupolaTE) world.getTileEntity(new BlockPos(x, y, z));
                return new CupolaGui(player.inventory, te);
        }
        return null;
    }
}
