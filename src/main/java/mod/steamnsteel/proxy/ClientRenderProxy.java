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

import mod.steamnsteel.TheMod;
import mod.steamnsteel.client.model.opengex.OpenGEXModelLoader;
import mod.steamnsteel.client.renderer.tileentity.LargeFanTESR;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import mod.steamnsteel.tileentity.LargeFanTE;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@SuppressWarnings({"MethodMayBeStatic", "WeakerAccess"})
public class ClientRenderProxy extends RenderProxy
{
    @Override
    public void preInit()
    {
        registerBlocksItemModels();
        registerItemRenderers();

        registerEventHandlers();

        OpenGEXModelLoader.instance.addDomain(TheMod.MOD_ID);
        OBJLoader.instance.addDomain(TheMod.MOD_ID);
        B3DLoader.instance.addDomain(TheMod.MOD_ID);
        ModelLoaderRegistry.registerLoader(OpenGEXModelLoader.instance);
    }

    @Override
    public void init() {
        registerTESRs();
    }

    private void registerBlocksItemModels() {
        //Ores
        registerBlockItemModel(ModBlock.oreCopper);
        registerBlockItemModel(ModBlock.oreNiter);
        registerBlockItemModel(ModBlock.oreSulfur);
        registerBlockItemModel(ModBlock.oreTin);
        registerBlockItemModel(ModBlock.oreZinc);

        //Compressed Blocks
        registerBlockItemModel(ModBlock.blockBrass);
        registerBlockItemModel(ModBlock.blockBronze);
        registerBlockItemModel(ModBlock.blockCopper);
        registerBlockItemModel(ModBlock.blockPlotonium);
        registerBlockItemModel(ModBlock.blockSteel);
        registerBlockItemModel(ModBlock.blockTin);
        registerBlockItemModel(ModBlock.blockZinc);

        registerBlockItemModel(ModBlock.cupola);
        registerBlockItemModel(ModBlock.fanLarge);
        registerBlockItemModel(ModBlock.pipe);
        registerBlockItemModel(ModBlock.pipeValve);
        registerBlockItemModel(ModBlock.pipeValveRedstone);
        registerBlockItemModel(ModBlock.pipeJunction);

        registerBlockItemModel(ModBlock.remnantRuinPillar);
        registerBlockItemModel(ModBlock.remnantRuinChest);
    }

    private void registerBlockItemModel(Block block) {
        final String resourceName = block.getUnlocalizedName().substring(5);
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(block),
                0,
                new ModelResourceLocation(resourceName, "inventory")
        );
    }

    private void registerItemModel(Item item) {
        final String resourceName = item.getUnlocalizedName().substring(5);
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(resourceName, "inventory")
        );
    }

    private void registerItemRenderers()
    {
        registerItemModel(ModItem.anachDoodad);
        registerItemModel(ModItem.mustyJournal);
        registerItemModel(ModItem.perGuiVox);
        registerItemModel(ModItem.plotoniumScrap);
        registerItemModel(ModItem.voxBox);

        registerItemModel(ModItem.dustNiter);
        registerItemModel(ModItem.dustSulfur);

        registerItemModel(ModItem.ingotBrass);
        registerItemModel(ModItem.ingotBronze);
        registerItemModel(ModItem.ingotCopper);
        registerItemModel(ModItem.ingotPlotonium);
        registerItemModel(ModItem.ingotSteel);
        registerItemModel(ModItem.ingotTin);
        registerItemModel(ModItem.ingotZinc);

        registerItemModel(ModItem.helmetBronze);
        registerItemModel(ModItem.chestplateBronze);
        registerItemModel(ModItem.leggingsBronze);
        registerItemModel(ModItem.bootsBronze);
        registerItemModel(ModItem.helmetSteel);
        registerItemModel(ModItem.chestplateSteel);
        registerItemModel(ModItem.leggingsSteel);
        registerItemModel(ModItem.bootsSteel);

        registerItemModel(ModItem.axeBronze);
        registerItemModel(ModItem.pickBronze);
        registerItemModel(ModItem.shovelBronze);
        registerItemModel(ModItem.swordBronze);
        registerItemModel(ModItem.hoeBronze);
        registerItemModel(ModItem.axeSteel);
        registerItemModel(ModItem.pickSteel);
        registerItemModel(ModItem.shovelSteel);
        registerItemModel(ModItem.swordSteel);
        registerItemModel(ModItem.hoeSteel);

        //TODO: reenable these once I have them working
        /*
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipe), new PipeItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeValve), new PipeValveItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeValveRedstone), new PipeRedstoneValveItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.pipeJunction), new PipeJunctionItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.remnantRuinChest), new PlotoniumChestItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.remnantRuinPillar), new RemnantRuinPillarItemRenderer());
        */
    }

    private void registerTESRs()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(LargeFanTE.class, new LargeFanTESR());
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
