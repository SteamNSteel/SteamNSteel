package mod.steamnsteel.utility.blockParts;

import com.google.common.base.Objects;
import java.util.HashMap;

/**
 * Created by steblo on 31/10/2014.
 */
public class PartSet {
    private final String name;
    private final int partSetId;
    private final HashMap<Integer, BlockPart> blockParts = new HashMap<Integer, BlockPart>();
    private final HashMap<Object, BlockPart> blockPartsByKey = new HashMap<Object, BlockPart>();
    private int nextBlockPartId = 0;

    public PartSet(String name, int partSetId) {

        this.name = name;
        this.partSetId = partSetId;
    }

    public Iterable<BlockPart> getParts() {
        return blockParts.values();
    }

    public boolean[] getEnabledFlags() {

        final boolean[] enabledFlags = new boolean[blockParts.size()];
        for (BlockPart part : blockParts.values()) {
            enabledFlags[part.index] = part.isEnabledByDefault();
        }
        return enabledFlags;
    }

    public BlockPart createBlockPart(String name) {
        int blockPartId = nextBlockPartId++;
        BlockPart blockPart = new BlockPart(name, this, blockPartId);
        blockParts.put(blockPartId, blockPart);
        return blockPart;
    }

    public BlockPart getBlockPartByKey(Object key) {
        blockPartsByKey.clear();
        if (blockPartsByKey.size() != blockParts.size()) {
            for (BlockPart part : blockParts.values()) {
                blockPartsByKey.put(part.getKey(), part);
            }
        }
        return blockPartsByKey.get(key);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("name", this.name)
                .add("parts", this.blockParts.size())
                .toString();

    }
}
