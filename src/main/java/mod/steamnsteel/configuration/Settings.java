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

package mod.steamnsteel.configuration;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.Reference;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
public enum Settings
{
    INSTANCE;

    public static void syncConfig(Configuration config)
    {
        World.syncConfig(config);
    }

    @SuppressWarnings("StaticNonFinalField")
    public enum World
    {
        @SuppressWarnings("InnerClassFieldHidesOuterClassField")
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".world";
        // Defaults
        private static boolean isCopperGenerated = true;
        private static boolean isNiterGenerated = true;
        private static boolean isSulfurGenerated = true;
        private static boolean isTinGenerated = true;
        private static boolean isZincGenerated = true;

        private static boolean doRetroOreGen = false;

        public static boolean isCopperGenerated() { return isCopperGenerated; }

        public static boolean isNiterGenerated() { return isNiterGenerated; }

        public static boolean isSulfurGenerated() { return isSulfurGenerated; }

        public static boolean isTinGenerated() { return isTinGenerated; }

        public static boolean isZincGenerated() { return isZincGenerated; }

        public static boolean doRetroOreGen() { return doRetroOreGen; }

        private static void syncConfig(Configuration config)
        {
            // Ore Generation
            isCopperGenerated = get(config, "genCopper", CATEGORY, isCopperGenerated);
            isNiterGenerated = get(config, "genNiter", CATEGORY, isNiterGenerated);
            isSulfurGenerated = get(config, "genSulfur", CATEGORY, isSulfurGenerated);
            isTinGenerated = get(config, "genTin", CATEGORY, isTinGenerated);
            isZincGenerated = get(config, "genZinc", CATEGORY, isZincGenerated);

            doRetroOreGen = get(config, "retroOreGen", CATEGORY, doRetroOreGen);
        }
    }

    private static boolean get(Configuration config, String settingName, String category, boolean defaultValue)
    {
        return config.getBoolean(settingName, category, defaultValue, getLocalizedComment(settingName));
    }

    private static String getLocalizedComment(String settingName)
    {
        return I18n.translateToLocal("config." + Reference.MOD_ID + ':' + settingName);
    }
}
