package mod.steamnsteel.tileentity;

import mod.steamnsteel.factory.IFactoryTE;
import mod.steamnsteel.utility.NBTHelper;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.MathHelper;

public class SpiderFactoryTE extends SteamNSteelTE implements IFactoryTE
{
    public static final String CURR_HEALTH = "health";
    private static final String IS_SLAVE = "isSlave";

    public static final int MAX_HEALTH = 50;

    //Repair static values
    public static final int REPAIR_AMOUNT_PERCENT = 10;
    public static final int REPAIR_TIME = 200;
    public static final int REPAIR_COOLDOWN = 600;

    //Lockdown static values
    public static final int LOCKDOWN_TIME = 200;
    public static final int LOCKDOWN_COOLDOWN = 600;

    private float health = 50;

    //Repair values
    private int repairTime = -1;
    private float healthToRepair;

    //Lockdown values
    private int lockdownTime = -1;

    private WorldBlockCoord masterLocation;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        health = nbt.getFloat(CURR_HEALTH);
        masterLocation = NBTHelper.readWorldBlockCoord(nbt, "master");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setFloat(CURR_HEALTH, health);
        if (masterLocation != null) {
            NBTHelper.writeWorldBlockCoord(nbt, "master", this.masterLocation);
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            //Repair
            if (repairTime > -1) {
                //Check if we should continue repairing
                if (health >= MAX_HEALTH || repairTime >= REPAIR_TIME) {
                    repairTime = -REPAIR_COOLDOWN;
                }
                else {
                    repairTime++;
                    health = MathHelper.clamp_float(health + (healthToRepair / REPAIR_TIME), 0, MAX_HEALTH);
                }
            }
            else if (repairTime < -1) {
                repairTime++;
            }

            //Lockdown
            else if (lockdownTime > -1) {
                lockdownTime++;
            }
            else if (lockdownTime < -1) {
                lockdownTime++;
            }

            if (repairTime == -1 && health < (MAX_HEALTH / 2)) {
                //Begin repair phase
                healthToRepair = (MAX_HEALTH / 100F) * (REPAIR_AMOUNT_PERCENT * worldObj.difficultySetting.getDifficultyId());
                repairTime = 0;
                //worldObj.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, TheMod.MOD_ID + ":block.welding", 1.0F, worldObj.rand.nextFloat()  * 0.1F + 0.5F);
                //Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147675_a(new ResourceLocation(TheMod.MOD_ID, "block.welding"), xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F));

                Logger.info("Beginning repair phase for factory (%s, %s, %s) to repair %s health over %s ticks", xCoord, yCoord, zCoord, healthToRepair, repairTime);
            }
        }
    }

    public boolean isSlave() {
        return masterLocation != null;
    }

    public void setParent(WorldBlockCoord masterLocation)
    {
        this.masterLocation = masterLocation;
        sendUpdate();
    }

    protected void sendUpdate()
    {
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public WorldBlockCoord getMaster()
    {
        return this.masterLocation;
    }
}
