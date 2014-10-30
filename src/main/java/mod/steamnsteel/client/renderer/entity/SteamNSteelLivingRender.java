package mod.steamnsteel.client.renderer.entity;

import mod.steamnsteel.TheMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;

abstract class SteamNSteelLivingRender extends RenderLiving
{
    private static final String TEXTURE_FILE_EXTENSION = ".png";
    private static final String TEXTURE_LOCATION = "textures/models/";

    SteamNSteelLivingRender(ModelBase model, float shadowSize)
    {
        super(model, shadowSize);
    }

    static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), getTexturePath(name));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static String getTexturePath(String name)
    {
        return TEXTURE_LOCATION + name + TEXTURE_FILE_EXTENSION;
    }
}
