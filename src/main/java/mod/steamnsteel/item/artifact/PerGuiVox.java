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

package mod.steamnsteel.item.artifact;

import mod.steamnsteel.event.VoxBoxPlayerProperty;
import mod.steamnsteel.item.SteamNSteelItem;
import mod.steamnsteel.library.ModItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PerGuiVox extends SteamNSteelItem
{
    public static final String PROPERTY_ID = "voxbox_enabled";

    public PerGuiVox()
    {
        setUnlocalizedName(ModItem.Names.PER_GUI_VOX);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        VoxBoxPlayerProperty property = VoxBoxPlayerProperty.get(player);

        if(property == null){
            VoxBoxPlayerProperty.register(player);
            property = VoxBoxPlayerProperty.get(player);
        }

        if(!property.enabled) {
            property.enabled = true;
            stack.stackSize--;
        }

        return super.onItemRightClick(stack, world, player);
    }
}
