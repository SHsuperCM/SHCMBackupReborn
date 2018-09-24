package SHCM.SHsuperCM.forge.shcmbackupreborn.server.commands;

import SHCM.SHsuperCM.forge.shcmbackupreborn.server.BackupsHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;
import java.util.List;

public class CommandBackup extends CommandBase {

    @Override
    public String getName() { return "backup"; }
    @Override
    public String getUsage(ICommandSender sender) { return "chat.shcmbackupreborn.backup.usage"; }
    @Override
    public int getRequiredPermissionLevel() { return 2; }
    @Override
    public List<String> getAliases() { return Arrays.asList("bk"); }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String comment = args.length > 0 ? String.join(" ",args) : "";

        BackupsHandler.backup(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getSaveHandler().getWorldDirectory(),comment,true);
    }
}
