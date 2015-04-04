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

import mod.steamnsteel.api.entity.IEntityFactory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The command for spawning entities that has support for mod added entities
 */
public class CommandSpawnFactory extends CommandBase {

    @Override
    public String getCommandName() {
        return "spawnentity";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "/spawnfactory <x> <y> <z> <entityname> [direction]";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] arguments) {
        if (arguments.length > 4) {
            String entityName = arguments[3];
            Entity entity = EntityList.createEntityByName(entityName, commandSender.getEntityWorld());
            if (!(entity instanceof IEntityFactory)) {
                throw new CommandException("Specified entity is not a Factory");
            }

            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            int z = Integer.parseInt(arguments[2]);

            entity.setPosition(x, y, z);
            commandSender.getEntityWorld().spawnEntityInWorld(entity);
        }
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        ArrayList<String> validEntities = new ArrayList<String>();

        for (Class<?> clazz : (Class<?>[])EntityList.classToStringMapping.keySet().toArray(new Class<?>[EntityList.stringToClassMapping.size()])) {
            if (clazz.isAssignableFrom(IEntityFactory.class)) {
                validEntities.add((String)EntityList.classToStringMapping.get(clazz));
            }
        }

        return par2ArrayOfStr.length > 0 ? getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, validEntities) : null;
    }

    public int compareTo(Object par1Obj) {
        return this.compareTo((ICommand)par1Obj);
    }
}
