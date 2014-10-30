package mod.steamnsteel.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.client.renderer.model.SteamSpiderModel;
import mod.steamnsteel.entity.SteamSpiderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SteamSpiderLivingRender extends SteamNSteelLivingRender
{
    private static final ResourceLocation TEXTURE = getResourceLocation(SteamSpiderEntity.NAME);

    public SteamSpiderLivingRender()
    {
        super(new SteamSpiderModel(), 0.4F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TEXTURE;
    }
}