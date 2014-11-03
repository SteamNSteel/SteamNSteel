package mod.steamnsteel.library;

import mod.steamnsteel.utility.blockParts.TileEntityBlockPartState;
import mod.steamnsteel.utility.blockParts.PartSet;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by steblo on 31/10/2014.
 */
public final class ModBlockParts {
    private static PartSet pipePartSet;

    public static void init() {
        final float scale = 1f / 16f;
        final float e = 0.00001f;

        pipePartSet = TileEntityBlockPartState.registerPartSet("pipes");
        pipePartSet.createBlockPart("North Connection")
                .setMetadata(ForgeDirection.NORTH)
                .setBoundingBox(2f * scale - e, 2f * scale - e, 0f * scale - e,
                        14f * scale + e, 14f * scale, 3f * scale + e);

        pipePartSet.createBlockPart("South Connection")
                .setMetadata(ForgeDirection.SOUTH)
                .setBoundingBox(2f * scale - e, 2f * scale - e, 14f * scale - e,
                        14f * scale + e, 14f * scale, 16f * scale + e);

        pipePartSet.createBlockPart("East Connection")
                .setMetadata(ForgeDirection.EAST)
                .setBoundingBox(0f * scale - e, 2f * scale - e, 2f * scale - e,
                        3f * scale + e, 14f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("West Connection")
                .setMetadata(ForgeDirection.WEST)
                .setBoundingBox(14f * scale - e, 2f * scale - e, 2f * scale - e,
                        16f * scale + e, 14f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("Top Connection")
                .setMetadata(ForgeDirection.UP)
                .setBoundingBox(2f * scale - e, 0f * scale - e, 2f * scale - e,
                        14f * scale + e, 3f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("Bottom Connection")
                .setMetadata(ForgeDirection.DOWN)
                .setBoundingBox(2f * scale - e, 14f * scale - e, 2f * scale - e,
                        14f * scale + e, 16f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("Pipe Body")
                .enabledByDefault()
                .setBoundingBox(3f * scale - e, 3f * scale - e, 3f * scale - e,
                        13f * scale + e, 13f * scale + e, 13f * scale + e);
    }
}
