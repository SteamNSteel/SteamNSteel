package mod.steamnsteel.world.structure.remnantruins;

import mod.steamnsteel.world.SchematicLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class RuinSchematic
{
    public final SchematicLoader.ISchematicMetadata schematicMetadata;
    public final ResourceLocation resource;
    public final int maxWidth;
    public final int heightOffset;
    public final int maxLength;
    public final int xOffset;
    public final int zOffset;

    public RuinSchematic(ResourceLocation resource, SchematicLoader.ISchematicMetadata schematicMetadata) {
        this.resource = resource;
        this.schematicMetadata = schematicMetadata;

        int width = schematicMetadata.getWidth();
        int length = schematicMetadata.getLength();
        int heightOffset = 0;
        int xOffset = 0;
        int zOffset = 0;

        NBTTagCompound nbt = this.schematicMetadata.getExtendedMetadata();
        if (nbt != null && nbt.hasKey("Origin")) {
            final NBTTagCompound originNBT = nbt.getCompoundTag("Origin");
            xOffset = originNBT.getInteger("X");
            zOffset = originNBT.getInteger("Z");
            width += xOffset / 2;
            length += zOffset / 2;
            heightOffset = originNBT.getInteger("Y");
        }
        this.maxWidth = width;
        this.heightOffset = heightOffset;
        this.maxLength = length;
        this.xOffset = xOffset;
        this.zOffset = zOffset;


    }
}
