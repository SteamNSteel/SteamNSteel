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


import net.minecraftforge.fml.common.SidedProxy;

@SuppressWarnings({"StaticNonFinalField", "PublicField"})
public enum Proxies
{
    INSTANCE;
    @SuppressWarnings("WeakerAccess")
    public static final String CLIENT_RENDER_PROXY_CLASS = "mod.steamnsteel.proxy.ClientRenderProxy";

    @SuppressWarnings("WeakerAccess")
    public static final String COMMON_RENDER_PROXY_CLASS = "mod.steamnsteel.proxy.CommonRenderProxy";

    @SidedProxy(clientSide = CLIENT_RENDER_PROXY_CLASS, serverSide = COMMON_RENDER_PROXY_CLASS)
    public static CommonRenderProxy render;

    @SuppressWarnings("WeakerAccess")
    public static final String CLIENT_NETWORK_PROXY_CLASS = "mod.steamnsteel.proxy.ClientNetworkProxy";

    @SuppressWarnings("WeakerAccess")
    public static final String COMMON_NETWORK_PROXY_CLASS = "mod.steamnsteel.proxy.CommonNetworkProxy";

    @SidedProxy(clientSide = CLIENT_NETWORK_PROXY_CLASS, serverSide = COMMON_NETWORK_PROXY_CLASS)
    public static CommonNetworkProxy network;
}
