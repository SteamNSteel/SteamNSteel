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

public class Vector<T>
{
    private final T x;
    private final T y;
    private final T z;

    public Vector(T x, T y, T z)
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
	    return x.equals(vector.x) && y.equals(vector.y) && z.equals(vector.z);
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

    public T getX()
    {

        return x;
    }

    public T getY()
    {

        return y;
    }

    public T getZ()
    {
        return z;
    }
}
