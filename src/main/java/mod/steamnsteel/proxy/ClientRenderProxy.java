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

package mod.steamnsteel.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.machine.PipeJunctionBlock;
import mod.steamnsteel.block.machine.PipeRedstoneValveBlock;
import mod.steamnsteel.block.machine.PipeValveBlock;
import mod.steamnsteel.client.renderer.item.*;
import mod.steamnsteel.client.renderer.tileentity.*;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.tileentity.*;
import mod.steamnsteel.utility.blockParts.BlockHighlightEventListener;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings({"MethodMayBeStatic", "WeakerAccess"})
public class ClientRenderProxy extends RenderProxy
{
    @Override
    public void init()
    {
        registerItemRenderers();
        registerTESRs();
        registerEventHandlers();
    }

    @Override
    public int addNewArmourRenderers(String armor)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    private void registerItemRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.cupola), new CupolaItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipe), new PipeItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeValve), new PipeValveItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeRedstoneValve), new PipeRedstoneValveItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeJunction), new PipeJunctionItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.chestPlotonium), new PlotoniumChestItemRenderer());
    }

    private void registerTESRs()
    {
        PipeBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        PipeValveBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        PipeRedstoneValveBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        PipeJunctionBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());

        ClientRegistry.bindTileEntitySpecialRenderer(CupolaTE.class, new CupbolaTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeTE.class, new PipeTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeValveTE.class, new PipeValveTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeRedstoneValveTE.class, new PipeRedstoneValveTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeJunctionTE.class, new PipeJunctionTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PlotoniumChestTE.class, new PlotoniumChestTESR());
    }

    private void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(BlockHighlightEventListener.getInstance());
    }
}
