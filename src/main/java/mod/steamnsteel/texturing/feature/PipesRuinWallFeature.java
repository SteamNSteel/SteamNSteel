package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.BlockSideRotation;
import mod.steamnsteel.texturing.IRuinWallFeature;
import mod.steamnsteel.texturing.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class PipesRuinWallFeature implements IRuinWallFeature
{

    private RuinWallTexture ruinWallTexture;
    private final int featureId;

    public PipesRuinWallFeature(RuinWallTexture ruinWallTexture, int featureId) {this.ruinWallTexture = ruinWallTexture;
        this.featureId = featureId;
    }

    @Override
    public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId)
    {
        ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);

        if (!ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back))
        {
            return false;
        }

        ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

        boolean aboveValid = (ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above)) == featureId);
        //boolean aboveValid2 = (ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above).offset(above), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above).offset(above)) == featureId);
        if (aboveValid)
        {
            return true;
        }


        boolean belowValid = (ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below)) == featureId);
        //boolean belowValid2 = (ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below).offset(below), back) && ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below).offset(below)) == featureId);

        if (belowValid)
        {
            return true;
        }

        return false;
    }

    @Override
    public Collection<ProceduralConnectedTexture.Feature> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        Random random = new Random(Objects.hash(chunkCoord, featureId));

        final int featureCount = 64;

        List<ProceduralConnectedTexture.Feature> features = new ArrayList<ProceduralConnectedTexture.Feature>(featureCount);
        //Generate Pipe features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(15);
            int zPos = random.nextInt(18) - 1;

            features.add(new ProceduralConnectedTexture.Feature(featureId, WorldBlockCoord.of(xPos, yPos, zPos), 1, 2, 1));
        }
        return features;
    }
}
