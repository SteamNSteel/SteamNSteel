package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.resource.structure.RemnantRuinFloorBlock;
import mod.steamnsteel.texturing.api.SpriteRequest;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RemnantRuinFloorSideTexture extends ProceduralConnectedTexture
{
    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {

    }

    @Override
    protected void registerSprites(ISpriteDefinitionStart textures)
    {
        textures.useSpriteNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideLeft"))
                .forTraitSet(LEFT)
                .andTraitSet(LEFT | TOP | BOTTOM)
                .andTraitSet(LEFT | TOP)
                .andTraitSet(LEFT | BOTTOM);
        textures.useSpriteNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideRight"))
                .forTraitSet(RIGHT)
                .andTraitSet(RIGHT | TOP | BOTTOM)
                .andTraitSet(RIGHT | TOP)
                .andTraitSet(RIGHT | BOTTOM);
        textures.useSpriteNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideMiddle"))
                .forTraitSet(DEFAULT)
                .andTraitSet(TOP | BOTTOM)
                .andTraitSet(TOP)
                .andTraitSet(BOTTOM);

        textures.useSpriteNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideSingle"))
                .forTraitSet(LEFT | RIGHT)
                .andTraitSet(LEFT | RIGHT | TOP | BOTTOM)
                .andTraitSet(LEFT | RIGHT | TOP)
                .andTraitSet(LEFT | RIGHT | BOTTOM);
    }

    private ResourceLocation makeResourceLocation(String spriteResourceLocation)
    {
        return new ResourceLocation(TheMod.MOD_ID, "blocks/" + spriteResourceLocation);
    }

    @Override
    protected boolean isCompatibleBlock(SpriteRequest request, IBlockState blockState)
    {
        return blockState.getBlock() instanceof RemnantRuinFloorBlock;
    }

    @Override
    public TextureAtlasSprite getDefaultTextureForSide(EnumFacing side)
    {
        return getSpriteForTraitSet(DEFAULT);
    }
}
