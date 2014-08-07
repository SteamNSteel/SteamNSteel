/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.reference;

@SuppressWarnings("UtilityClass")
public final class Reference
{
    public static final String MOD_ID = "enormabuild";
    public static final String MOD_NAME = "EnormaBuild";
    public static final String MOD_VERSION = "@MOD_VERSION@";
    public static final String MOD_GUI_FACTORY = "mod.steamnsteel.configuration.client.ModGuiFactory";
    public static final String CLIENT_PROXY_CLASS = "mod.steamnsteel.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "mod.steamnsteel.proxy.ServerProxy";

    public static final String CONFIG_VERSION = "1";

    // PRIVATE //

    /**
     The caller references the constants using <tt>Reference.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Reference(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
