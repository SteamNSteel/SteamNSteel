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

import com.foudroyantfactotum.tool.structure.renderer.StructureTESR;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.resource.structure.RemnantRuinIronBarsBlock.IronBarsTextures;
import mod.steamnsteel.client.model.opengex.OpenGEXModelLoader;
import mod.steamnsteel.client.model.pct.PCTModelLoader;
import mod.steamnsteel.client.renderer.tileentity.LargeFanTESR;
import mod.steamnsteel.client.renderer.tileentity.OgexStructureTESR;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import mod.steamnsteel.texturing.wall.RemnantRuinFloorSideTexture;
import mod.steamnsteel.texturing.wall.RemnantRuinWallTexture;
import mod.steamnsteel.tileentity.structure.BallMillTE;
import mod.steamnsteel.tileentity.structure.BlastFurnaceTE;
import mod.steamnsteel.tileentity.structure.BoilerTE;
import mod.steamnsteel.tileentity.structure.LargeFanTE;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@SuppressWarnings({"MethodMayBeStatic", "WeakerAccess"})
public class ClientRenderProxy extends CommonRenderProxy
{
    @Override
    public void preInit()
    {
        registerBlocksItemModels();
        registerItemRenderers();

        registerConnectedTextures();

        registerEventHandlers();

        OpenGEXModelLoader.instance.addDomain(TheMod.MOD_ID);
        OBJLoader.instance.addDomain(TheMod.MOD_ID);
        B3DLoader.instance.addDomain(TheMod.MOD_ID);
        ModelLoaderRegistry.registerLoader(OpenGEXModelLoader.instance);
        ModelLoaderRegistry.registerLoader(PCTModelLoader.instance);
    }

    private void registerConnectedTextures()
    {
        PCTModelLoader.instance.registerTexture("ruinWall", new RemnantRuinWallTexture());
        PCTModelLoader.instance.registerTexture("ruinFloor", new RemnantRuinFloorSideTexture());
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
        registerBlockItemModel(ModBlock.pipe);
        registerBlockItemModel(ModBlock.pipeValve);
        registerBlockItemModel(ModBlock.pipeValveRedstone);
        registerBlockItemModel(ModBlock.pipeJunction);

        //Structure
        registerBlockItemModel(ModBlock.fanLarge);
        registerBlockItemModel(ModBlock.ssBoiler);
        registerBlockItemModel(ModBlock.ssBallMill);
        registerBlockItemModel(ModBlock.ssBlastFurnace);

        registerBlockItemModel(ModBlock.remnantRuinPillar);
        registerBlockItemModel(ModBlock.remnantRuinChest);

        registerBlockItemModel(ModBlock.remnantRuinWall);
        registerBlockItemModel(ModBlock.remnantRuinFloor);

        registerIronBarsModel(ModBlock.remnantRuinIronBars);
        registerMetadataBlockModel(ModBlock.blockConcrete, 0, "wetness", "0");
        registerMetadataBlockModel(ModBlock.blockConcrete, 5, "wetness", "5");
    }

    private void registerIronBarsModel(Block block) {
        for (int i = 0; i < IronBarsTextures.VALUES.length; ++i)
        {
            registerMetadataBlockModel(block, i, "type", IronBarsTextures.VALUES[i].getName());
        }
    }

    private void registerMetadataBlockModel(Block block, int meta, String discriminator, String discriminatorValue) {
        final String resourceName = block.getUnlocalizedName().substring(5);

        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(block),
                meta,
                new ModelResourceLocation(resourceName, "inventory," + discriminator + "=" + discriminatorValue)
        );
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
    }

    private void registerTESRs()
    {
        final StructureTESR STESR = new StructureTESR();

        ClientRegistry.bindTileEntitySpecialRenderer(LargeFanTE.class, new LargeFanTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(BoilerTE.class, STESR);
        ClientRegistry.bindTileEntitySpecialRenderer(BallMillTE.class, new OgexStructureTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(BlastFurnaceTE.class, STESR);
    }

    private void registerEventHandlers() {
        //FIXME: The Block Parts are not currently working.
        //MinecraftForge.EVENT_BUS.register(BlockHighlightEventListener.getInstance());
        MinecraftForge.EVENT_BUS.register(PCTModelLoader.instance);

    }
}
