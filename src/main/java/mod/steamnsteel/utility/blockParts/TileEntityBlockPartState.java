package mod.steamnsteel.utility.blockParts;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Tracks the state of BlockParts for a TileEntity instance.
 *
 * It also acts as the registry for BlockParts.
 */
public class TileEntityBlockPartState
{
    private final PartSet partSet;
    private final boolean[] enabledFlags;
    private final TileEntity tileEntity;
    private Predicate<PartSelectionHelper> enabledPartFilter = new Predicate<PartSelectionHelper>()
    {
        @Override
        public boolean apply(PartSelectionHelper input)
        {
            return input.highlighted;
        }
    };
    private Function<PartSelectionHelper, BlockPart> unwrapPartSelectionHelper = new Function<PartSelectionHelper, BlockPart>()
    {
        @Override
        public BlockPart apply(PartSelectionHelper partSelectionHelper)
        {
            return partSelectionHelper.blockPart;
        }
    };
    private Comparator<PartSelectionHelper> distanceComparator = new Comparator<PartSelectionHelper>()
    {
        @Override
        public int compare(PartSelectionHelper a, PartSelectionHelper b)
        {
            return Double.compare(a.distance, b.distance);
        }
    };


    public TileEntityBlockPartState(final TileEntity tileEntity, final int partSetId)
    {
        this.tileEntity = tileEntity;
        this.partSet = partSets.get(partSetId);
        enabledFlags = partSet.getEnabledFlags();
    }

    public Iterable<BlockPart> getBlockPartsIntersecting(final Vec3 posVec, final Vec3 lookVec)
    {
        return FluentIterable.from(
                FluentIterable.from(
                        partSet.getParts()).transform(new Function<BlockPart, PartSelectionHelper>()
                        {
                            @Override
                            public PartSelectionHelper apply(BlockPart blockPart)
                            {

                                MovingObjectPosition intercept = blockPart.getBoundingBox().calculateIntercept(posVec, lookVec);

                                boolean highlighted = intercept != null && enabledFlags[blockPart.index];
                                double distance = highlighted ? intercept.hitVec.squareDistanceTo(posVec) : Double.MAX_VALUE;
                                return new PartSelectionHelper(blockPart, distance, highlighted);
                            }
                        })
                        .toSortedList(distanceComparator)
                )
                .filter(enabledPartFilter)
                .transform(unwrapPartSelectionHelper)
                .limit(1);
    }

    public void setEnabled(final BlockPart blockPart, final boolean b)
    {
        enabledFlags[blockPart.index] = b;
    }

    public BlockPart getBlockPartByMetadata(Object metadata){
        return partSet.getByMetadata(metadata);
    }

    private class PartSelectionHelper
    {

        public final BlockPart blockPart;
        public final double distance;
        public final boolean highlighted;

        public PartSelectionHelper(BlockPart blockPart, double distance, boolean highlighted)
        {

            this.blockPart = blockPart;
            this.distance = distance;
            this.highlighted = highlighted;
        }
    }

    private static HashMap<Integer, PartSet> partSets = new HashMap<Integer, PartSet>();
    private static int nextPartSetId = 0;

    public static PartSet registerPartSet(String name)
    {
        int partSetId = (nextPartSetId++);
        PartSet partSet = new PartSet(name, partSetId);
        partSets.put(partSetId, partSet);
        return partSet;
    }
}
