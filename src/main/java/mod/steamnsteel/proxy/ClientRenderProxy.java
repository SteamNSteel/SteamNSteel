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
import mod.steamnsteel.client.fx.SteamParticle;
import mod.steamnsteel.block.resource.structure.RemnantRuinPillarBlock;
import mod.steamnsteel.client.renderer.block.SteamNSteelPaneRenderer;
import mod.steamnsteel.client.renderer.entity.SteamNSteelLivingRender;
import mod.steamnsteel.client.renderer.item.*;
import mod.steamnsteel.client.renderer.model.SteamSpiderModel;
import mod.steamnsteel.client.renderer.tileentity.*;
import mod.steamnsteel.entity.SteamProjectileEntity;
import mod.steamnsteel.entity.SteamSpiderEntity;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

@SuppressWarnings({"MethodMayBeStatic", "WeakerAccess"})
public class ClientRenderProxy extends RenderProxy
{
    @Override
    public void init()
    {
        registerEntityRenderers();
        registerItemRenderers();
        registerTESRs();
        registerEventHandlers();
    }

    @Override
    public void spawnParticle(String name, World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale)
    {
        //Similar to spawnParticle in world but with support for custom particles and additional details for vanilla
        EntityFX entityFX = null;
        if (name.equals("steam"))
        {
            entityFX = new SteamParticle(world, x, y, z, motionX, motionY, motionZ, scale);
        }
        else if (name.equals("smoke"))
        {
            entityFX = new EntitySmokeFX(world, x, y, z, motionX, motionY, motionZ, scale);
        }

        if (entityFX != null)
        {
            Minecraft.getMinecraft().effectRenderer.addEffect(entityFX);
        }
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
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.remnantRuinChest), new PlotoniumChestItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlock.remnantRuinPillar), new RemnantRuinPillarItemRenderer());
    }

    private void registerTESRs()
    {
        PipeBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        PipeValveBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        PipeRedstoneValveBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        PipeJunctionBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());
        RemnantRuinPillarBlock.setRenderType(RenderingRegistry.getNextAvailableRenderId());

        RenderingRegistry.registerBlockHandler(SteamNSteelPaneRenderer.INSTANCE);

        ClientRegistry.bindTileEntitySpecialRenderer(CupolaTE.class, new CupolaTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeTE.class, new PipeTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeValveTE.class, new PipeValveTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeRedstoneValveTE.class, new PipeRedstoneValveTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(PipeJunctionTE.class, new PipeJunctionTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(RemnantRuinChestTE.class, new PlotoniumChestTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(RemnantRuinPillarTE.class, new RemnantRuinPillarTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(SpiderFactoryTE.class, new SpiderFactoryTESR());
    }

    private void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(SteamSpiderEntity.class, new SteamNSteelLivingRender(new SteamSpiderModel(), SteamSpiderEntity.NAME, 0.4F));
        RenderingRegistry.registerEntityRenderingHandler(SteamProjectileEntity.class, new Render()
        {
            @Override
            public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) { }

            @Override
            protected ResourceLocation getEntityTexture(Entity p_110775_1_)
            {
                return null;
            }
        }); //We have to give it a render otherwise it renders a white box
    }

    private void registerEventHandlers() {
        //FIXME: The Block Parts are not currently working.
        //MinecraftForge.EVENT_BUS.register(BlockHighlightEventListener.getInstance());
    }
}
