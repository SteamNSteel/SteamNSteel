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

package mod.steamnsteel.utility.crafting;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

public final class RecipePattern
{
    private final ImmutableList<String> pattern;

    private RecipePattern(String firstRow, String secondRow, String thirdRow)
    {
        pattern = ImmutableList.of(firstRow, secondRow, thirdRow);
    }

    public static RecipePattern of(String firstRow, String secondRow, String thirdRow)
    {
        return new RecipePattern(firstRow, secondRow, thirdRow);
    }

    public static RecipePattern of(String firstRow, String secondRow)
    {
        return new RecipePattern(firstRow, secondRow, "");
    }

    public static RecipePattern of(String firstRow)
    {
        return new RecipePattern(firstRow, "", "");
    }

    public String[] get()
    {
        return pattern.toArray(new String[3]);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("Row 1", pattern.get(0))
                .add("Row 2", pattern.get(1))
                .add("Row 3", pattern.get(2))
                .toString();
    }

    @SuppressWarnings("QuestionableName")
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RecipePattern that = (RecipePattern) o;

        return pattern.equals(that.pattern);
    }

    @Override
    public int hashCode()
    {
        return pattern.hashCode();
    }
}
