package mod.steamnsteel.world;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class SchematicLoaderCommand extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "spawnPotato";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "beep";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (sender instanceof EntityPlayerMP) {
            final EntityPlayerMP player = (EntityPlayerMP) sender;
            SchematicLoader loader = new SchematicLoader();
            final ResourceLocation schematicLocation = new ResourceLocation("SteamNSteel:schematics/potato.schematic");

            loader.loadSchematic(schematicLocation);
            loader.renderSchematicInOneShot(schematicLocation, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ, ForgeDirection.NORTH, false);
            sender.addChatMessage(new ChatComponentText("Potato Spawned."));
        }
    }
}
