package mod.steamnsteel.library;

import mod.steamnsteel.utility.blockParts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockParts.PartSet;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by steblo on 31/10/2014.
 */
public final class ModBlockParts {
    private static PartSet pipePartSet;

    public static void init() {
        final float scale = 1f / 16f;
        final float e = 0.00001f;

        pipePartSet = BlockPartConfiguration.registerPartSet("pipes");
        pipePartSet.createBlockPart("North Connection")
                .setBoundingBox(2f * scale - e, 2f * scale - e, 0f * scale - e,
                        14f * scale + e , 14f * scale, 3f * scale + e );

        pipePartSet.createBlockPart("South Connection")
                .setBoundingBox(2f * scale - e, 2f * scale - e, 14f * scale - e,
                        14f * scale + e , 14f * scale, 16f * scale + e );

        pipePartSet.createBlockPart("East Connection")
                .setBoundingBox(0f * scale - e, 2f * scale - e, 2f * scale - e,
                        3f * scale + e, 14f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("West Connection")
                .setBoundingBox(14f * scale - e, 2f * scale - e, 2f * scale - e,
                        16f * scale + e, 14f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("Top Connection")
                .setBoundingBox(2f * scale - e, 0f * scale - e, 2f * scale - e,
                        14f * scale + e, 3f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("Bottom Connection")
                .setBoundingBox(2f * scale - e, 14f * scale - e, 2f * scale - e,
                        14f * scale + e, 16f * scale + e, 14f * scale + e);

        pipePartSet.createBlockPart("Pipe Body")
                .setBoundingBox(3f * scale - e, 3f * scale - e, 3f * scale - e,
                        13f * scale + e, 13f * scale + e, 13f * scale + e);
    }
}
