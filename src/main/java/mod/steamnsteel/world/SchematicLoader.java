package mod.steamnsteel.world;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SchematicLoader
{
    public boolean loadSchematic(File directory, String filename)
    {
        SchematicWorld schematic = readFromFile(directory, filename);
        if (schematic == null)
        {
            return false;
        }

        _logger.info(String.format("Loaded %s [w:%d,h:%d,l:%d]", filename, schematic.getWidth(), schematic.getHeight(), schematic.getLength()));
        return true;
    }

    private static Logger _logger;

    private static final FMLControlledNamespacedRegistry<Block> BLOCK_REGISTRY = GameData.getBlockRegistry();

    public SchematicWorld readFromFile(File file)
    {
        try
        {
            final NBTTagCompound tagCompound = readTagCompoundFromFile(file);
            return readFromNBT(tagCompound);
        } catch (Exception ex)
        {
            _logger.error("Failed to read schematic!", ex);
        }

        return null;
    }

    private NBTTagCompound readTagCompoundFromFile(File file) throws IOException
    {
        try
        {
            return CompressedStreamTools.readCompressed(new FileInputStream(file));
        } catch (Exception ex)
        {
            _logger.warn("Failed compressed read, trying normal read...", ex);
            return CompressedStreamTools.read(file);
        }
    }

    public SchematicWorld readFromFile(File directory, String filename)
    {
        return readFromFile(new File(directory, filename));
    }

    public SchematicWorld readFromNBT(NBTTagCompound tagCompound)
    {
        byte localBlocks[] = tagCompound.getByteArray(Names.NBT.BLOCKS);
        byte localMetadata[] = tagCompound.getByteArray(Names.NBT.DATA);

        boolean extra = false;
        byte extraBlocks[] = null;
        byte extraBlocksNibble[] = null;
        if (tagCompound.hasKey(Names.NBT.ADD_BLOCKS))
        {
            extra = true;
            extraBlocksNibble = tagCompound.getByteArray(Names.NBT.ADD_BLOCKS);
            extraBlocks = new byte[extraBlocksNibble.length * 2];
            for (int i = 0; i < extraBlocksNibble.length; i++)
            {
                extraBlocks[i * 2 + 0] = (byte) ((extraBlocksNibble[i] >> 4) & 0xF);
                extraBlocks[i * 2 + 1] = (byte) (extraBlocksNibble[i] & 0xF);
            }
        } else if (tagCompound.hasKey(Names.NBT.ADD_BLOCKS_SCHEMATICA))
        {
            extra = true;
            extraBlocks = tagCompound.getByteArray(Names.NBT.ADD_BLOCKS_SCHEMATICA);
        }

        short width = tagCompound.getShort(Names.NBT.WIDTH);
        short length = tagCompound.getShort(Names.NBT.LENGTH);
        short height = tagCompound.getShort(Names.NBT.HEIGHT);

        short[][][] blocks = new short[width][height][length];
        byte[][][] metadata = new byte[width][height][length];

        Short id;
        Map<Short, Short> oldToNew = new HashMap<Short, Short>();
        if (tagCompound.hasKey(Names.NBT.MAPPING_SCHEMATICA))
        {
            NBTTagCompound mapping = tagCompound.getCompoundTag(Names.NBT.MAPPING_SCHEMATICA);
            Set<String> names = mapping.func_150296_c();
            for (String name : names)
            {
                oldToNew.put(mapping.getShort(name), (short) GameData.getBlockRegistry().getId(name));
            }
        }

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int z = 0; z < length; z++)
                {
                    int index = x + (y * length + z) * width;
                    blocks[x][y][z] = (short) ((localBlocks[index] & 0xFF) | (extra ? ((extraBlocks[index] & 0xFF) << 8) : 0));
                    metadata[x][y][z] = (byte) (localMetadata[index] & 0xFF);
                    if ((id = oldToNew.get(blocks[x][y][z])) != null)
                    {
                        blocks[x][y][z] = id;
                    }
                }
            }
        }

        List<TileEntity> tileEntities = new ArrayList<TileEntity>();
        NBTTagList tileEntitiesList = tagCompound.getTagList(Names.NBT.TILE_ENTITIES, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tileEntitiesList.tagCount(); i++)
        {
            try
            {
                TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntitiesList.getCompoundTagAt(i));
                if (tileEntity != null)
                {
                    tileEntities.add(tileEntity);
                }
            } catch (Exception e)
            {
                _logger.error("TileEntity failed to load properly!", e);
            }
        }

        return new SchematicWorld(blocks, metadata, tileEntities, width, height, length);
    }

    public static class SchematicWorld
    {
        private short[][][] blocks;
        private byte[][][] metadata;
        private final List<TileEntity> tileEntities = new ArrayList<TileEntity>();
        private short width;
        private short height;
        private short length;

        public SchematicWorld()
        {
            this.blocks = null;
            this.metadata = null;
            this.tileEntities.clear();
            this.width = 0;
            this.height = 0;
            this.length = 0;
        }

        public SchematicWorld(short[][][] blocks, byte[][][] metadata, List<TileEntity> tileEntities, short width, short height, short length)
        {
            this();

            this.blocks = blocks != null ? blocks.clone() : new short[width][height][length];
            this.metadata = metadata != null ? metadata.clone() : new byte[width][height][length];

            this.width = width;
            this.height = height;
            this.length = length;

            if (tileEntities != null)
            {
                this.tileEntities.addAll(tileEntities);
                for (TileEntity tileEntity : this.tileEntities)
                {
                    tileEntity.getBlockType();
                    try
                    {
                        tileEntity.validate();
                    } catch (Exception e)
                    {
                        _logger.error(String.format("TileEntity validation for %s failed!", tileEntity.getClass()), e);
                    }
                }
            }
        }

        public int getBlockIdRaw(int x, int y, int z)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return 0;
            }
            return this.blocks[x][y][z];
        }

        private int getBlockId(int x, int y, int z)
        {
            return getBlockIdRaw(x, y, z);
        }

        public Block getBlockRaw(int x, int y, int z)
        {
            return BLOCK_REGISTRY.getObjectById(getBlockIdRaw(x, y, z));
        }

        public Block getBlock(int x, int y, int z)
        {
            return BLOCK_REGISTRY.getObjectById(getBlockId(x, y, z));
        }

        public TileEntity getTileEntity(int x, int y, int z)
        {
            for (TileEntity tileEntity : this.tileEntities)
            {
                if (tileEntity.xCoord == x && tileEntity.yCoord == y && tileEntity.zCoord == z)
                {
                    return tileEntity;
                }
            }
            return null;
        }

        public int getBlockMetadata(int x, int y, int z)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return 0;
            }
            return this.metadata[x][y][z];
        }

        public boolean isAirBlock(int x, int y, int z)
        {
            Block block = getBlock(x, y, z);
            if (block == null)
            {
                return true;
            }
            return block.isAir(null, x, y, z);
        }

        public int getWidth()
        {
            return this.width;
        }

        public int getLength()
        {
            return this.length;
        }

        public int getHeight()
        {
            return this.height;
        }

        public List<TileEntity> getTileEntities()
        {
            return this.tileEntities;
        }
    }

    public static final class Names
    {
        public static final class NBT
        {
            public static final String BLOCKS = "Blocks";
            public static final String DATA = "Data";
            public static final String ADD_BLOCKS = "AddBlocks";
            public static final String ADD_BLOCKS_SCHEMATICA = "Add";
            public static final String WIDTH = "Width";
            public static final String LENGTH = "Length";
            public static final String HEIGHT = "Height";
            public static final String MAPPING_SCHEMATICA = "SchematicaMapping";
            public static final String TILE_ENTITIES = "TileEntities";
        }
    }
}
