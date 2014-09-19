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

import mod.steamnsteel.item.SteamNSteelItem;
<<<<<<< HEAD
import mod.steamnsteel.library.ModItem;
=======
import mod.steamnsteel.library.Names;
import mod.steamnsteel.network.Event.PropertyVoxBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
>>>>>>> d4a9589ca683f932fb1b114dc3b08d0c4c8eb2d1

public class PerGuiVox extends SteamNSteelItem
{
    public PerGuiVox()
    {
        setUnlocalizedName(ModItem.Names.PER_GUI_VOX);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            PropertyVoxBox property = PropertyVoxBox.get(player);
            if (!property.isVoxBoxEnabled()) {
                property.setVoxBoxEnabled(true);
            }
        }

        return super.onItemRightClick(itemStack, world, player);
    }
}
