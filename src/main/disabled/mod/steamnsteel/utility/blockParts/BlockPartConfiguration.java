package mod.steamnsteel.utility.blockParts;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by steblo on 31/10/2014.
 */
public class BlockPartConfiguration
{
    private final PartSet partSet;
    private final boolean[] enabledFlags;
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
    private Comparator<PartSelectionHelper> distanceComparator = distanceComparator = new Comparator<PartSelectionHelper>()
    {
        @Override
        public int compare(PartSelectionHelper partSelectionHelper, PartSelectionHelper partSelectionHelper2)
        {
            return Double.compare(partSelectionHelper.distance, partSelectionHelper2.distance);
        }
    };

    public BlockPartConfiguration(int partSetId)
    {
        this.partSet = partSets.get(partSetId);
        enabledFlags = partSet.getEnabledFlags();
    }

    public Iterable<BlockPart> getBlockPartsIntersecting(TileEntity tileEntity, final Vec3 posVec, final Vec3 lookVec)
    {
        return FluentIterable.from(
                FluentIterable.from(
                        partSet.getParts()).transform(new Function<BlockPart, PartSelectionHelper>()
                        {
                            @Override
                            public PartSelectionHelper apply(BlockPart blockPart)
                            {
                                final AxisAlignedBB boundingBox = blockPart.getBoundingBox();
                                MovingObjectPosition intercept = boundingBox.calculateIntercept(posVec, lookVec);

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

    public boolean isEnabled(BlockPart part)
    {
        return enabledFlags[part.index];
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

    public void setEnabled(BlockPart part, boolean enabled)
    {
        enabledFlags[part.index] = enabled;
    }

    public BlockPart getBlockPartByKey(Object key)
    {
        return partSet.getBlockPartByKey(key);
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
}
