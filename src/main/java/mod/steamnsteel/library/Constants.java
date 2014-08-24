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

package mod.steamnsteel.library;

import mod.steamnsteel.TheMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public enum Constants
{
    _;

    public static final String RENDER_PROXY_CLASS = "mod.steamnsteel.proxy.RenderProxy";
    public static final String CLIENT_RENDER_PROXY_CLASS = "mod.steamnsteel.proxy.ClientRenderProxy";

    public static final String CONFIG_VERSION = "1";

    public static final String RESOURCE_PREFIX = TheMod.MOD_ID.toLowerCase() + ':';


    @SuppressWarnings("AnonymousInnerClass")
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(TheMod.MOD_ID.toLowerCase())
    {
        @Override
        public Item getTabIconItem()
        {
            return ModItems.MUSTY_JOURNAL;
        }
    };
}
