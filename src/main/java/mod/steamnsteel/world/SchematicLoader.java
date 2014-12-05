package mod.steamnsteel.world;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Logger;
import javax.vecmath.Vector3f;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SchematicLoader
{
    public boolean loadSchematic(EntityPlayer player, File directory, String filename)
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
    private static final WorldSettings WORLD_SETTINGS = new WorldSettings(0, WorldSettings.GameType.CREATIVE, false, false, WorldType.FLAT);
    private static final Comparator<ItemStack> BLOCK_COMPARATOR = new Comparator<ItemStack>()
    {
        @Override
        public int compare(ItemStack itemStackA, ItemStack itemStackB)
        {
            return itemStackA.getUnlocalizedName().compareTo(itemStackB.getUnlocalizedName());
        }
    };


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

        Short id = null;
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


    private static ISaveHandler NULL_SAVE_HANDLER = new ISaveHandler()
    {
        @Override
        public WorldInfo loadWorldInfo()
        {
            return null;
        }

        @Override
        public void checkSessionLock() throws MinecraftException {}

        @Override
        public IChunkLoader getChunkLoader(WorldProvider provider)
        {
            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo info, NBTTagCompound compound) {}

        @Override
        public void saveWorldInfo(WorldInfo info) {}

        @Override
        public IPlayerFileData getSaveHandler()
        {
            return null;
        }

        @Override
        public void flush() {}

        @Override
        public File getWorldDirectory()
        {
            return null;
        }

        @Override
        public File getMapFileFromName(String name)
        {
            return null;
        }

        @Override
        public String getWorldDirectoryName()
        {
            return null;
        }
    };

    public static class SchematicWorld extends World
    {
        private short[][][] blocks;
        private byte[][][] metadata;
        private final List<TileEntity> tileEntities = new ArrayList<TileEntity>();
        private short width;
        private short height;
        private short length;

//        public final Vector3i position = new Vector3i();

        public SchematicWorld()
        {
            super(NULL_SAVE_HANDLER, "Schematica", WORLD_SETTINGS, null, null);
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
                    tileEntity.setWorldObj(this);
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

        public SchematicWorld(short width, short height, short length)
        {
            this(null, null, null, width, height, length);
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

        @Override
        public Block getBlock(int x, int y, int z)
        {
            return BLOCK_REGISTRY.getObjectById(getBlockId(x, y, z));
        }

        public boolean setBlock(int x, int y, int z, Block block, int metadata)
        {
            return setBlock(x, y, z, block, metadata, 0);
        }

        @Override
        public boolean setBlock(int x, int y, int z, Block block, int metadata, int flags)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return false;
            }

            final int id = BLOCK_REGISTRY.getId(block);
            if (id == -1)
            {
                return false;
            }

            this.blocks[x][y][z] = (short) id;
            this.metadata[x][y][z] = (byte) metadata;
            return true;
        }

        @Override
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

        @Override
        public void setTileEntity(int x, int y, int z, TileEntity tileEntity)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return;
            }

            removeTileEntity(x, y, z);

            this.tileEntities.add(tileEntity);
        }

        @Override
        public void removeTileEntity(int x, int y, int z)
        {
            final Iterator<TileEntity> iterator = this.tileEntities.iterator();
            while (iterator.hasNext())
            {
                final TileEntity tileEntity = iterator.next();
                if (tileEntity.xCoord == x && tileEntity.yCoord == y && tileEntity.zCoord == z)
                {
                    iterator.remove();
                }
            }
        }

        @SideOnly(Side.CLIENT)
        @Override
        public int getSkyBlockTypeBrightness(EnumSkyBlock skyBlock, int x, int y, int z)
        {
            return 15;
        }

        @Override
        public float getLightBrightness(int x, int y, int z)
        {
            return 1.0f;
        }

        @Override
        public int getBlockMetadata(int x, int y, int z)
        {
            if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length)
            {
                return 0;
            }
            return this.metadata[x][y][z];
        }

        @Override
        public boolean isBlockNormalCubeDefault(int x, int y, int z, boolean _default)
        {
            Block block = getBlock(x, y, z);
            if (block == null)
            {
                return false;
            }
            if (block.isNormalCube())
            {
                return true;
            }
            return _default;
        }

        @Override
        protected int func_152379_p()
        {
            return 0;
        }

        @Override
        public boolean isAirBlock(int x, int y, int z)
        {
            Block block = getBlock(x, y, z);
            if (block == null)
            {
                return true;
            }
            return block.isAir(this, x, y, z);
        }

        @Override
        public BiomeGenBase getBiomeGenForCoords(int x, int z)
        {
            return BiomeGenBase.jungle;
        }

        public int getWidth()
        {
            return this.width;
        }

        public int getLength()
        {
            return this.length;
        }

        @Override
        public int getHeight()
        {
            return this.height;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean extendedLevelsInChunkCache()
        {
            return false;
        }

        @Override
        protected IChunkProvider createChunkProvider()
        {
            return new ChunkProviderSchematic(this);
        }

        @Override
        public Entity getEntityByID(int id)
        {
            return null;
        }

        @Override
        public boolean blockExists(int x, int y, int z)
        {
            return false;
        }

        @Override
        public boolean setBlockMetadataWithNotify(int x, int y, int z, int metadata, int flag)
        {
            this.metadata[x][y][z] = (byte) (metadata & 0xFF);
            return true;
        }

        @Override
        public boolean isSideSolid(int x, int y, int z, ForgeDirection side)
        {
            return isSideSolid(x, y, z, side, false);
        }

        @Override
        public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
        {
            Block block = getBlock(x, y, z);
            if (block == null)
            {
                return false;
            }
            return block.isSideSolid(this, x, y, z, side);
        }

        public void setTileEntities(List<TileEntity> tileEntities)
        {
            this.tileEntities.clear();
            this.tileEntities.addAll(tileEntities);
            for (TileEntity tileEntity : this.tileEntities)
            {
                tileEntity.setWorldObj(this);
                try
                {
                    tileEntity.validate();
                } catch (Exception e)
                {
                    _logger.error(String.format("TileEntity validation for %s failed!", tileEntity.getClass()), e);
                }
            }
        }

        public List<TileEntity> getTileEntities()
        {
            return this.tileEntities;
        }

        public void refreshChests()
        {
            for (TileEntity tileEntity : this.tileEntities)
            {
                if (tileEntity instanceof TileEntityChest)
                {
                    TileEntityChest tileEntityChest = (TileEntityChest) tileEntity;
                    tileEntityChest.adjacentChestChecked = false;
                    tileEntityChest.checkForAdjacentChests();
                }
            }
        }

        public void flip()
        {
        /*
        int tmp;
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				for (int z = 0; z < (this.length + 1) / 2; z++) {
					tmp = this.blocks[x][y][z];
					this.blocks[x][y][z] = this.blocks[x][y][this.length - 1 - z];
					this.blocks[x][y][this.length - 1 - z] = tmp;

					if (z == this.length - 1 - z) {
						this.metadata[x][y][z] = BlockInfo.getTransformedMetadataFlip(this.blocks[x][y][z], this.metadata[x][y][z]);
					} else {
						tmp = this.metadata[x][y][z];
						this.metadata[x][y][z] = BlockInfo.getTransformedMetadataFlip(this.blocks[x][y][z], this.metadata[x][y][this.length - 1 - z]);
						this.metadata[x][y][this.length - 1 - z] = BlockInfo.getTransformedMetadataFlip(this.blocks[x][y][this.length - 1 - z], tmp);
					}
				}
			}
		}

		TileEntity tileEntity;
		for (int i = 0; i < this.tileEntities.size(); i++) {
			tileEntity = this.tileEntities.get(i);
			tileEntity.zCoord = this.length - 1 - tileEntity.zCoord;
			tileEntity.blockMetadata = this.metadata[tileEntity.xCoord][tileEntity.yCoord][tileEntity.zCoord];

			if (tileEntity instanceof TileEntitySkull && tileEntity.blockMetadata == 0x1) {
				TileEntitySkull skullTileEntity = (TileEntitySkull) tileEntity;
				int angle = skullTileEntity.func_82119_b();
				int base = 0;
				if (angle <= 7) {
					base = 4;
				} else {
					base = 12;
				}

				skullTileEntity.setSkullRotation((2 * base - angle) & 15);
			}
		}

		refreshChests();
		*/
        }

        public void rotate()
        {
            short[][][] localBlocks = new short[this.length][this.height][this.width];
            byte[][][] localMetadata = new byte[this.length][this.height][this.width];

            for (int y = 0; y < this.height; y++)
            {
                for (int z = 0; z < this.length; z++)
                {
                    for (int x = 0; x < this.width; x++)
                    {
                        try
                        {
                            getBlock(x, y, this.length - 1 - z).rotateBlock(this, x, y, this.length - 1 - z, ForgeDirection.UP);
                        } catch (Exception e)
                        {
                            _logger.debug("Failed to rotate block!", e);
                        }
                        localBlocks[z][y][x] = this.blocks[x][y][this.length - 1 - z];
                        localMetadata[z][y][x] = this.metadata[x][y][this.length - 1 - z];
                    }
                }
            }

            this.blocks = localBlocks;
            this.metadata = localMetadata;

            int coord;
            for (TileEntity tileEntity : this.tileEntities)
            {
                coord = tileEntity.zCoord;
                tileEntity.zCoord = tileEntity.xCoord;
                tileEntity.xCoord = this.length - 1 - coord;
                tileEntity.blockMetadata = this.metadata[tileEntity.xCoord][tileEntity.yCoord][tileEntity.zCoord];

                if (tileEntity instanceof TileEntitySkull && tileEntity.blockMetadata == 0x1)
                {
                    TileEntitySkull skullTileEntity = (TileEntitySkull) tileEntity;
                    skullTileEntity.func_145903_a((skullTileEntity.func_145906_b() + 12) & 15);
                }
            }

            short tmp = this.width;
            this.width = this.length;
            this.length = tmp;

            refreshChests();
        }

        public Vector3f dimensions()
        {
            return new Vector3f(this.width, this.height, this.length);
        }
    }

    static class ChunkProviderSchematic implements IChunkProvider
    {
        private Chunk emptyChunk;

        public ChunkProviderSchematic(World world)
        {
            this.emptyChunk = new EmptyChunk(world, 0, 0);
        }

        @Override
        public boolean chunkExists(int x, int y)
        {
            return true;
        }

        @Override
        public Chunk provideChunk(int x, int y)
        {
            return this.emptyChunk;
        }

        @Override
        public Chunk loadChunk(int x, int y)
        {
            return this.emptyChunk;
        }

        @Override
        public void populate(IChunkProvider provider, int x, int y) {}

        @Override
        public boolean saveChunks(boolean saveExtra, IProgressUpdate progressUpdate)
        {
            return true;
        }

        @Override
        public boolean unloadQueuedChunks()
        {
            return false;
        }

        @Override
        public boolean canSave()
        {
            return false;
        }

        @Override
        public String makeString()
        {
            return "SchematicChunkCache";
        }

        @Override
        public List getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z)
        {
            return null;
        }

        @Override
        public ChunkPosition func_147416_a(World world, String name, int x, int y, int z)
        {
            return null;
        }

        @Override
        public int getLoadedChunkCount()
        {
            return 0;
        }

        @Override
        public void recreateStructures(int x, int y) {}

        @Override
        public void saveExtraData() {}
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
