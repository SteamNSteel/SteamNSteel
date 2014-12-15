package mod.steamnsteel.world;

import com.google.common.base.*;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Objects;

public class SchematicLoader
{
    private Map<ResourceLocation, SchematicWorld> loadedSchematics = new HashMap<ResourceLocation, SchematicWorld>();

    private List<ITileEntityLoadedEvent> listeners = new LinkedList<ITileEntityLoadedEvent>();

    public SchematicLoader()
    {
        listeners.add(new ITileEntityLoadedEvent()
        {
            @Override
            public boolean onTileEntityAdded(TileEntity tileEntity)
            {
                if (tileEntity instanceof TileEntityCommandBlock)
                {
                    _logger.info("Activating command Block");

                    final GameRules gameRules = MinecraftServer.getServer().worldServers[0].getGameRules();
                    Boolean commandBlockOutputSetting = gameRules.getGameRuleBooleanValue("commandBlockOutput");
                    gameRules.setOrCreateGameRule("commandBlockOutput", "false");

                    final World worldObj = tileEntity.getWorldObj();
                    TileEntityCommandBlock commandBlock = (TileEntityCommandBlock) tileEntity;
                    Block block = worldObj.getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                    CommandBlockLogic commandblocklogic = commandBlock.func_145993_a();
                    commandblocklogic.func_145755_a(worldObj);
                    worldObj.func_147453_f(commandBlock.xCoord, commandBlock.yCoord, commandBlock.zCoord, block);

                    if (worldObj.getTileEntity(commandBlock.xCoord, commandBlock.yCoord, commandBlock.zCoord) instanceof TileEntityCommandBlock)
                    {
                        worldObj.setBlock(commandBlock.xCoord, commandBlock.yCoord, commandBlock.zCoord, Blocks.air, 0, 3);
                    }
                    gameRules.setOrCreateGameRule("commandBlockOutput", commandBlockOutputSetting.toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void loadSchematic(ResourceLocation schematicLocation)
    {
        try
        {
            if (loadedSchematics.containsKey(schematicLocation))
            {
                return;
            }
            _logger.info(String.format("%s - Loading schematic %s", System.currentTimeMillis(), schematicLocation));

            final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(schematicLocation);
            SchematicWorld schematic = readFromFile(resource.getInputStream());
            if (schematic == null)
            {
                return;
            }

            loadedSchematics.put(schematicLocation, schematic);
            _logger.info(String.format("%s - Loaded %s [w:%d,h:%d,l:%d]", System.currentTimeMillis(), schematicLocation, schematic.getWidth(), schematic.getHeight(), schematic.getLength()));
        } catch (IOException exception)
        {
            _logger.error(String.format("Unable to load %s", schematicLocation), exception);
        }
    }

    public void renderSchematicToSingleChunk(ResourceLocation resource, World world,
                                             int originX, int originY, int originZ,
                                             int chunkX, int chunkZ, ForgeDirection rotation, boolean flip) {
        if (rotation == ForgeDirection.DOWN || rotation == ForgeDirection.UP)
        {
            _logger.error("Unable to load schematic %s, invalid rotation specified: %s", resource, rotation);
            return;
        }

        SchematicWorld schematic = loadedSchematics.get(resource);
        if (schematic == null)
        {
            _logger.error("schematic %s was not loaded", resource);
            return;
        }

        _logger.info(String.format("%s - Rendering Chunk (%d, %d) ", System.currentTimeMillis(), chunkX, chunkZ));

        final int minX = originX;
        final int maxX = originX + schematic.getWidth();
        final int minY = originY;
        final int maxY = originY + schematic.getHeight();
        final int minZ = originZ;
        final int maxZ = originZ + schematic.getLength();

        final int localMinX = minX < (chunkX << 4) ? 0 : (minX & 15);
        final int localMaxX = maxX > ((chunkX << 4) + 15) ? 15 : (maxX & 15);
        final int localMinZ = minZ < (chunkZ << 4) ? 0 : (minZ & 15);
        final int localMaxZ = maxZ > ((chunkZ << 4) + 15) ? 15 : (maxZ & 15);

        Chunk c = world.getChunkFromChunkCoords(chunkX, chunkZ);

        int blockCount = 0;
        Block ignore = Blocks.air;

        LinkedList<TileEntity> createdTileEntities = new LinkedList<TileEntity>();

        for (int chunkLocalZ = localMinZ; chunkLocalZ <= localMaxZ; chunkLocalZ++) {
            for (int y = minY; y < maxY; y++) {
                for (int chunkLocalX = localMinX; chunkLocalX <= localMaxX; chunkLocalX++) {
                    ++blockCount;
                    final int x = chunkLocalX | (chunkX << 4);
                    final int z = chunkLocalZ | (chunkZ << 4);

                    final int schematicX = x - minX;
                    final int schematicY = y - minY;
                    final int schematicZ = z - minZ;

                    try {
                        final Block block = schematic.getBlock(schematicX, schematicY, schematicZ);
                        final int metadata = schematic.getBlockMetadata(schematicX, schematicY, schematicZ);

                        if (c.func_150807_a(chunkLocalX, y, chunkLocalZ, block, metadata)) {
                            world.markBlockForUpdate(x, y, z);
                            final NBTTagCompound tileEntityData = schematic.getTileEntity(schematicX, schematicY, schematicZ);
                            if (block.hasTileEntity(metadata) && tileEntityData != null) {
                                TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntityData);

                                c.func_150812_a(chunkLocalX, y, chunkLocalZ, tileEntity);
                                tileEntity.getBlockType();
                                try
                                {
                                    tileEntity.validate();
                                } catch (Exception e)
                                {
                                    _logger.error(String.format("TileEntity validation for %s failed!", tileEntity.getClass()), e);
                                }

                                createdTileEntities.add(tileEntity);
                            }
                        }
                    } catch (Exception e) {
                        _logger.error("Something went wrong!", e);
                    }
                }
            }
        }

        for (final TileEntity tileEntity : createdTileEntities)
        {
            for (ITileEntityLoadedEvent tileEntityHandler : listeners)
            {
                if (tileEntityHandler.onTileEntityAdded(tileEntity))
                {
                    break;
                }
            }
        }

        c.enqueueRelightChecks();
        c.setChunkModified();
    }

    public void renderSchematicInOneShot(ResourceLocation resource, World world, int x, int y, int z, ForgeDirection rotation, boolean flip)
    {
        long start = System.currentTimeMillis();

        if (rotation == ForgeDirection.DOWN || rotation == ForgeDirection.UP)
        {
            _logger.error("Unable to load schematic %s, invalid rotation specified: %s", resource, rotation);
            return;
        }

        SchematicWorld schematic = loadedSchematics.get(resource);
        if (schematic == null)
        {
            _logger.error("schematic %s was not loaded", resource);
            return;
        }

        boolean useChunkRendering = true;


        if (useChunkRendering)
        {
            int chunkXStart = x >> 4;
            int chunkXEnd = ((x + schematic.getWidth()) >> 4) + 1;
            int chunkZStart = z >> 4;
            int chunkZEnd = ((z + schematic.getLength()) >> 4) + 1;

            for (int chunkX = chunkXStart; chunkX <= chunkXEnd; ++chunkX)
            {
                for (int chunkZ = chunkZStart; chunkZ <= chunkZEnd; ++chunkZ)
                {
                    renderSchematicToSingleChunk(resource, world, x, y, z, chunkX, chunkZ, rotation, flip);
                }
            }
        } else
        {

            _logger.info(String.format("%s - Setting Blocks", System.currentTimeMillis()));
            for (int schematicZ = 0; schematicZ < schematic.getLength(); ++schematicZ)
            {
                _logger.info(String.format("%s - Working at z = " + schematicZ, System.currentTimeMillis()));
                for (int schematicX = 0; schematicX < schematic.getWidth(); ++schematicX)
                {
                    for (int schematicY = 0; schematicY < schematic.getHeight(); ++schematicY)
                    {
                        final int xPos = schematicX + x;
                        final int yPos = schematicY + y;
                        final int zPos = schematicZ + z;
                        final Block block = schematic.getBlock(schematicX, schematicY, schematicZ);
                        if (block != Blocks.air)
                        {
                            final int blockMetadata = schematic.getBlockMetadata(schematicX, schematicY, schematicZ);

                            world.setBlock(xPos, yPos, zPos, block, blockMetadata, 2);
                        }
                    }
                }
            }

            _logger.info(String.format("%s - Creating Tile Entities", System.currentTimeMillis()));
            for (NBTTagCompound entity : schematic.getTileEntityData())
            {
                TileEntity tileEntity = TileEntity.createAndLoadEntity(entity);
                world.setTileEntity(tileEntity.xCoord + x, tileEntity.yCoord + y, tileEntity.zCoord + z, tileEntity);
                tileEntity.getBlockType();
                try
                {
                    tileEntity.validate();
                } catch (Exception e)
                {
                    _logger.error(String.format("TileEntity validation for %s failed!", tileEntity.getClass()), e);
                }

                for (ITileEntityLoadedEvent tileEntityHandler : listeners)
                {
                    if (tileEntityHandler.onTileEntityAdded(tileEntity))
                    {
                        break;
                    }
                }
            }
        }

        long end = System.currentTimeMillis();
        _logger.info(String.format("Writing schematic took %d millis", end - start));
    }

    private static Logger _logger = LogManager.getLogger("SchematicLoader");

    private static final FMLControlledNamespacedRegistry<Block> BLOCK_REGISTRY = GameData.getBlockRegistry();

    public SchematicWorld readFromFile(InputStream inputStream)
    {
        try
        {
            NBTTagCompound result;
            try
            {
                result = CompressedStreamTools.readCompressed(inputStream);
            } catch (Exception ex)
            {
                _logger.warn("Failed compressed read, trying normal read...", ex);
                result = CompressedStreamTools.read(new DataInputStream(new BufferedInputStream(inputStream)));
            }
            final NBTTagCompound tagCompound = result;
            return readFromNBT(tagCompound);
        } catch (Exception ex)
        {
            _logger.error("Failed to read schematic!", ex);
        }

        return null;
    }

    private SchematicWorld readFromNBT(NBTTagCompound tagCompound)
    {
        byte localBlocks[] = tagCompound.getByteArray(Names.NBT.BLOCKS);
        byte localMetadata[] = tagCompound.getByteArray(Names.NBT.DATA);

        boolean extra = false;
        byte extraBlocks[] = null;
        byte extraBlocksNibble[];
        if (tagCompound.hasKey(Names.NBT.ADD_BLOCKS))
        {
            extra = true;
            extraBlocksNibble = tagCompound.getByteArray(Names.NBT.ADD_BLOCKS);
            extraBlocks = new byte[extraBlocksNibble.length * 2];
            for (int i = 0; i < extraBlocksNibble.length; i++)
            {
                extraBlocks[i * 2] = (byte) ((extraBlocksNibble[i] >> 4) & 0xF);
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

        short[] blocks = new short[width * height * length];
        byte[] metadata = new byte[width * height * length];

        Short id;
        Map<Short, Short> oldToNew = new HashMap<Short, Short>();
        if (tagCompound.hasKey(Names.NBT.MAPPING_SCHEMATICA))
        {
            NBTTagCompound mapping = tagCompound.getCompoundTag(Names.NBT.MAPPING_SCHEMATICA);
            @SuppressWarnings("unchecked")
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
                    blocks[index] = (short) ((localBlocks[index] & 0xFF) | (extra ? ((extraBlocks[index] & 0xFF) << 8) : 0));
                    metadata[index] = (byte) (localMetadata[index] & 0xFF);
                    if ((id = oldToNew.get(blocks[index])) != null)
                    {
                        blocks[index] = id;
                    }
                }
            }
        }

        Map<WorldBlockCoord, NBTTagCompound> tileEntities = new HashMap<WorldBlockCoord, NBTTagCompound>();
        NBTTagList tileEntitiesList = tagCompound.getTagList(Names.NBT.TILE_ENTITIES, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tileEntitiesList.tagCount(); i++)
        {
            try
            {
                NBTTagCompound tileEntity = (NBTTagCompound) tileEntitiesList.getCompoundTagAt(i).copy();

                if (tileEntity != null)
                {
                    int x = tileEntity.getInteger("x");
                    int y = tileEntity.getInteger("y");
                    int z = tileEntity.getInteger("z");

                    WorldBlockCoord loc = WorldBlockCoord.of(x, y, z);

                    tileEntities.put(loc, tileEntity);
                }
            } catch (Exception e)
            {
                _logger.error("TileEntity failed to load properly!", e);
            }
        }

        return new SchematicWorld(blocks, metadata, tileEntities, width, height, length);
    }

    private static class SchematicWorld
    {
        private short[] blocks;
        private byte[] metadata;
        private final Map<WorldBlockCoord, NBTTagCompound> tileEntities = new HashMap<WorldBlockCoord, NBTTagCompound>();
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

        public SchematicWorld(short[] blocks, byte[] metadata, Map<WorldBlockCoord, NBTTagCompound> tileEntities, short width, short height, short length)
        {
            this();

            this.blocks = blocks != null ? blocks.clone() : new short[width * height * length];
            this.metadata = metadata != null ? metadata.clone() : new byte[width * height * length];

            this.width = width;
            this.height = height;
            this.length = length;

            if (tileEntities != null)
            {
                this.tileEntities.putAll(tileEntities);
            }
        }

        public Block getBlock(int x, int y, int z)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return BLOCK_REGISTRY.getObjectById(0);
            }
            int index = x + (y * length + z) * width;
            return BLOCK_REGISTRY.getObjectById(this.blocks[index]);
        }

        public int getBlockMetadata(int x, int y, int z)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return 0;
            }
            int index = x + (y * length + z) * width;
            return this.metadata[index];
        }

        public boolean isAirBlock(int x, int y, int z)
        {
            Block block = getBlock(x, y, z);
            return block == null || block.isAir(null, x, y, z);
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

        public Collection<NBTTagCompound> getTileEntityData()
        {
            return this.tileEntities.values();
        }

        public NBTTagCompound getTileEntity(int x, int y, int z) {
            return tileEntities.get(WorldBlockCoord.of(x, y, z));
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

    public interface ITileEntityLoadedEvent
    {
        boolean onTileEntityAdded(TileEntity tileEntity);
    }

    public static class WorldBlockCoord implements Comparable<WorldBlockCoord>
    {
        private final ImmutableTriple<Integer, Integer, Integer> data;

        private WorldBlockCoord(int x, int y, int z) { data = ImmutableTriple.of(x, y, z); }

        public static WorldBlockCoord of(int x, int y, int z) { return new WorldBlockCoord(x, y, z); }

        public int getX() { return data.left; }

        public int getY() { return data.middle; }

        public int getZ() { return data.right; }

        @Override
        public int hashCode()
        {
            return com.google.common.base.Objects.hashCode(data.left, data.middle, data.right);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final WorldBlockCoord that = (WorldBlockCoord) o;
            return data.left.equals(that.data.left)
                    && data.middle.equals(that.data.middle)
                    && data.right.equals(that.data.right);
        }

        @Override
        public String toString()
        {
            return com.google.common.base.Objects.toStringHelper(this)
                    .add("X", data.left)
                    .add("Y", data.middle)
                    .add("Z", data.right)
                    .toString();
        }

        @Override
        public int compareTo(WorldBlockCoord o)
        {
            if (data.left.equals(o.data.left)) return data.middle.equals(o.data.middle)
                    ? data.right.compareTo(o.data.right)
                    : data.middle.compareTo(o.data.middle);

            else return data.left.compareTo(o.data.left);
        }
    }

}
