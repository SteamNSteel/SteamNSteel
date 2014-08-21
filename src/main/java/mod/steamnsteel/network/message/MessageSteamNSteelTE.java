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

package mod.steamnsteel.network.message;

import com.google.common.base.Objects;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.tileentity.TileEntity;

public class MessageSteamNSteelTE  implements IMessage, IMessageHandler<MessageSteamNSteelTE, IMessage>
{
    public int x;
    public int y;
    public int z;
    public byte orientation;
    public byte state;
    public String customName;
    public String owner;

    public MessageSteamNSteelTE()
    {
    }

    public MessageSteamNSteelTE(SteamNSteelTE tileEntity)
    {
        x = tileEntity.xCoord;
        y = tileEntity.yCoord;
        z = tileEntity.zCoord;
        orientation = (byte) tileEntity.getOrientation().ordinal();
        state = (byte) tileEntity.getState();
        customName = tileEntity.getCustomName();
        owner = tileEntity.getOwner();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        orientation = buf.readByte();
        state = buf.readByte();
        final int customNameLength = buf.readInt();
        customName = new String(buf.readBytes(customNameLength).array());
        final int ownerLength = buf.readInt();
        owner = new String(buf.readBytes(ownerLength).array());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(orientation);
        buf.writeByte(state);
        buf.writeInt(customName.length());
        buf.writeBytes(customName.getBytes());
        buf.writeInt(owner.length());
        buf.writeBytes(owner.getBytes());
    }

    @Override
    public IMessage onMessage(MessageSteamNSteelTE message, MessageContext ctx)
    {
        final TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);

        if (tileEntity instanceof SteamNSteelTE)
        {
            final SteamNSteelTE te = (SteamNSteelTE) tileEntity;
            te.setOrientation(message.orientation);
            te.setState(message.state);
            te.setCustomName(message.customName);
            te.setOwner(message.owner);
        }

        return null;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .add("orientation", orientation)
                .add("state", state)
                .add("customName", customName)
                .add("owner", owner)
                .toString();
    }
}
