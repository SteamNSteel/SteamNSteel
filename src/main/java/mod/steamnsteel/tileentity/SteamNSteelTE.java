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

package mod.steamnsteel.tileentity;

import mod.steamnsteel.network.PacketHandler;
import mod.steamnsteel.network.message.MessageSteamNSteelTE;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

public class SteamNSteelTE extends TileEntity
{
    private static final String CUSTOM_NAME = "[SNS]CustomName";
    private static final String OWNER = "[SNS]Owner";

    private String customName;
    private String owner;

    public SteamNSteelTE()
    {
        customName = "";
        owner = "";
    }

    public String getCustomName()
    {
        return customName;
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey(CUSTOM_NAME))
        {
            customName = nbtTagCompound.getString(CUSTOM_NAME);
        }

        if (nbtTagCompound.hasKey(OWNER))
        {
            owner = nbtTagCompound.getString(OWNER);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

        if (hasCustomName())
        {
            nbtTagCompound.setString(CUSTOM_NAME, customName);
        }

        if (hasOwner())
        {
            nbtTagCompound.setString(OWNER, owner);
        }
    }

    public boolean hasCustomName()
    {
        return customName != null && !customName.isEmpty();
    }

    public boolean hasOwner()
    {
        return owner != null && !owner.isEmpty();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.INSTANCE.getPacketFrom(new MessageSteamNSteelTE(this));
    }
}
