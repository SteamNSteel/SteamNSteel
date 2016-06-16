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
package mod.steamnsteel.proxy;

import mod.steamnsteel.networking.ProjectTableCraftPacket;
import mod.steamnsteel.networking.ProjectTableCraftPacketMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by codew on 17/01/2016.
 */
public class ClientNetworkProxy extends CommonNetworkProxy
{
    @Override
    public void init() {
        super.init();
        getNetwork().registerMessage(ProjectTableCraftPacketMessageHandler.class, ProjectTableCraftPacket.class, 0, Side.CLIENT);
    }

}
