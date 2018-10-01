package SHCM.SHsuperCM.forge.shcmbackupreborn.server.command;

import SHCM.SHsuperCM.forge.shcmbackupreborn.server.BackupsHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.nio.file.InvalidPathException;
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
        try {
            BackupsHandler.backup(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getSaveHandler().getWorldDirectory(), buildString(args, 0));
        } catch (InvalidPathException e) {
            throw new SyntaxErrorException("chat.shcmbackupreborn.backup.usage.comment");
        }
    }
}
