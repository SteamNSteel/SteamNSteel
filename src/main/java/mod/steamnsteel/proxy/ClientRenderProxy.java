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

    private void registerItemRenderers()
    {
        //TODO: reenable these once I have them working
        /*
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.cupola), new CupolaItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipe), new PipeItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeValve), new PipeValveItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeRedstoneValve), new PipeRedstoneValveItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeJunction), new PipeJunctionItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.remnantRuinChest), new PlotoniumChestItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.remnantRuinPillar), new RemnantRuinPillarItemRenderer());
        */
    }

    private void registerTESRs()
    {
        /*
        ClientRegistry.bindTileEntitySpecialRenderer(CupolaTE.class, new CupolaTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeTE.class, new PipeTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeValveTE.class, new PipeValveTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeRedstoneValveTE.class, new PipeRedstoneValveTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeJunctionTE.class, new PipeJunctionTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(RemnantRuinChestTE.class, new PlotoniumChestTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(RemnantRuinPillarTE.class, new RemnantRuinPillarTESR());
        */
    }

    private void registerEventHandlers() {
        //FIXME: The Block Parts are not currently working.
        //MinecraftForge.EVENT_BUS.register(BlockHighlightEventListener.getInstance());
    }
}
