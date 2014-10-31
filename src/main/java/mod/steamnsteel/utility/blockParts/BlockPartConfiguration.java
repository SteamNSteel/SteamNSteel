package mod.steamnsteel.utility.blockParts;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;

/**
 * Created by steblo on 31/10/2014.
 */
public class BlockPartConfiguration {
    private final PartSet partSet;
    private final boolean[] enabledFlags;
    private Predicate<BlockPart> enabledPartFilter;

    public BlockPartConfiguration(int partSetId) {
        this.partSet = partSets.get(partSetId);
        enabledFlags = partSet.getEnabledFlags();
    }

    public Iterable<BlockPart> getBlockPartsIntersecting(TileEntity tileEntity, final Vec3 posVec, final Vec3 lookVec) {
        enabledPartFilter = new Predicate<BlockPart>() {
            @Override
            public boolean apply(BlockPart input) {
                Vec3 pos = posVec;
                Vec3 look = lookVec;

                /*Vec3 pastBlockPos = posVec.addVector()

                input.getBoundingBox().calculateIntercept()*/

                boolean isInside = input.getBoundingBox().isVecInside(look);
                //return enabledFlags[input.index] && input.getBoundingBox().isVecInside(vec);
                return isInside;
            }
        };

        return FluentIterable.from(partSet.getParts())
                .filter(enabledPartFilter);
    }

    public boolean isEnabled(BlockPart part) {
        return false;
    }

    private static HashMap<Integer, PartSet> partSets = new HashMap<Integer, PartSet>();
    private static int nextPartSetId = 0;
    public static PartSet registerPartSet(String name) {
        int partSetId = (nextPartSetId++);
        PartSet partSet = new PartSet(name, partSetId);
        partSets.put(partSetId, partSet);
        return partSet;
    }
}
