package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.resource.structure.RemnantRuinFloorBlock;
import mod.steamnsteel.texturing.api.IconRequest;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.api.traiticonregistry.IIconDefinitionStart;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class RemnantRuinFloorSideTexture extends ProceduralConnectedTexture
{
    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {

    }

    @Override
    protected void registerIcons(IIconDefinitionStart textures)
    {
        textures.useIconNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideLeft"))
                .forTraitSet(LEFT)
                .andTraitSet(LEFT | TOP | BOTTOM)
                .andTraitSet(LEFT | TOP)
                .andTraitSet(LEFT | BOTTOM);
        textures.useIconNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideRight"))
                .forTraitSet(RIGHT)
                .andTraitSet(RIGHT | TOP | BOTTOM)
                .andTraitSet(RIGHT | TOP)
                .andTraitSet(RIGHT | BOTTOM);
        textures.useIconNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideMiddle"))
                .forTraitSet(DEFAULT)
                .andTraitSet(TOP | BOTTOM)
                .andTraitSet(TOP)
                .andTraitSet(BOTTOM);

        textures.useIconNamed(makeResourceLocation("remnantRuinFloorSide/RemnantRuinFloorSideSingle"))
                .forTraitSet(LEFT | RIGHT)
                .andTraitSet(LEFT | RIGHT | TOP | BOTTOM)
                .andTraitSet(LEFT | RIGHT | TOP)
                .andTraitSet(LEFT | RIGHT | BOTTOM);
    }

    private ResourceLocation makeResourceLocation(String iconName)
    {
        return new ResourceLocation(TheMod.MOD_ID, "blocks/" + iconName);
    }

    @Override
    protected boolean isCompatibleBlock(IconRequest request, IBlockState blockState)
    {
        return (blockState.getBlock() instanceof RemnantRuinFloorBlock);
    }
}
