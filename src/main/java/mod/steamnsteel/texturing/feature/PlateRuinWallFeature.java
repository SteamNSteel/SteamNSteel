package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class PlateRuinWallFeature extends ProceduralWallFeatureBase
{
    private RuinWallTexture ruinWallTexture;

    public PlateRuinWallFeature(RuinWallTexture ruinWallTexture, int layer)
    {
        super("Pipes", layer);
        this.ruinWallTexture = ruinWallTexture;
    }

    public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        /*ForgeDirection back = BlockSideRotation.forOrientation(TextureDirection.BACKWARDS, orientation);

        if (!ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord, back))
        {
            return false;
        }

        ForgeDirection left = BlockSideRotation.forOrientation(TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(TextureDirection.RIGHT, orientation);
        ForgeDirection below = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);

        //check Left
        boolean leftValid = (ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(left), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(left)) instanceof PlateRuinWallFeature);
        boolean rightValid = (ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(right), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(right)) instanceof PlateRuinWallFeature);
        if (!leftValid && !rightValid)
        {
            return false;
        }
        boolean aboveValid = (ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(above), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above)) instanceof PlateRuinWallFeature);
        boolean belowValid = (ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(below), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below)) instanceof PlateRuinWallFeature);

        if (!aboveValid && !belowValid)
        {
            return false;
        }

        //check for a cluster of 4 - Automatically valid
        //check above and left
        if (aboveValid && leftValid && ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(above).offset(left), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above).offset(left)) instanceof PlateRuinWallFeature)
        {
            return true;
        }
        //check above and right
        if (aboveValid && rightValid && ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(above).offset(right), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above).offset(right)) instanceof PlateRuinWallFeature)
        {
            return true;
        }
        //check below and left
        if (belowValid && leftValid && ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(below).offset(left), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below).offset(left)) instanceof PlateRuinWallFeature)
        {
            return true;
        }
        //check below and right
        if (belowValid && rightValid && ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(below).offset(right), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below).offset(right)) instanceof PlateRuinWallFeature)
        {
            return true;
        }*/

        //We have encountered an S or Z shape;
        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        final int featureCount = 64;

        Random random = new Random(Objects.hash(chunkCoord, getFeatureId(), 6));

        List<FeatureInstance> features = new ArrayList<FeatureInstance>(featureCount);

        //Generate plate features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(16);
            int zPos = random.nextInt(18) - 1;

            int width = random.nextInt(7) + 1;
            int height = random.nextInt(7) + 1;
            int depth = random.nextInt(7) + 1;

            features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), width, height, depth));
        }

        return features;
    }

    @Override
    public boolean canIntersect(IProceduralWallFeature feature)
    {
        return true;
    }

    @Override
    public long getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        /*ForgeDirection left = BlockSideRotation.forOrientation(TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(TextureDirection.RIGHT, orientation);
        ForgeDirection below = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);

        long subProperties = getFeatureId();
        IProceduralWallFeature leftBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(left), orientation, 1);
        IProceduralWallFeature rightBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(right), orientation, 1);
        IProceduralWallFeature aboveBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(above), orientation, 1);
        IProceduralWallFeature belowBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(below), orientation, 1);

        if (!(leftBlockFeature instanceof PlateRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_LEFT;
        }
        if (!(rightBlockFeature instanceof PlateRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_RIGHT;
        }
        if (!(aboveBlockFeature instanceof PlateRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_TOP;
        }
        if (!(belowBlockFeature instanceof PlateRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_BOTTOM;
        }

        final long FEATURE_EDGE_TOP_AND_BOTTOM = ruinWallTexture.FEATURE_EDGE_TOP | ruinWallTexture.FEATURE_EDGE_BOTTOM;
        final long FEATURE_EDGE_LEFT_AND_RIGHT = ruinWallTexture.FEATURE_EDGE_LEFT | ruinWallTexture.FEATURE_EDGE_RIGHT;

        //Plates cannot be 1 high or 1 wide.
        if ((subProperties & FEATURE_EDGE_TOP_AND_BOTTOM) == FEATURE_EDGE_TOP_AND_BOTTOM)
        {
            return 0;
        } else if ((subProperties & FEATURE_EDGE_LEFT_AND_RIGHT) == FEATURE_EDGE_LEFT_AND_RIGHT)
        {
            return 0;
        }

        //Calculate corner cases

        if (aboveBlockFeature instanceof PlateRuinWallFeature && leftBlockFeature instanceof PlateRuinWallFeature)
        {
            IProceduralWallFeature corner = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(above).offset(left), orientation, 1);
            if (!(corner instanceof PlateRuinWallFeature))
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_TL_CORNER;
            }
        }
        if (aboveBlockFeature instanceof PlateRuinWallFeature && rightBlockFeature instanceof PlateRuinWallFeature)
        {
            IProceduralWallFeature corner = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(above).offset(right), orientation, 1);
            if (!(corner instanceof PlateRuinWallFeature))
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_TR_CORNER;
            }
        }

        if (belowBlockFeature instanceof PlateRuinWallFeature && leftBlockFeature instanceof PlateRuinWallFeature)
        {
            IProceduralWallFeature corner = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(below).offset(left), orientation, 1);
            if (!(corner instanceof PlateRuinWallFeature))
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_BL_CORNER;
            }
        }
        if (belowBlockFeature instanceof PlateRuinWallFeature && rightBlockFeature instanceof PlateRuinWallFeature)
        {
            IProceduralWallFeature corner = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(below).offset(right), orientation, 1);
            if (!(corner instanceof PlateRuinWallFeature))
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_BR_CORNER;
            }
        }

        return subProperties;*/
        return 0;
    }

    @Override
    public String getName()
    {
        return "Plate";
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        return Behaviour.COEXIST;
    }
}
