package mod.steamnsteel.library;

import mod.steamnsteel.utility.blockParts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockParts.PartSet;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by steblo on 31/10/2014.
 */
public final class ModBlockParts {
    public static void init() {
        final float scale = 1f / 16f;
        final float e = 0.00001f;


        PartSet pipePartSet = BlockPartConfiguration.registerPartSet("pipes");
        pipePartSet.createBlockPart("North Connection")
                .setBoundingBox(0f * scale - e, 0f * scale - e, 0f * scale - e,
                        8f * scale , 8f * scale, 8f * scale );
    }
}
