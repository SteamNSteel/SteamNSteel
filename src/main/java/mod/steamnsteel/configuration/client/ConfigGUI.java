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

package mod.steamnsteel.configuration.client;

import com.google.common.collect.Lists;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.configuration.ConfigurationHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class ConfigGUI extends GuiConfig
{
    public ConfigGUI(GuiScreen parent)
    {
        super(parent, getConfigElements(), TheMod.MOD_ID, false, false,
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.INSTANCE.getConfig().toString()));
    }

    @SuppressWarnings("unchecked")
    private static List<IConfigElement> getConfigElements()
    {
        final List<IConfigElement> configElements = Lists.newArrayList();

        final Configuration config = ConfigurationHandler.INSTANCE.getConfig();
        final ConfigElement general = new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL));
        configElements.addAll(general.getChildElements());

        return configElements;
    }
}
