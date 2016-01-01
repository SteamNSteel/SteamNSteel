package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.block.resource.structure.RemnantRuinFloorBlock;
import mod.steamnsteel.texturing.api.IconRequest;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.api.traiticonregistry.IIconDefinitionStart;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import net.minecraft.block.Block;

public class RemnantRuinFloorSideTexture extends ProceduralConnectedTexture
{
    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {

    }

    @Override
    protected void registerIcons(IIconDefinitionStart textures)
    {
        textures.useIconNamed("remnantRuinFloorSide/RemnantRuinFloorSideLeft")
                .forTraitSet(LEFT)
                .andTraitSet(LEFT | TOP | BOTTOM)
                .andTraitSet(LEFT | TOP)
                .andTraitSet(LEFT | BOTTOM);
        textures.useIconNamed("remnantRuinFloorSide/RemnantRuinFloorSideRight")
                .forTraitSet(RIGHT)
                .andTraitSet(RIGHT | TOP | BOTTOM)
                .andTraitSet(RIGHT | TOP)
                .andTraitSet(RIGHT | BOTTOM);
        textures.useIconNamed("remnantRuinFloorSide/RemnantRuinFloorSideMiddle")
                .forTraitSet(DEFAULT)
                .andTraitSet(TOP | BOTTOM)
                .andTraitSet(TOP)
                .andTraitSet(BOTTOM);

        textures.useIconNamed("remnantRuinFloorSide/RemnantRuinFloorSideSingle")
                .forTraitSet(LEFT | RIGHT)
                .andTraitSet(LEFT | RIGHT | TOP | BOTTOM)
                .andTraitSet(LEFT | RIGHT | TOP)
                .andTraitSet(LEFT | RIGHT | BOTTOM);
    }

    @Override
    protected boolean isCompatibleBlock(IconRequest request, Block block)
    {
        return (block instanceof RemnantRuinFloorBlock);
    }
}
