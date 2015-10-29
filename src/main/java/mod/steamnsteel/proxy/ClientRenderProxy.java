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
import mod.steamnsteel.client.model.OpenGEXModel;
import mod.steamnsteel.client.model.OpenGEXModelLoader;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;

@SuppressWarnings({"MethodMayBeStatic", "WeakerAccess"})
public class ClientRenderProxy extends RenderProxy
{
    @Override
    public void init()
    {
        registerBlocks();
        registerItemRenderers();
        registerTESRs();
        registerEventHandlers();

        OpenGEXModelLoader.instance.addDomain(TheMod.MOD_ID);
        OBJLoader.instance.addDomain(TheMod.MOD_ID);
        ModelLoaderRegistry.registerLoader(OpenGEXModelLoader.instance);

    }

    private void registerBlocks() {
        //Ores
        registerBlock(ModBlock.oreCopper);
        registerBlock(ModBlock.oreNiter);
        registerBlock(ModBlock.oreSulfur);
        registerBlock(ModBlock.oreTin);
        registerBlock(ModBlock.oreZinc);

        //Compressed Blocks
        registerBlock(ModBlock.blockBrass);
        registerBlock(ModBlock.blockBronze);
        registerBlock(ModBlock.blockCopper);
        registerBlock(ModBlock.blockPlotonium);
        registerBlock(ModBlock.blockSteel);
        registerBlock(ModBlock.blockTin);
        registerBlock(ModBlock.blockZinc);

        registerBlock(ModBlock.cupola);
    }

    private void registerBlock(Block block) {
        final String resourceName = block.getUnlocalizedName().substring(5);
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(block),
                0,
                new ModelResourceLocation(resourceName, "inventory")
        );
    }

    private void registerItem(Item item) {
        final String resourceName = item.getUnlocalizedName().substring(5);
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(resourceName, "inventory")
        );
    }

    private void registerItemRenderers()
    {
        registerItem(ModItem.anachDoodad);
        registerItem(ModItem.mustyJournal);
        registerItem(ModItem.perGuiVox);
        registerItem(ModItem.plotoniumScrap);
        registerItem(ModItem.voxBox);

        registerItem(ModItem.dustNiter);
        registerItem(ModItem.dustSulfur);

        registerItem(ModItem.ingotBrass);
        registerItem(ModItem.ingotBronze);
        registerItem(ModItem.ingotCopper);
        registerItem(ModItem.ingotPlotonium);
        registerItem(ModItem.ingotSteel);
        registerItem(ModItem.ingotTin);
        registerItem(ModItem.ingotZinc);

        registerItem(ModItem.helmetBronze);
        registerItem(ModItem.chestplateBronze);
        registerItem(ModItem.leggingsBronze);
        registerItem(ModItem.bootsBronze);
        registerItem(ModItem.helmetSteel);
        registerItem(ModItem.chestplateSteel);
        registerItem(ModItem.leggingsSteel);
        registerItem(ModItem.bootsSteel);

        registerItem(ModItem.axeBronze);
        registerItem(ModItem.pickBronze);
        registerItem(ModItem.shovelBronze);
        registerItem(ModItem.swordBronze);
        registerItem(ModItem.hoeBronze);
        registerItem(ModItem.axeSteel);
        registerItem(ModItem.pickSteel);
        registerItem(ModItem.shovelSteel);
        registerItem(ModItem.swordSteel);
        registerItem(ModItem.hoeSteel);

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
