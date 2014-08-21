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

import mod.steamnsteel.library.NBTTags;
import mod.steamnsteel.network.PacketHandler;
import mod.steamnsteel.network.message.MessageSteamNSteelTE;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class SteamNSteelTE extends TileEntity
{
    private ForgeDirection orientation;
    private byte state;
    private String customName;
    private String owner;

    public SteamNSteelTE()
    {
        orientation = ForgeDirection.SOUTH;
        state = 0;
        customName = "";
        owner = "";
    }

    public ForgeDirection getOrientation()
    {
        return orientation;
    }

    public void setOrientation(ForgeDirection orientation)
    {
        this.orientation = orientation;
    }

    public void setOrientation(int orientation)
    {
        this.orientation = ForgeDirection.getOrientation(orientation);
    }

    public short getState()
    {
        return state;
    }

    public void setState(byte state)
    {
        this.state = state;
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

        if (nbtTagCompound.hasKey(NBTTags.DIRECTION))
        {
            orientation = ForgeDirection.getOrientation((int) nbtTagCompound.getByte(NBTTags.DIRECTION));
        }

        if (nbtTagCompound.hasKey(NBTTags.STATE))
        {
            state = nbtTagCompound.getByte(NBTTags.STATE);
        }

        if (nbtTagCompound.hasKey(NBTTags.CUSTOM_NAME))
        {
            customName = nbtTagCompound.getString(NBTTags.CUSTOM_NAME);
        }

        if (nbtTagCompound.hasKey(NBTTags.OWNER))
        {
            owner = nbtTagCompound.getString(NBTTags.OWNER);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setByte(NBTTags.DIRECTION, (byte) orientation.ordinal());
        nbtTagCompound.setByte(NBTTags.STATE, state);

        if (hasCustomName())
        {
            nbtTagCompound.setString(NBTTags.CUSTOM_NAME, customName);
        }

        if (hasOwner())
        {
            nbtTagCompound.setString(NBTTags.OWNER, owner);
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
