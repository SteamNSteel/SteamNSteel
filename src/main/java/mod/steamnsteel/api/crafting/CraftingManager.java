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

package mod.steamnsteel.api.crafting;

import com.google.common.base.Optional;

/**
 * The class that accesses the singleton crafting managers..
 *
 * @author Scott Killen
 * @version 1.0
 * @since 0.1
 */
@SuppressWarnings({"StaticNonFinalField", "PublicField", "NonConstantFieldWithUpperCaseName"})
public enum CraftingManager
{
    INSTANCE;

    /**
     * The singleton IAlloyManager implementation. If the API is present without the mod, alloyManager.isPresent()
     * returns <code>false</code>.
     */
    public static Optional<IAlloyManager> alloyManager = Optional.absent();
}
