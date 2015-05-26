package mod.steamnsteel.event;

import mod.steamnsteel.item.artifact.PerGuiVox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class VoxBoxPlayerProperty implements IExtendedEntityProperties {
    public boolean enabled = false;

    public static final void register(EntityPlayer player){
        player.registerExtendedProperties(PerGuiVox.PROPERTY_ID, new VoxBoxPlayerProperty());
    }

    public static final VoxBoxPlayerProperty get(EntityPlayer player){
        return (VoxBoxPlayerProperty)player.getExtendedProperties(PerGuiVox.PROPERTY_ID);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setBoolean("enabled", enabled);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        enabled = compound.getBoolean("enabled");
    }

    @Override
    public void init(Entity entity, World world) {

    }
}
