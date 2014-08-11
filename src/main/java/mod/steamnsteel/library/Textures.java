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

public final class Textures
{
    public static final String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ':';

    private Textures()
    {
        throw new AssertionError();
    }

    public final class Armor
    {
        private static final String SHEET_LOCATION = "textures/armor/";

        private Armor()
        {
            throw new AssertionError();
        }
    }

    public final class Model
    {
        private static final String TEXTURE_LOCATION = "textures/models/";

        private Model()
        {
            throw new AssertionError();
        }
    }

    public final class Gui
    {
        private static final String SHEET_LOCATION = "textures/gui/";

        private Gui()
        {
            throw new AssertionError();
        }
    }

    public final class Effect
    {
        private static final String EFFECTS_LOCATION = "textures/effects/";

        private Effect()
        {
            throw new AssertionError();
        }
    }
}
