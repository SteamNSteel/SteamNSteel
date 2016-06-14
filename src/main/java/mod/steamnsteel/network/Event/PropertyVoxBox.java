package mod.steamnsteel.network.Event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Created by Probook on 8/15/2014.
 */
public class PropertyVoxBox implements IExtendedEntityProperties{
    public static final String EXT_PROP_NAME = "PropertyVoxBox";
    private EntityPlayer player;
    private boolean hasVoxBoxEnabled;

    public PropertyVoxBox(EntityPlayer player){
        this.player = player;
        hasVoxBoxEnabled = false;
    }

    public static final void register(EntityPlayer player){
        player.registerExtendedProperties(EXT_PROP_NAME, new PropertyVoxBox(player));
    }

    public static final PropertyVoxBox get(EntityPlayer player){
        return (PropertyVoxBox) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setBoolean("voxboxEnabled", hasVoxBoxEnabled);

        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME);
        this.hasVoxBoxEnabled = properties.getBoolean("voxboxEnabled");
    }

    //Apparently useless
    @Override
    public void init(Entity entity, World world) {}

    public boolean isVoxBoxEnabled(){
        return hasVoxBoxEnabled;
    }

    public void setVoxBoxEnabled(boolean isEnabled){
        this.hasVoxBoxEnabled = isEnabled;
    }

    public void sync(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);

        try{
            outputStream.writeBoolean(isVoxBoxEnabled());
        }catch(Exception ex){
            ex.printStackTrace();
        }

        
    }
}
