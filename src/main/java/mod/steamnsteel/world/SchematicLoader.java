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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

                    worldObj.setBlock(commandBlock.xCoord, commandBlock.yCoord, commandBlock.zCoord, Blocks.air, 0, 3);
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

            final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(schematicLocation);
            SchematicWorld schematic = readFromFile(resource.getInputStream());
            if (schematic == null)
            {
                return;
            }

            loadedSchematics.put(schematicLocation, schematic);
            _logger.info(String.format("Loaded %s [w:%d,h:%d,l:%d]", schematicLocation, schematic.getWidth(), schematic.getHeight(), schematic.getLength()));
        } catch (IOException exception)
        {
            _logger.error(String.format("Unable to load %s", schematicLocation), exception);
        }
    }

    public void renderSchematic(ResourceLocation resource, World world, int x, int y, int z, ForgeDirection rotation, boolean flip)
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

        _logger.info("Setting Blocks");
        for (int schematicZ = 0; schematicZ < schematic.getLength(); ++schematicZ)
        {
            _logger.info("Working at z = " + schematicZ);
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
                        world.setBlock(xPos, yPos, zPos, block, blockMetadata, 3);
                    }
                }
            }
        }

        _logger.info("Creating Tile Entities");
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
                    //int index2 = y | z << 8 | x << 12;
                    blocks[index] = (short) ((localBlocks[index] & 0xFF) | (extra ? ((extraBlocks[index] & 0xFF) << 8) : 0));
                    metadata[index] = (byte) (localMetadata[index] & 0xFF);
                    if ((id = oldToNew.get(blocks[index])) != null)
                    {
                        blocks[index] = id;
                    }
                }
            }
        }

        List<NBTTagCompound> tileEntities = new ArrayList<NBTTagCompound>();
        NBTTagList tileEntitiesList = tagCompound.getTagList(Names.NBT.TILE_ENTITIES, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tileEntitiesList.tagCount(); i++)
        {
            try
            {
                NBTTagCompound tileEntity = (NBTTagCompound) tileEntitiesList.getCompoundTagAt(i).copy();
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

    private static class SchematicWorld
    {
        private short[] blocks;
        private byte[] metadata;
        private final List<NBTTagCompound> tileEntities = new ArrayList<NBTTagCompound>();
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

        public SchematicWorld(short[] blocks, byte[] metadata, List<NBTTagCompound> tileEntities, short width, short height, short length)
        {
            this();

            this.blocks = blocks != null ? blocks.clone() : new short[width * height * length];
            this.metadata = metadata != null ? metadata.clone() : new byte[width * height * length];

            this.width = width;
            this.height = height;
            this.length = length;

            if (tileEntities != null)
            {
                this.tileEntities.addAll(tileEntities);
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

        public List<NBTTagCompound> getTileEntityData()
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

    public interface ITileEntityLoadedEvent
    {
        boolean onTileEntityAdded(TileEntity tileEntity);
    }
}
