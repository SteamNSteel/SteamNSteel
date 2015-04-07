/*
 * Copyright (C) 2014  Kihira
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package mod.steamnsteel.commands;

import mod.steamnsteel.factory.Factory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ChunkCoordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * The command for spawning entities that has support for mod added entities
 */
public class CommandSpawnFactory extends CommandBase {

    @Override
    public String getCommandName() {
        return "snsCreateFactory";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "/snsCreateFactory <x> <y> <z> <entityname> [direction]";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] arguments) {
        if (arguments.length >= 4) {
            String entityName = arguments[3];

            ChunkCoordinates coordinates = commandSender.getPlayerCoordinates();
            //Gets coordinates handling coordinates relative to sender
            int x = (int)func_110666_a(commandSender, coordinates.posX, arguments[0]);
            int y = (int)func_110666_a(commandSender, coordinates.posY, arguments[1]);
            int z = (int)func_110666_a(commandSender, coordinates.posZ, arguments[2]);
            Object entityClass = EntityList.stringToClassMapping.get(entityName);
            if (entityClass == null) {
                throw new CommandException("Specified entity does not exist");
            }
            Factory factory = Factory.getFactoryForEntity((Class<? extends Entity>) entityClass);
            if (factory == null) {
                throw new CommandException(String.format("Could not identify factory for entity %s", entityName));
            }

            int direction = 0;
            if (arguments.length >= 5) {
                direction = parseIntBounded(commandSender, arguments[4], 0, 3);
            }
            factory.createFactory(commandSender.getEntityWorld(), x, y, z, direction);
        }
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        ArrayList<String> validEntities = new ArrayList<String>();

        if (par2ArrayOfStr.length <= 3) {
            validEntities.add("~ ");
        } else if (par2ArrayOfStr.length == 4) {
            for (Factory factory : Factory.getAllFactories()) {
                Class<? extends Entity> entityClass = factory.getSpawnedEntity();
                validEntities.add(EntityList.classToStringMapping.get(entityClass) + " ");
            }
        } else if (par2ArrayOfStr.length == 5) {
            validEntities.add("0 ");
        }

        return par2ArrayOfStr.length > 0 ? getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, validEntities) : null;
    }

    public int compareTo(Object par1Obj) {
        return this.compareTo((ICommand)par1Obj);
    }
}
