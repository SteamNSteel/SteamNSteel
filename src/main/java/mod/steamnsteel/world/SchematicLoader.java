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
import net.minecraft.util.MathHelper;
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
import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class SchematicLoader
{
    public boolean loadSchematic(EntityPlayer player, File directory, String filename) {
        SchematicWorld schematic = SchematicFormat.readFromFile(directory, filename);
        if (schematic == null) {
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

    public static abstract class SchematicFormat {
        public static final Map<String, SchematicFormat> FORMATS = new HashMap<String, SchematicFormat>();
        public static String FORMAT_DEFAULT;

        public abstract SchematicWorld readFromNBT(NBTTagCompound tagCompound);

        public abstract boolean writeToNBT(NBTTagCompound tagCompound, SchematicWorld world);

        public static SchematicWorld readFromFile(File file) {
            try {
                final NBTTagCompound tagCompound = readTagCompoundFromFile(file);
                final String format = tagCompound.getString(Names.NBT.MATERIALS);
                final SchematicFormat schematicFormat = FORMATS.get(format);

                if (schematicFormat == null) {
                    throw new UnsupportedFormatException(format);
                }

                return schematicFormat.readFromNBT(tagCompound);
            } catch (Exception ex) {
                _logger.error("Failed to read schematic!", ex);
            }

            return null;
        }

        public static NBTTagCompound readTagCompoundFromFile(File file) throws IOException
        {
            try {
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            } catch (Exception ex) {
                _logger.warn("Failed compressed read, trying normal read...", ex);
                return CompressedStreamTools.read(file);
            }
        }

        public static SchematicWorld readFromFile(File directory, String filename) {
            return readFromFile(new File(directory, filename));
        }

        public static boolean writeToFile(File file, SchematicWorld world) {
            try {
                NBTTagCompound tagCompound = new NBTTagCompound();

                FORMATS.get(FORMAT_DEFAULT).writeToNBT(tagCompound, world);

                DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

                try {
                    NBTTagCompound.func_150298_a(Names.NBT.ROOT, tagCompound, dataOutputStream);
                } finally {
                    dataOutputStream.close();
                }

                return true;
            } catch (Exception ex) {
                _logger.error("Failed to write schematic!", ex);
            }

            return false;
        }

        public static boolean writeToFile(File directory, String filename, SchematicWorld world) {
            return writeToFile(new File(directory, filename), world);
        }

        static {
            FORMATS.put(Names.NBT.FORMAT_ALPHA, new SchematicAlpha());

            FORMAT_DEFAULT = Names.NBT.FORMAT_ALPHA;
        }
    }

    public static class SchematicAlpha extends SchematicFormat {
        @Override
        public SchematicWorld readFromNBT(NBTTagCompound tagCompound) {
            byte localBlocks[] = tagCompound.getByteArray(Names.NBT.BLOCKS);
            byte localMetadata[] = tagCompound.getByteArray(Names.NBT.DATA);

            boolean extra = false;
            byte extraBlocks[] = null;
            byte extraBlocksNibble[] = null;
            if (tagCompound.hasKey(Names.NBT.ADD_BLOCKS)) {
                extra = true;
                extraBlocksNibble = tagCompound.getByteArray(Names.NBT.ADD_BLOCKS);
                extraBlocks = new byte[extraBlocksNibble.length * 2];
                for (int i = 0; i < extraBlocksNibble.length; i++) {
                    extraBlocks[i * 2 + 0] = (byte) ((extraBlocksNibble[i] >> 4) & 0xF);
                    extraBlocks[i * 2 + 1] = (byte) (extraBlocksNibble[i] & 0xF);
                }
            } else if (tagCompound.hasKey(Names.NBT.ADD_BLOCKS_SCHEMATICA)) {
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
            if (tagCompound.hasKey(Names.NBT.MAPPING_SCHEMATICA)) {
                NBTTagCompound mapping = tagCompound.getCompoundTag(Names.NBT.MAPPING_SCHEMATICA);
                Set<String> names = mapping.func_150296_c();
                for (String name : names) {
                    oldToNew.put(mapping.getShort(name), (short) GameData.getBlockRegistry().getId(name));
                }
            }

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < length; z++) {
                        int index = x + (y * length + z) * width;
                        blocks[x][y][z] = (short) ((localBlocks[index] & 0xFF) | (extra ? ((extraBlocks[index] & 0xFF) << 8) : 0));
                        metadata[x][y][z] = (byte) (localMetadata[index] & 0xFF);
                        if ((id = oldToNew.get(blocks[x][y][z])) != null) {
                            blocks[x][y][z] = id;
                        }
                    }
                }
            }

            List<TileEntity> tileEntities = new ArrayList<TileEntity>();
            NBTTagList tileEntitiesList = tagCompound.getTagList(Names.NBT.TILE_ENTITIES, Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < tileEntitiesList.tagCount(); i++) {
                try {
                    TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntitiesList.getCompoundTagAt(i));
                    if (tileEntity != null) {
                        tileEntities.add(tileEntity);
                    }
                } catch (Exception e) {
                    _logger.error("TileEntity failed to load properly!", e);
                }
            }

            return new SchematicWorld(blocks, metadata, tileEntities, width, height, length);
        }

        @Override
        public boolean writeToNBT(NBTTagCompound tagCompound, SchematicWorld world) {
            tagCompound.setShort(Names.NBT.WIDTH, (short) world.getWidth());
            tagCompound.setShort(Names.NBT.LENGTH, (short) world.getLength());
            tagCompound.setShort(Names.NBT.HEIGHT, (short) world.getHeight());

            int size = world.getWidth() * world.getLength() * world.getHeight();
            byte localBlocks[] = new byte[size];
            byte localMetadata[] = new byte[size];
            byte extraBlocks[] = new byte[size];
            byte extraBlocksNibble[] = new byte[(int) Math.ceil(size / 2.0)];
            boolean extra = false;
            NBTTagCompound mapping = new NBTTagCompound();

            for (int x = 0; x < world.getWidth(); x++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    for (int z = 0; z < world.getLength(); z++) {
                        int index = x + (y * world.getLength() + z) * world.getWidth();
                        int blockId = world.getBlockIdRaw(x, y, z);
                        localBlocks[index] = (byte) blockId;
                        localMetadata[index] = (byte) world.getBlockMetadata(x, y, z);
                        if ((extraBlocks[index] = (byte) (blockId >> 8)) > 0) {
                            extra = true;
                        }

                        String name = GameData.getBlockRegistry().getNameForObject(world.getBlockRaw(x, y, z));
                        if (!mapping.hasKey(name)) {
                            mapping.setShort(name, (short) blockId);
                        }
                    }
                }
            }

            for (int i = 0; i < extraBlocksNibble.length; i++) {
                if (i * 2 + 1 < extraBlocks.length) {
                    extraBlocksNibble[i] = (byte) ((extraBlocks[i * 2 + 0] << 4) | extraBlocks[i * 2 + 1]);
                } else {
                    extraBlocksNibble[i] = (byte) (extraBlocks[i * 2 + 0] << 4);
                }
            }

            int count = 20;
            NBTTagList tileEntitiesList = new NBTTagList();
            for (TileEntity tileEntity : world.getTileEntities()) {
                NBTTagCompound tileEntityTagCompound = new NBTTagCompound();
                try {
                    tileEntity.writeToNBT(tileEntityTagCompound);
                    tileEntitiesList.appendTag(tileEntityTagCompound);
                } catch (Exception e) {
                    int pos = tileEntity.xCoord + (tileEntity.yCoord * world.getLength() + tileEntity.zCoord) * world.getWidth();
                    if (--count > 0) {
                        Block block = world.getBlockRaw(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        _logger.error(String.format("Block %s[%s] with TileEntity %s failed to save! Replacing with bedrock...", block, block != null ? GameData.getBlockRegistry().getNameForObject(block) : "?", tileEntity.getClass().getName()), e);
                    }
                    localBlocks[pos] = (byte) GameData.getBlockRegistry().getId(Blocks.bedrock);
                    localMetadata[pos] = 0;
                    extraBlocks[pos] = 0;
                }
            }

            tagCompound.setString(Names.NBT.MATERIALS, Names.NBT.FORMAT_ALPHA);
            tagCompound.setByteArray(Names.NBT.BLOCKS, localBlocks);
            tagCompound.setByteArray(Names.NBT.DATA, localMetadata);
            if (extra) {
                tagCompound.setByteArray(Names.NBT.ADD_BLOCKS, extraBlocksNibble);
            }
            tagCompound.setTag(Names.NBT.ENTITIES, new NBTTagList());
            tagCompound.setTag(Names.NBT.TILE_ENTITIES, tileEntitiesList);
            tagCompound.setTag(Names.NBT.MAPPING_SCHEMATICA, mapping);

            return true;
        }
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
        private final List<ItemStack> blockList = new ArrayList<ItemStack>();
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

            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            {
                generateBlockList();
            }
        }

        public SchematicWorld(short width, short height, short length)
        {
            this(null, null, null, width, height, length);
        }

        private void generateBlockList()
        {
            this.blockList.clear();

            int x, y, z, itemDamage;
            Block block;
            Item item;
            ItemStack itemStack;

            for (y = 0; y < this.height; y++)
            {
                for (z = 0; z < this.length; z++)
                {
                    for (x = 0; x < this.width; x++)
                    {
                        block = this.getBlock(x, y, z);
                        item = Item.getItemFromBlock(block);
                        itemDamage = this.metadata[x][y][z];

                        if (block == null || block == Blocks.air)
                        {
                            continue;
                        }

                        if (BlockInfo.BLOCK_LIST_IGNORE_BLOCK.contains(block))
                        {
                            continue;
                        }

                        if (BlockInfo.BLOCK_LIST_IGNORE_METADATA.contains(block))
                        {
                            itemDamage = 0;
                        }

                        Item tmp = BlockInfo.BLOCK_ITEM_MAP.get(block);
                        if (tmp != null)
                        {
                            item = tmp;
                            Block blockFromItem = Block.getBlockFromItem(item);
                            if (blockFromItem != Blocks.air)
                            {
                                block = blockFromItem;
                            } else
                            {
                                itemDamage = 0;
                            }
                        }

                        if (block instanceof BlockLog || block instanceof BlockLeavesBase)
                        {
                            itemDamage &= 0x03;
                        }

                        if (block instanceof BlockSlab)
                        {
                            itemDamage &= 0x07;
                        }

                        if (block instanceof BlockDoublePlant)
                        {
                            if ((itemDamage & 0x08) == 0x08)
                            {
                                continue;
                            }
                        }

                        if (block == Blocks.cocoa)
                        {
                            itemDamage = 0x03;
                        }

                        if (item == Items.skull)
                        {
                            TileEntity tileEntity = getTileEntity(x, y, z);
                            if (tileEntity instanceof TileEntitySkull)
                            {
                                itemDamage = ((TileEntitySkull) tileEntity).func_145904_a();
                            }
                        }

                        itemStack = null;
                        for (ItemStack stack : this.blockList)
                        {
                            if (stack.getItem() == item && stack.getItemDamage() == itemDamage)
                            {
                                itemStack = stack;
                                itemStack.stackSize++;
                                break;
                            }
                        }

                        if (itemStack == null)
                        {
                            itemStack = new ItemStack(item, 1, itemDamage);
                            if (itemStack.getItem() != null)
                            {
                                this.blockList.add(itemStack);
                            }
                        }
                    }
                }
            }
            Collections.sort(this.blockList, BLOCK_COMPARATOR);
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

        public List<ItemStack> getBlockList()
        {
            return this.blockList;
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

    static class ChunkProviderSchematic implements IChunkProvider {
        private Chunk emptyChunk;

        public ChunkProviderSchematic(World world) {
            this.emptyChunk = new EmptyChunk(world, 0, 0);
        }

        @Override
        public boolean chunkExists(int x, int y) {
            return true;
        }

        @Override
        public Chunk provideChunk(int x, int y) {
            return this.emptyChunk;
        }

        @Override
        public Chunk loadChunk(int x, int y) {
            return this.emptyChunk;
        }

        @Override
        public void populate(IChunkProvider provider, int x, int y) {}

        @Override
        public boolean saveChunks(boolean saveExtra, IProgressUpdate progressUpdate) {
            return true;
        }

        @Override
        public boolean unloadQueuedChunks() {
            return false;
        }

        @Override
        public boolean canSave() {
            return false;
        }

        @Override
        public String makeString() {
            return "SchematicChunkCache";
        }

        @Override
        public List getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z) {
            return null;
        }

        @Override
        public ChunkPosition func_147416_a(World world, String name, int x, int y, int z) {
            return null;
        }

        @Override
        public int getLoadedChunkCount() {
            return 0;
        }

        @Override
        public void recreateStructures(int x, int y) {}

        @Override
        public void saveExtraData() {}
    }


    public static class BlockInfo {
        public static final List<Block> BLOCK_LIST_IGNORE_BLOCK = new ArrayList<Block>();
        public static final List<Block> BLOCK_LIST_IGNORE_METADATA = new ArrayList<Block>();
        public static final Map<Block, Item> BLOCK_ITEM_MAP = new HashMap<Block, Item>();
        public static final Map<Class, PlacementData> CLASS_PLACEMENT_MAP = new HashMap<Class, PlacementData>();
        public static final Map<Item, PlacementData> ITEM_PLACEMENT_MAP = new HashMap<Item, PlacementData>();

        private static String modId = Names.ModId.MINECRAFT;

        public static void setModId(String modId) {
            BlockInfo.modId = modId;
        }

        public static void populateIgnoredBlocks() {
            BLOCK_LIST_IGNORE_BLOCK.clear();

            /**
             * minecraft
             */
            addIgnoredBlock(Blocks.piston_head);
            addIgnoredBlock(Blocks.piston_extension);
            addIgnoredBlock(Blocks.portal);
            addIgnoredBlock(Blocks.end_portal);
        }

        private static boolean addIgnoredBlock(Block block) {
            if (block == null) {
                return false;
            }

            return BLOCK_LIST_IGNORE_BLOCK.add(block);
        }

        private static boolean addIgnoredBlock(String blockName) {
            if (!Names.ModId.MINECRAFT.equals(modId) && !Loader.isModLoaded(modId)) {
                return false;
            }

            return addIgnoredBlock(GameData.getBlockRegistry().getObject(String.format("%s:%s", modId, blockName)));
        }

        public static void populateIgnoredBlockMetadata() {
            BLOCK_LIST_IGNORE_METADATA.clear();

            /**
             * minecraft
             */
            addIgnoredBlockMetadata(Blocks.flowing_water);
            addIgnoredBlockMetadata(Blocks.water);
            addIgnoredBlockMetadata(Blocks.flowing_lava);
            addIgnoredBlockMetadata(Blocks.lava);
            addIgnoredBlockMetadata(Blocks.dispenser);
            addIgnoredBlockMetadata(Blocks.bed);
            addIgnoredBlockMetadata(Blocks.golden_rail);
            addIgnoredBlockMetadata(Blocks.detector_rail);
            addIgnoredBlockMetadata(Blocks.sticky_piston);
            addIgnoredBlockMetadata(Blocks.piston);
            addIgnoredBlockMetadata(Blocks.torch);
            addIgnoredBlockMetadata(Blocks.oak_stairs);
            addIgnoredBlockMetadata(Blocks.chest);
            addIgnoredBlockMetadata(Blocks.redstone_wire);
            addIgnoredBlockMetadata(Blocks.wheat);
            addIgnoredBlockMetadata(Blocks.farmland);
            addIgnoredBlockMetadata(Blocks.furnace);
            addIgnoredBlockMetadata(Blocks.lit_furnace);
            addIgnoredBlockMetadata(Blocks.standing_sign);
            addIgnoredBlockMetadata(Blocks.wooden_door);
            addIgnoredBlockMetadata(Blocks.ladder);
            addIgnoredBlockMetadata(Blocks.rail);
            addIgnoredBlockMetadata(Blocks.stone_stairs);
            addIgnoredBlockMetadata(Blocks.wall_sign);
            addIgnoredBlockMetadata(Blocks.lever);
            addIgnoredBlockMetadata(Blocks.stone_pressure_plate);
            addIgnoredBlockMetadata(Blocks.iron_door);
            addIgnoredBlockMetadata(Blocks.wooden_pressure_plate);
            addIgnoredBlockMetadata(Blocks.unlit_redstone_torch);
            addIgnoredBlockMetadata(Blocks.redstone_torch);
            addIgnoredBlockMetadata(Blocks.stone_button);
            addIgnoredBlockMetadata(Blocks.cactus);
            addIgnoredBlockMetadata(Blocks.reeds);
            addIgnoredBlockMetadata(Blocks.pumpkin);
            addIgnoredBlockMetadata(Blocks.portal);
            addIgnoredBlockMetadata(Blocks.lit_pumpkin);
            addIgnoredBlockMetadata(Blocks.cake);
            addIgnoredBlockMetadata(Blocks.unpowered_repeater);
            addIgnoredBlockMetadata(Blocks.powered_repeater);
            addIgnoredBlockMetadata(Blocks.trapdoor);
            addIgnoredBlockMetadata(Blocks.vine);
            addIgnoredBlockMetadata(Blocks.fence_gate);
            addIgnoredBlockMetadata(Blocks.brick_stairs);
            addIgnoredBlockMetadata(Blocks.stone_brick_stairs);
            addIgnoredBlockMetadata(Blocks.waterlily);
            addIgnoredBlockMetadata(Blocks.nether_brick_stairs);
            addIgnoredBlockMetadata(Blocks.nether_wart);
            addIgnoredBlockMetadata(Blocks.end_portal_frame);
            addIgnoredBlockMetadata(Blocks.redstone_lamp);
            addIgnoredBlockMetadata(Blocks.lit_redstone_lamp);
            addIgnoredBlockMetadata(Blocks.sandstone_stairs);
            addIgnoredBlockMetadata(Blocks.ender_chest);
            addIgnoredBlockMetadata(Blocks.tripwire_hook);
            addIgnoredBlockMetadata(Blocks.tripwire);
            addIgnoredBlockMetadata(Blocks.spruce_stairs);
            addIgnoredBlockMetadata(Blocks.birch_stairs);
            addIgnoredBlockMetadata(Blocks.jungle_stairs);
            addIgnoredBlockMetadata(Blocks.command_block);
            addIgnoredBlockMetadata(Blocks.flower_pot);
            addIgnoredBlockMetadata(Blocks.carrots);
            addIgnoredBlockMetadata(Blocks.potatoes);
            addIgnoredBlockMetadata(Blocks.wooden_button);
            addIgnoredBlockMetadata(Blocks.anvil);
            addIgnoredBlockMetadata(Blocks.trapped_chest);
            addIgnoredBlockMetadata(Blocks.hopper);
            addIgnoredBlockMetadata(Blocks.quartz_stairs);
            addIgnoredBlockMetadata(Blocks.dropper);
        }

        private static boolean addIgnoredBlockMetadata(Block block) {
            if (block == null) {
                return false;
            }

            return BLOCK_LIST_IGNORE_METADATA.add(block);
        }

        private static boolean addIgnoredBlockMetadata(String blockName) {
            if (!Names.ModId.MINECRAFT.equals(modId) && !Loader.isModLoaded(modId)) {
                return false;
            }

            return addIgnoredBlockMetadata(GameData.getBlockRegistry().getObject(String.format("%s:%s", modId, blockName)));
        }

        public static void populateBlockItemMap() {
            BLOCK_ITEM_MAP.clear();

            /**
             * minecraft
             */
            addBlockItemMapping(Blocks.flowing_water, Items.water_bucket);
            addBlockItemMapping(Blocks.water, Items.water_bucket);
            addBlockItemMapping(Blocks.flowing_lava, Items.lava_bucket);
            addBlockItemMapping(Blocks.lava, Items.lava_bucket);
            addBlockItemMapping(Blocks.bed, Items.bed);
            addBlockItemMapping(Blocks.redstone_wire, Items.redstone);
            addBlockItemMapping(Blocks.wheat, Items.wheat_seeds);
            addBlockItemMapping(Blocks.lit_furnace, Blocks.furnace);
            addBlockItemMapping(Blocks.standing_sign, Items.sign);
            addBlockItemMapping(Blocks.wooden_door, Items.wooden_door);
            addBlockItemMapping(Blocks.iron_door, Items.iron_door);
            addBlockItemMapping(Blocks.wall_sign, Items.sign);
            addBlockItemMapping(Blocks.unlit_redstone_torch, Blocks.redstone_torch);
            addBlockItemMapping(Blocks.reeds, Items.reeds);
            addBlockItemMapping(Blocks.unpowered_repeater, Items.repeater);
            addBlockItemMapping(Blocks.powered_repeater, Items.repeater);
            addBlockItemMapping(Blocks.pumpkin_stem, Items.pumpkin_seeds);
            addBlockItemMapping(Blocks.melon_stem, Items.melon_seeds);
            addBlockItemMapping(Blocks.nether_wart, Items.nether_wart);
            addBlockItemMapping(Blocks.brewing_stand, Items.brewing_stand);
            addBlockItemMapping(Blocks.cauldron, Items.cauldron);
            addBlockItemMapping(Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
            addBlockItemMapping(Blocks.cocoa, Items.dye);
            addBlockItemMapping(Blocks.tripwire, Items.string);
            addBlockItemMapping(Blocks.flower_pot, Items.flower_pot);
            addBlockItemMapping(Blocks.carrots, Items.carrot);
            addBlockItemMapping(Blocks.potatoes, Items.potato);
            addBlockItemMapping(Blocks.skull, Items.skull);
            addBlockItemMapping(Blocks.unpowered_comparator, Items.comparator);
            addBlockItemMapping(Blocks.powered_comparator, Items.comparator);
        }

        private static Item addBlockItemMapping(Block block, Item item) {
            if (block == null || item == null) {
                return null;
            }

            return BLOCK_ITEM_MAP.put(block, item);
        }

        private static Item addBlockItemMapping(Block block, Block item) {
            return addBlockItemMapping(block, Item.getItemFromBlock(item));
        }

        private static Item addBlockItemMapping(Object blockObj, Object itemObj) {
            if (!Names.ModId.MINECRAFT.equals(modId) && !Loader.isModLoaded(modId)) {
                return null;
            }

            Block block = null;
            Item item = null;

            if (blockObj instanceof Block) {
                block = (Block) blockObj;
            } else if (blockObj instanceof String) {
                block = GameData.getBlockRegistry().getObject(String.format("%s:%s", modId, blockObj));
            }

            if (itemObj instanceof Item) {
                item = (Item) itemObj;
            } else if (itemObj instanceof Block) {
                item = Item.getItemFromBlock((Block) itemObj);
            } else if (itemObj instanceof String) {
                String formattedName = String.format("%s:%s", modId, itemObj);
                item = GameData.getItemRegistry().getObject(formattedName);
                if (item == null) {
                    item = Item.getItemFromBlock(GameData.getBlockRegistry().getObject(formattedName));
                }
            }

            return addBlockItemMapping(block, item);
        }

        public static Item getItemFromBlock(Block block) {
            Item item = BLOCK_ITEM_MAP.get(block);
            if (item != null) {
                return item;
            }

            return Item.getItemFromBlock(block);
        }

        public static void populatePlacementMaps() {
            ITEM_PLACEMENT_MAP.clear();

            /**
             * minecraft
             */
            addPlacementMapping(BlockButton.class, new PlacementData(PlacementType.BLOCK, -1, -1, 3, 4, 1, 2).setMaskMeta(0x7));
            addPlacementMapping(BlockChest.class, new PlacementData(PlacementType.PLAYER, -1, -1, 3, 2, 5, 4));
            addPlacementMapping(BlockDispenser.class, new PlacementData(PlacementType.PISTON, 0, 1, 2, 3, 4, 5).setMaskMeta(0x7));
            addPlacementMapping(BlockEnderChest.class, new PlacementData(PlacementType.PLAYER, -1, -1, 3, 2, 5, 4));
            addPlacementMapping(BlockFurnace.class, new PlacementData(PlacementType.PLAYER, -1, -1, 3, 2, 5, 4));
            addPlacementMapping(BlockHopper.class, new PlacementData(PlacementType.BLOCK, 0, 1, 2, 3, 4, 5).setMaskMeta(0x7));
            addPlacementMapping(BlockLog.class, new PlacementData(PlacementType.BLOCK, 0, 0, 8, 8, 4, 4).setMaskMeta(0xC).setMaskMetaInHand(0x3));
            addPlacementMapping(BlockPistonBase.class, new PlacementData(PlacementType.PISTON, 0, 1, 2, 3, 4, 5).setMaskMeta(0x7));
            addPlacementMapping(BlockPumpkin.class, new PlacementData(PlacementType.PLAYER, -1, -1, 0, 2, 3, 1).setMaskMeta(0xF));
            addPlacementMapping(BlockStairs.class, new PlacementData(PlacementType.PLAYER, -1, -1, 3, 2, 1, 0).setOffset(0x4, 0.0f, 1.0f).setMaskMeta(0x3));
            addPlacementMapping(BlockTorch.class, new PlacementData(PlacementType.BLOCK, 5, -1, 3, 4, 1, 2).setMaskMeta(0xF));

            addPlacementMapping(Blocks.dirt, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.planks, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.sandstone, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.wool, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.yellow_flower, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.red_flower, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.double_stone_slab, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.stone_slab, new PlacementData(PlacementType.BLOCK).setOffset(0x8, 0.0f, 1.0f).setMaskMeta(0x7).setMaskMetaInHand(0x7));
            addPlacementMapping(Blocks.stained_glass, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.ladder, new PlacementData(PlacementType.BLOCK, -1, -1, 3, 2, 5, 4));
            addPlacementMapping(Blocks.lever, new PlacementData(PlacementType.BLOCK, -1, -1, 3, 4, 1, 2).setMaskMeta(0x7));
            addPlacementMapping(Blocks.snow_layer, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0x7));
            addPlacementMapping(Blocks.trapdoor, new PlacementData(PlacementType.BLOCK, -1, -1, 1, 0, 3, 2).setOffset(0x8, 0.0f, 1.0f).setMaskMeta(0x3));
            addPlacementMapping(Blocks.monster_egg, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.stonebrick, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.tripwire_hook, new PlacementData(PlacementType.BLOCK, -1, -1, 0, 2, 3, 1).setMaskMeta(0x3));
            addPlacementMapping(Blocks.quartz_block, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.fence_gate, new PlacementData(PlacementType.PLAYER, -1, -1, 2, 0, 1, 3).setMaskMeta(0x3));
            addPlacementMapping(Blocks.double_wooden_slab, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.wooden_slab, new PlacementData(PlacementType.BLOCK).setOffset(0x8, 0.0f, 1.0f).setMaskMeta(0x7).setMaskMetaInHand(0x7));
            addPlacementMapping(Blocks.anvil, new PlacementData(PlacementType.PLAYER, -1, -1, 1, 3, 0, 2).setMaskMeta(0x3).setMaskMetaInHand(0xC).setBitShiftMetaInHand(2));
            addPlacementMapping(Blocks.stained_hardened_clay, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.carpet, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Blocks.stained_glass_pane, new PlacementData(PlacementType.BLOCK).setMaskMetaInHand(0xF));
            addPlacementMapping(Items.wooden_door, new PlacementData(PlacementType.PLAYER, -1, -1, 3, 1, 2, 0).setMaskMeta(0x7));
            addPlacementMapping(Items.iron_door, new PlacementData(PlacementType.PLAYER, -1, -1, 3, 1, 2, 0).setMaskMeta(0x7));
            addPlacementMapping(Items.repeater, new PlacementData(PlacementType.PLAYER, -1, -1, 0, 2, 3, 1).setMaskMeta(0x3));
            addPlacementMapping(Items.comparator, new PlacementData(PlacementType.PLAYER, -1, -1, 0, 2, 3, 1).setMaskMeta(0x3));
        }

        public static PlacementData addPlacementMapping(Class clazz, PlacementData data) {
            if (clazz == null || data == null) {
                return null;
            }

            return CLASS_PLACEMENT_MAP.put(clazz, data);
        }

        public static PlacementData addPlacementMapping(Item item, PlacementData data) {
            if (item == null || data == null) {
                return null;
            }

            return ITEM_PLACEMENT_MAP.put(item, data);
        }

        public static PlacementData addPlacementMapping(Block block, PlacementData data) {
            return addPlacementMapping(Item.getItemFromBlock(block), data);
        }

        public static PlacementData addPlacementMapping(Object itemObj, PlacementData data) {
            if (itemObj == null || data == null) {
                return null;
            }

            Item item = null;

            if (itemObj instanceof Item) {
                item = (Item) itemObj;
            } else if (itemObj instanceof Block) {
                item = Item.getItemFromBlock((Block) itemObj);
            } else if (itemObj instanceof String) {
                String formattedName = String.format("%s:%s", modId, itemObj);
                item = GameData.getItemRegistry().getObject(formattedName);
                if (item == null) {
                    item = Item.getItemFromBlock(GameData.getBlockRegistry().getObject(formattedName));
                }
            }

            return addPlacementMapping(item, data);
        }

        public static PlacementData getPlacementDataFromItem(Item item) {
            Block block = Block.getBlockFromItem(item);
            PlacementData data = null;

            for (Class clazz : CLASS_PLACEMENT_MAP.keySet()) {
                if (clazz.isInstance(block)) {
                    data = CLASS_PLACEMENT_MAP.get(clazz);
                    break;
                }
            }

            for (Item i : ITEM_PLACEMENT_MAP.keySet()) {
                if (i == item) {
                    data = ITEM_PLACEMENT_MAP.get(i);
                    break;
                }
            }

            return data;
        }

        static {
            populateIgnoredBlocks();
            populateIgnoredBlockMetadata();
            populateBlockItemMap();
            populatePlacementMaps();
        }
    }


    public static enum PlacementType {
        BLOCK, PLAYER, PISTON
    }

    public static class PlacementData {


        public static final ForgeDirection[] VALID_DIRECTIONS = ForgeDirection.VALID_DIRECTIONS;

        public final PlacementType type;
        public int maskOffset = 0x0;
        public float offsetLowY = 0.0f;
        public float offsetHighY = 1.0f;
        public int maskMetaInHand = -1;
        public int bitShiftMetaInHand = 0;
        public int maskMeta = 0xF;
        public final Map<ForgeDirection, Integer> mapping = new HashMap<ForgeDirection, Integer>();

        public PlacementData(PlacementType type, int... metadata) {
            this.type = type;

            for (int i = 0; i < VALID_DIRECTIONS.length && i < metadata.length; i++) {
                if (metadata[i] >= 0x0 && metadata[i] <= 0xF) {
                    this.mapping.put(VALID_DIRECTIONS[i], metadata[i]);
                }
            }
        }

        public PlacementData setOffset(int maskOffset, float offsetLowY, float offsetHighY) {
            this.maskOffset = maskOffset;
            this.offsetLowY = offsetLowY;
            this.offsetHighY = offsetHighY;
            return this;
        }

        public PlacementData setMaskMetaInHand(int maskMetaInHand) {
            this.maskMetaInHand = maskMetaInHand;
            return this;
        }

        public PlacementData setBitShiftMetaInHand(int bitShiftMetaInHand) {
            this.bitShiftMetaInHand = bitShiftMetaInHand;
            return this;
        }

        public PlacementData setMaskMeta(int maskMeta) {
            this.maskMeta = maskMeta;
            return this;
        }

        public float getOffsetFromMetadata(int metadata) {
            return (metadata & this.maskOffset) == 0 ? this.offsetLowY : this.offsetHighY;
        }

        public int getMetaInHand(int metadata) {
            if (this.maskMetaInHand != -1) {
                metadata &= this.maskMetaInHand;
            }

            if (this.bitShiftMetaInHand > 0) {
                metadata >>= this.bitShiftMetaInHand;
            } else if (this.bitShiftMetaInHand < 0) {
                metadata <<= -this.bitShiftMetaInHand;
            }

            return metadata;
        }

        public ForgeDirection[] getValidDirections(ForgeDirection[] solidSides, int metadata) {
            List<ForgeDirection> list = new ArrayList<ForgeDirection>();

            for (ForgeDirection direction : solidSides) {
                if (this.maskOffset != 0) {
                    if ((metadata & this.maskOffset) == 0) {
                        if (this.offsetLowY < 0.5f && direction == ForgeDirection.UP) {
                            continue;
                        }
                    } else {
                        if (this.offsetLowY < 0.5f && direction == ForgeDirection.DOWN) {
                            continue;
                        }
                    }
                }

                if (this.type == PlacementType.BLOCK) {
                    Integer meta = this.mapping.get(direction);
                    if ((meta != null ? meta : -1) != (this.maskMeta & metadata) && this.mapping.size() != 0) {
                        continue;
                    }
                }

                list.add(direction);
            }

            ForgeDirection[] directions = new ForgeDirection[list.size()];
            return list.toArray(directions);
        }
    }


    public static final class Names {
        public static final class ModId {
            public static final String MINECRAFT = "minecraft";
        }


        public static final class NBT {
            public static final String ROOT = "Schematic";

            public static final String MATERIALS = "Materials";
            public static final String FORMAT_ALPHA = "Alpha";

            public static final String BLOCKS = "Blocks";
            public static final String DATA = "Data";
            public static final String ADD_BLOCKS = "AddBlocks";
            public static final String ADD_BLOCKS_SCHEMATICA = "Add";
            public static final String WIDTH = "Width";
            public static final String LENGTH = "Length";
            public static final String HEIGHT = "Height";
            public static final String MAPPING = "..."; // TODO: use this once MCEdit adds support for it
            public static final String MAPPING_SCHEMATICA = "SchematicaMapping";
            public static final String TILE_ENTITIES = "TileEntities";
            public static final String ENTITIES = "Entities";
        }
    }

}
