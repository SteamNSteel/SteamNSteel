package mod.steamnsteel.utility.blockParts;

import java.util.HashMap;

/**
 * Created by steblo on 31/10/2014.
 */
public class PartSet {
    private final String name;
    private final int partSetId;
    private final HashMap<Integer, BlockPart> blockParts = new HashMap<Integer, BlockPart>();
    private int nextBlockPartId = 0;

    public PartSet(String name, int partSetId) {

        this.name = name;
        this.partSetId = partSetId;
    }

    public Iterable<BlockPart> getParts() {
        return blockParts.values();
    }

    public boolean[] getEnabledFlags() {

        return new boolean[blockParts.size()];
    }

    public BlockPart createBlockPart(String s) {
        int blockPartId = nextBlockPartId++;
        BlockPart blockPart = new BlockPart(this, blockPartId);
        blockParts.put(blockPartId, blockPart);
        return blockPart;
    }
}
