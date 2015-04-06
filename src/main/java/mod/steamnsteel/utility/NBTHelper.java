package mod.steamnsteel.utility;

import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper
{
    public static void writeWorldBlockCoord(NBTTagCompound nbt, String name, WorldBlockCoord worldBlockCoord) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("x", worldBlockCoord.getX());
        compound.setInteger("y", worldBlockCoord.getY());
        compound.setInteger("z", worldBlockCoord.getZ());
        nbt.setTag(name, compound);
    }

    public static WorldBlockCoord readWorldBlockCoord(NBTTagCompound nbt, String name) {
        if (!nbt.hasKey(name)) return null;
        NBTTagCompound compound = (NBTTagCompound)nbt.getTag(name);
        int x = compound.getInteger("x");
        int y = compound.getInteger("y");
        int z = compound.getInteger("z");
        return WorldBlockCoord.of(x, y, z);
    }
}
