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

package mod.steamnsteel.utility;

import com.google.common.base.Objects;

public class Vector
{
    private final float x;
    private final float y;
    private final float z;

    public Vector(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @SuppressWarnings("QuestionableName")
    @Override
    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        final Vector vector = (Vector) that;

        return Float.compare(vector.x, x) == 0 &&
                Float.compare(vector.y, y) == 0 &&
                Float.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(x, y, z);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .toString();
    }

    public float getX()
    {

        return x;
    }

    public float getY()
    {

        return y;
    }

    public float getZ()
    {
        return z;
    }
}
