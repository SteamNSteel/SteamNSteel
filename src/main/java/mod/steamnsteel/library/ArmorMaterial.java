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

import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public enum ArmorMaterial
{
    INSTANCE;

    public static final ItemArmor.ArmorMaterial BRONZE = EnumHelper.addArmorMaterial("bronze", 16, new int[]{3, 6, 5, 3}, 12);
    public static final ItemArmor.ArmorMaterial STEEL = EnumHelper.addArmorMaterial("steel", 29, new int[]{3, 7, 6, 3}, 15);
}
