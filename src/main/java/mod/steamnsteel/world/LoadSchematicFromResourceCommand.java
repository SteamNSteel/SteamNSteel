package mod.steamnsteel.world;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing;

public class LoadSchematicFromResourceCommand extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "snsLoadSchematicFromResource";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "snsLoadSchematicFromResource [schematicName]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {

            if (args.length < 1) {
                throw new CommandException("Could not execute command");
            }
            String filename = args[0];

            final EntityPlayerMP player = (EntityPlayerMP) sender;
            SchematicLoader loader = new SchematicLoader();
            final ResourceLocation schematicLocation = new ResourceLocation(String.format("SteamNSteel:schematics/%s.schematic", filename));

            loader.loadSchematic(schematicLocation);
            loader.renderSchematicInOneShot(schematicLocation, player.getEntityWorld(), sender.getPosition(), EnumFacing.NORTH, false);
            sender.addChatMessage(new ChatComponentText("Potato Spawned."));
        }
    }
}
