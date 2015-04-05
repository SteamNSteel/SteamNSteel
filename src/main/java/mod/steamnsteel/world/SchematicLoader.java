package mod.steamnsteel.world;

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
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

public class SchematicLoader
{
    private static final FMLControlledNamespacedRegistry<Block> BLOCK_REGISTRY = GameData.getBlockRegistry();
    private static Logger _logger = LogManager.getLogger("SchematicLoader");
    private Map<ResourceLocation, SchematicWorld> loadedSchematics = new HashMap<ResourceLocation, SchematicWorld>();
    private List<ITileEntityLoadedEvent> tileEntityLoadedEventListeners = new LinkedList<ITileEntityLoadedEvent>();
    private List<IPreSetBlockEventListener> setBlockEventListeners;
    private List<IUnknownBlockEventListener> unknownBlockEventListener;

    public SchematicLoader()
    {
        tileEntityLoadedEventListeners.add(new ITileEntityLoadedEvent()
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

    public void addSetBlockEventListener(IPreSetBlockEventListener listener)
    {
        if (setBlockEventListeners == null)
        {
            setBlockEventListeners = new LinkedList<IPreSetBlockEventListener>();
        }
        setBlockEventListeners.add(listener);
    }

    public void addUnknownBlockEventListener(IUnknownBlockEventListener listener)
    {
        if (unknownBlockEventListener == null)
        {
            unknownBlockEventListener = new LinkedList<IUnknownBlockEventListener>();
        }
        unknownBlockEventListener.add(listener);
    }

    public ResourceLocation loadSchematic(File file)
    {
        ResourceLocation schematicLocation = null;
        try
        {

            schematicLocation = new ResourceLocation("localFile:" + file.getPath());

            if (loadedSchematics.containsKey(schematicLocation))
            {
                //If you copy this code, you probably want this enabled.
//                return schematicLocation;
            }
            _logger.info(String.format("%s - Loading schematic %s", System.currentTimeMillis(), schematicLocation));

            SchematicWorld schematic = readFromFile(new FileInputStream(file));
            if (schematic == null)
            {
                return null;
            }

            loadedSchematics.put(schematicLocation, schematic);
            _logger.info(String.format("%s - Loaded %s [w:%d,h:%d,l:%d]", System.currentTimeMillis(), schematicLocation, schematic.getWidth(), schematic.getHeight(), schematic.getLength()));
        } catch (IOException exception)
        {
            _logger.error(String.format("Unable to load %s", file.getAbsolutePath()), exception);
        }
        return schematicLocation;
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
                                             int chunkX, int chunkZ, ForgeDirection rotation, boolean flip)
    {
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

        for (int chunkLocalZ = localMinZ; chunkLocalZ <= localMaxZ; chunkLocalZ++)
        {
            for (int y = minY; y < maxY; y++)
            {
                for (int chunkLocalX = localMinX; chunkLocalX <= localMaxX; chunkLocalX++)
                {
                    ++blockCount;
                    final int x = chunkLocalX | (chunkX << 4);
                    final int z = chunkLocalZ | (chunkZ << 4);

                    final int schematicX = x - minX;
                    final int schematicY = y - minY;
                    final int schematicZ = z - minZ;

                    try
                    {
                        WorldBlockCoord worldCoord = new WorldBlockCoord(chunkX << 4 | chunkLocalX, y, chunkZ << 4 | chunkLocalZ);
                        WorldBlockCoord schematicCoord = new WorldBlockCoord(schematicX, schematicY, schematicZ);
                        PreSetBlockEvent event = new PreSetBlockEvent(schematic, world, worldCoord, schematicCoord);

                        if (setBlockEventListeners != null)
                        {
                            for (final IPreSetBlockEventListener listener : setBlockEventListeners)
                            {
                                listener.preBlockSet(event);
                            }
                        }

                        if (event.block != null && c.func_150807_a(chunkLocalX, y, chunkLocalZ, event.block, event.metadata))
                        {
                            world.markBlockForUpdate(x, y, z);
                            final NBTTagCompound tileEntityData = schematic.getTileEntity(schematicX, schematicY, schematicZ);
                            if (event.block.hasTileEntity(event.metadata) && tileEntityData != null)
                            {
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
                    } catch (Exception e)
                    {
                        _logger.error("Something went wrong!", e);
                    }
                }
            }
        }

        for (final TileEntity tileEntity : createdTileEntities)
        {
            for (ITileEntityLoadedEvent tileEntityHandler : tileEntityLoadedEventListeners)
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
                        Block block = schematic.getBlock(schematicX, schematicY, schematicZ);
                        if (block != Blocks.air)
                        {
                            WorldBlockCoord worldCoord = new WorldBlockCoord(xPos, yPos, zPos);
                            WorldBlockCoord schematicCoord = new WorldBlockCoord(schematicX, schematicY, schematicZ);
                            PreSetBlockEvent event = new PreSetBlockEvent(schematic, world, worldCoord, schematicCoord);

                            if (setBlockEventListeners != null)
                            {
                                for (final IPreSetBlockEventListener listener : setBlockEventListeners)
                                {
                                    listener.preBlockSet(event);
                                }
                            }

                            world.setBlock(xPos, yPos, zPos, event.block, event.metadata, 2);
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

                for (ITileEntityLoadedEvent tileEntityHandler : tileEntityLoadedEventListeners)
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

    private SchematicWorld readFromFile(InputStream inputStream)
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
        short currentBadId = -1;
        if (tagCompound.hasKey(Names.NBT.MAPPING_SCHEMATICA))
        {
            NBTTagCompound mapping = tagCompound.getCompoundTag(Names.NBT.MAPPING_SCHEMATICA);
            @SuppressWarnings("unchecked")
            Set<String> names = mapping.func_150296_c();
            for (String name : names)
            {
                if (GameData.getBlockRegistry().containsKey(name))
                {
                    final short id1 = (short) GameData.getBlockRegistry().getId(name);
                    oldToNew.put(mapping.getShort(name), id1);
                } else
                {
                    if (unknownBlockEventListener != null)
                    {
                        UnknownBlockEvent event = new UnknownBlockEvent(name, GameData.getBlockRegistry());
                        for (final IUnknownBlockEventListener listener : unknownBlockEventListener)
                        {
                            listener.unknownBlock(event);
                        }
                        if (event.isRemapped())
                        {
                            oldToNew.put(mapping.getShort(name), event.newId);
                        } else
                        {
                            oldToNew.put(mapping.getShort(name), currentBadId);
                            currentBadId--;
                        }
                    } else
                    {
                        oldToNew.put(mapping.getShort(name), currentBadId);
                        currentBadId--;
                    }
                }
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

        NBTTagCompound extendedMetadata = null;
        if (tagCompound.hasKey(Names.NBT.EXTENDED_METADATA)) {
            extendedMetadata = tagCompound.getCompoundTag(Names.NBT.EXTENDED_METADATA);
        }

        return new SchematicWorld(blocks, metadata, tileEntities, width, height, length, extendedMetadata);
    }

    public ISchematicMetadata getSchematicMetadata(ResourceLocation schematicLocation)
    {
        if (loadedSchematics.containsKey(schematicLocation))
        {
            return loadedSchematics.get(schematicLocation);
        }
        return null;
    }

    public interface ITileEntityLoadedEvent
    {
        boolean onTileEntityAdded(TileEntity tileEntity);
    }

    public interface IPreSetBlockEventListener
    {
        void preBlockSet(PreSetBlockEvent event);
    }

    public interface IUnknownBlockEventListener
    {
        void unknownBlock(UnknownBlockEvent event);
    }

    public static class SchematicWorld implements ISchematicMetadata
    {
        private final Map<WorldBlockCoord, NBTTagCompound> tileEntities = new HashMap<WorldBlockCoord, NBTTagCompound>();
        private NBTTagCompound extendedMetadata;
        private short[] blocks;
        private byte[] metadata;
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

        public SchematicWorld(short[] blocks, byte[] metadata, Map<WorldBlockCoord, NBTTagCompound> tileEntities, short width, short height, short length, NBTTagCompound extendedMetadata)
        {
            this();
            this.extendedMetadata = extendedMetadata;

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
                //return BLOCK_REGISTRY.getObjectById(0);
                return null;
            }
            int index = x + (y * length + z) * width;
            final short blockId = this.blocks[index];

            if (!BLOCK_REGISTRY.containsId(blockId))
            {
                return null;
            }
            return BLOCK_REGISTRY.getObjectById(blockId);
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

        @Override
        public int getWidth()
        {
            return this.width;
        }

        @Override
        public int getLength()
        {
            return this.length;
        }

        @Override
        public int getHeight()
        {
            return this.height;
        }

        @Override
        public NBTTagCompound getExtendedMetadata()
        {

            return extendedMetadata != null ? (NBTTagCompound) extendedMetadata.copy() : null;
        }

        public Collection<NBTTagCompound> getTileEntityData()
        {
            return this.tileEntities.values();
        }

        public NBTTagCompound getTileEntity(int x, int y, int z)
        {
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
            public static final String EXTENDED_METADATA = "ExtendedMetadata";
            public static final String TILE_ENTITIES = "TileEntities";
        }
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

    public interface ISchematicMetadata
    {
        int getWidth();

        int getLength();

        int getHeight();

        NBTTagCompound getExtendedMetadata();
    }


    public class UnknownBlockEvent
    {

        public final String name;
        public final FMLControlledNamespacedRegistry<Block> blockRegistry;
        Short newId;

        public UnknownBlockEvent(String name, FMLControlledNamespacedRegistry<Block> blockRegistry)
        {
            this.name = name;

            this.blockRegistry = blockRegistry;
        }

        public void remap(Block block)
        {
            newId = (short) blockRegistry.getId(block);
        }

        public boolean isRemapped()
        {
            return newId != null;
        }
    }

    public class PreSetBlockEvent
    {
        public final SchematicWorld schematic;
        public final World world;
        public final WorldBlockCoord worldCoord;
        public final WorldBlockCoord schematicCoord;
        private int metadata;
        private Block block;

        public PreSetBlockEvent(SchematicWorld schematic, World world, WorldBlockCoord worldCoord, WorldBlockCoord schematicCoord)
        {
            this.block = schematic.getBlock(schematicCoord.getX(), schematicCoord.getY(), schematicCoord.getZ());
            this.metadata = schematic.getBlockMetadata(schematicCoord.getX(), schematicCoord.getY(), schematicCoord.getZ());
            this.schematic = schematic;
            this.world = world;
            this.worldCoord = worldCoord;
            this.schematicCoord = schematicCoord;
        }

        public Block getBlock()
        {
            return block;
        }

        public int getMetadata()
        {
            return metadata;
        }

        public void replaceBlock(Block replacementBlock, int metadata)
        {
            this.block = replacementBlock;
            this.metadata = metadata;
        }
    }

}
