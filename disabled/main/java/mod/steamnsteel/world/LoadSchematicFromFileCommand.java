package mod.steamnsteel.world;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing;
import java.io.File;

public class LoadSchematicFromFileCommand extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "snsLoadSchematicFromFile";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "snsLoadSchematicFromFile [schematicName]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP) {
            if (args.length < 1) {
                throw new CommandException("Could not execute command");
            }
            String filename = args[0];

            final EntityPlayerMP player = (EntityPlayerMP) sender;
            SchematicLoader loader = new SchematicLoader();

            ResourceLocation schematicLocation = loader.loadSchematic(new File(Minecraft.getMinecraft().mcDataDir, "\\Schematics\\" + filename + ".schematic"));
            loader.renderSchematicInOneShot(schematicLocation, player.getEntityWorld(), player.getPosition(), EnumFacing.NORTH, false);
            sender.addChatMessage(new TextComponentString("Rendered schematic " + filename));
        }
    }
}
