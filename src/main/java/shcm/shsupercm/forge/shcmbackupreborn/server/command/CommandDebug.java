package shcm.shsupercm.forge.shcmbackupreborn.server.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.server.FMLServerHandler;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;

public class CommandDebug extends CommandBase {
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
        sender.sendMessage(new TextComponentString(Boolean.toString(getCommandSenderAsPlayer(sender).connection.getNetworkManager().channel().attr(NetworkDispatcher.FML_DISPATCHER).get().getModList().containsKey(SHCMBackupReborn.MODID))));
    }
}
