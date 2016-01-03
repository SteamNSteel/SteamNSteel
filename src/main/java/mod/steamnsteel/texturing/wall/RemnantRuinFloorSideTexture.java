package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.resource.structure.RemnantRuinFloorBlock;
import mod.steamnsteel.texturing.api.Layer;
import mod.steamnsteel.texturing.api.SpriteRequest;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart.ISideTraitList;
import mod.steamnsteel.texturing.feature.RepeatingTextureFeature;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RemnantRuinFloorSideTexture extends ProceduralConnectedTexture
{
    private Layer LAYER_BASE;
    private RepeatingTextureFeature featureBase;

    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {
        LAYER_BASE = features.registerLayer("SteelFloor", false, EnumFacing.UP, EnumFacing.DOWN);
        featureBase = new RepeatingTextureFeature("SteelFloor", LAYER_BASE, 3, 9);
        features.registerFeature(featureBase);
    }

    @Override
    protected void registerSprites(final ISpriteDefinitionStart textures)
    {
        textures.forSides(new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST}, new ISpriteDefinitionStart.ISideTraitList() {
            @Override
            public void register()
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
        });


        textures.forSides(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN}, new ISideTraitList() {
            @Override
            public void register()
            {
                featureBase.registerTextures(textures, makeResourceLocation("steelfloor/steelfloor"));
            }
        });


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
        if (side == EnumFacing.UP || side == EnumFacing.DOWN)
        {
            return getSpriteForTraitSet(side, featureBase.getFeatureTraitId());
        } else {
            return getSpriteForTraitSet(side, LEFT | RIGHT);
        }
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return getSpriteForTraitSet(EnumFacing.NORTH, DEFAULT);
    }
}
