package mod.steamnsteel.commands;

import mod.steamnsteel.entity.SteamSpiderEntity;
import mod.steamnsteel.entity.Swarm;
import mod.steamnsteel.entity.SwarmManager;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class CommandCreateSwarm extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "createswarm";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender)
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        int spiderCount = args.length == 1 ? Integer.valueOf(args[0]) : 0;
        SwarmManager swarmManager = SwarmManager.swarmManagers.get(commandSender.getEntityWorld());
        EntityPlayer player = getCommandSenderAsPlayer(commandSender);
        Swarm swarm = new Swarm<SteamSpiderEntity>(commandSender.getEntityWorld(), ChunkCoord.of(player.chunkCoordX, player.chunkCoordZ),
                ChunkBlockCoord.of(8, MathHelper.floor_double(player.posY), 8), SteamSpiderEntity.class);

        for (int i = 0; i < spiderCount; i++)
        {
            SteamSpiderEntity spiderEntity = new SteamSpiderEntity(commandSender.getEntityWorld());
            spiderEntity.setSwarm(swarm);
            spiderEntity.setPosition(player.posX, player.posY, player.posZ);
            commandSender.getEntityWorld().spawnEntityInWorld(spiderEntity);
        }
        swarmManager.addSwarm(swarm);
    }
}
