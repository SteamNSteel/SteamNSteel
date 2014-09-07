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

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public enum ToolMaterial
{
    INSTANCE;

    public static final Item.ToolMaterial BRONZE = EnumHelper.addToolMaterial("bronze", 2, 312, 6.5F, 2.5F, 12);
    public static final Item.ToolMaterial STEEL = EnumHelper.addToolMaterial("steel", 3, 969, 7.5F, 4.0F, 15);
}
