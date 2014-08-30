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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class FillerTE extends TileEntity
{
    public static final String NAME = "filler";

    private static final String PX = "px";
    private static final String PY = "py";
    private static final String PZ = "pz";

    private int masterX;
    private int masterY;
    private int masterZ;

    public int getMasterX()
    {
        return masterX;
    }

    public void setMasterX(int masterX)
    {
        this.masterX = masterX;
    }

    public int getMasterY()
    {
        return masterY;
    }

    public void setMasterY(int masterY)
    {
        this.masterY = masterY;
    }

    public int getMasterZ()
    {
        return masterZ;
    }

    public void setMasterZ(int masterZ)
    {
        this.masterZ = masterZ;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey(PX))
            masterX = nbt.getInteger(PX);

        if (nbt.hasKey(PY))
            masterY = nbt.getInteger(PY);

        if (nbt.hasKey(PZ))
            masterZ = nbt.getInteger(PZ);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger(PX, masterX);
        nbt.setInteger(PY, masterY);
        nbt.setInteger(PZ, masterZ);
    }
}
