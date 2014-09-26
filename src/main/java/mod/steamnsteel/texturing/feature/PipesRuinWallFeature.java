package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.BlockSideRotation;
import mod.steamnsteel.texturing.IProceduralWallFeature;
import mod.steamnsteel.texturing.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class PipesRuinWallFeature implements IProceduralWallFeature
{

    private RuinWallTexture ruinWallTexture;
    private final int featureId;

    public PipesRuinWallFeature(RuinWallTexture ruinWallTexture, int featureId) {this.ruinWallTexture = ruinWallTexture;
        this.featureId = featureId;
    }

    @Override
    public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);

        if (!ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back))
        {
            return false;
        }

        ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

        final boolean aboveBlockIsClear = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back);
        final IProceduralWallFeature aboveBlockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above));
        final boolean belowBlockIsClear = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back);
        final IProceduralWallFeature belowBlockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below));

        boolean aboveValid2 = (ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above).offset(above), back));
        boolean belowValid2 = (ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below).offset(below), back));

        if (aboveBlockIsClear && !(aboveBlockFeature instanceof PipesRuinWallFeature) && belowBlockIsClear && belowBlockFeature instanceof PipesRuinWallFeature && belowValid2) {
            return true;
        }

        if (belowBlockIsClear && !(belowBlockFeature instanceof PipesRuinWallFeature)&& aboveBlockIsClear && aboveBlockFeature instanceof PipesRuinWallFeature && aboveValid2) {
            return true;
        }


        return false;
    }

    @Override
    public Collection<ProceduralConnectedTexture.FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        Random random = new Random(Objects.hash(chunkCoord, featureId, 6));

        final int featureCount = 64;

        List<ProceduralConnectedTexture.FeatureInstance> features = new ArrayList<ProceduralConnectedTexture.FeatureInstance>(featureCount);
        //Generate Pipe features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(15);
            int zPos = random.nextInt(18) - 1;

            features.add(new ProceduralConnectedTexture.FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 2, 1));
        }
        return features;
    }

    @Override
    public int getFeatureId()
    {
        return featureId;
    }

    @Override
    public boolean canIntersect(IProceduralWallFeature feature)
    {
        if (feature instanceof PlateRuinWallFeature) {
            return true;
        }
        return false;
    }

    @Override
    public int getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

        int subProperties = featureId;
        IProceduralWallFeature aboveBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(above), orientation);
        IProceduralWallFeature belowBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(below), orientation);

        if (!(aboveBlockFeature instanceof PipesRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_TOP;
        }
        if (!(belowBlockFeature instanceof PipesRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_BOTTOM;
        }

        final int FEATURE_EDGE_TOP_AND_BOTTOM = ruinWallTexture.FEATURE_EDGE_TOP | ruinWallTexture.FEATURE_EDGE_BOTTOM;

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= featureId | FEATURE_EDGE_TOP_AND_BOTTOM;
        return subProperties;
    }
}
