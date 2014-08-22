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

package mod.steamnsteel.utility.log;

import mod.steamnsteel.library.Constants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public enum Logger
{
    INSTANCE;

    @SuppressWarnings({"NonSerializableFieldInSerializableClass", "InstanceVariableMayNotBeInitialized"})
    private org.apache.logging.log4j.Logger logger;

    public static void info(final String format, final Object... args)
    {
        INSTANCE.log(Level.INFO, format, args);
    }

    public static void log(final Level level, final Throwable exception, final String format, final Object... args)
    {
        //noinspection ChainedMethodCall
        INSTANCE.getLogger().log(level, String.format(format, args), exception);
    }

    public static void severe(final String format, final Object... args)
    {
        INSTANCE.log(Level.ERROR, format, args);
    }

    public static void warning(final String format, final Object... args)
    {
        INSTANCE.log(Level.WARN, format, args);
    }

    public org.apache.logging.log4j.Logger getLogger()
    {
        if (logger == null)
        {
            init();
        }

        return logger;
    }

    private void init()
    {
        if (logger == null)
        {
            logger = LogManager.getLogger(Constants.MOD_ID);
        }
    }

    private void log(final Level level, final String format, final Object... data)
    {
        //noinspection ChainedMethodCall
        getLogger().log(level, String.format(format, data));
    }
}
