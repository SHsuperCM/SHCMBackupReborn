package shcm.shsupercm.forge.shcmbackupreborn.server.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.FMLServerHandler;

public class CommandDebug extends CommandBase {
    public static boolean shouldRestart = false;
    @Override
    public String getName() {
        return "moddebug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        FMLServerHandler.instance().getServer().worlds[0].flushToDisk();
        FMLServerHandler.instance().getServer().loadAllWorlds();
        shouldRestart = true;
    }
}
