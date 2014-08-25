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
import mod.steamnsteel.network.message.MessageCupolaTE;
import net.minecraft.network.Packet;

public class CupolaTE extends SteamNSteelTE
{
    private static final int STATE_ACTIVEi_FLAG = 0x1;  // 0001

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.INSTANCE.getPacketFrom(new MessageCupolaTE(this));
    }

    public boolean isActive()
    {
        return (getState() & STATE_ACTIVEi_FLAG) == STATE_ACTIVEi_FLAG;
    }

    @SuppressWarnings("BooleanParameter")
    public void setActive(boolean isActive)
    {
        byte state = getState();

        if (isActive)
            state |= STATE_ACTIVEi_FLAG;
        else
            state &= ~STATE_ACTIVEi_FLAG;

        setState(state);
    }
}
