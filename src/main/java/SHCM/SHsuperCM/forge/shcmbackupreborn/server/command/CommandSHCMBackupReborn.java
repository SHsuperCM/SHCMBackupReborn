package SHCM.SHsuperCM.forge.shcmbackupreborn.server.command;

import SHCM.SHsuperCM.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSHCMBackupReborn extends CommandBase {
    @Override
    public String getName() {
        return "shcmbackupreborn";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("backups");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "chat.shcmbackupreborn.shcmbackupreborn.usage";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length >= 1 && !args[0].isEmpty())
            switch(args[0]) {
                case "auto_backup_interval":
                    if (args.length == 2)
                        return Arrays.asList("15", "30", "60", "120", "300", "600");
                    break;
                case "trim":
                    if (args.length == 2)
                        return Arrays.asList("behavior", "max_backups");
                    if (args.length == 3)
                        switch (args[1]) {
                            case "behavior":
                                return Arrays.asList("delete_excess", "archive_on_threshold");
                            case "max_backups":
                                return Arrays.asList("10", "20", "30", "40", "50");
                        }
            }
        else return Arrays.asList("auto_backup_interval", "trim");
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        assert WorldProfile.currentWorldProfile != null;
        if(args.length >= 1)
            switch(args[0]) {
                case "auto_backup_interval":
                    if(args.length >= 2) {
                        try {
                            int minutes = parseInt(args[1], 0);
                            WorldProfile.currentWorldProfile.autoBackupInterval = minutes * 60 * 1000;
                            WorldProfile.currentWorldProfile.writeFile();
                            sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.set", args[0], minutes + "(minutes)"));
                            return;
                        } catch (NumberInvalidException e) {
                            throw new WrongUsageException(this.getUsage(sender));
                        }
                    } else {
                        sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.is",args[0], WorldProfile.currentWorldProfile.autoBackupInterval/1000/60 + "(minutes)"));
                        return;
                    }
                case "trim":
                    if(args.length >= 2)
                        switch(args[1]) {
                            case "behavior":
                                if (args.length >= 3) {
                                    if ((args[2].equals("delete_excess") || args[2].equals("archive_on_threshold"))) {
                                        WorldProfile.currentWorldProfile.trimBehavior = args[2];
                                        WorldProfile.currentWorldProfile.writeFile();
                                        sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.set", args[0] + "_" + args[1], WorldProfile.currentWorldProfile.trimBehavior));
                                        return;
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.is",args[0] + "_" + args[1], WorldProfile.currentWorldProfile.trimBehavior));
                                    return;
                                }
                                throw new WrongUsageException(this.getUsage(sender));
                            case "max_backups":
                                if(args.length >= 3) {
                                    try {
                                        WorldProfile.currentWorldProfile.trimMaxBackups = parseInt(args[2], 0);
                                        WorldProfile.currentWorldProfile.writeFile();
                                        sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.set", args[0] + "_" + args[1], WorldProfile.currentWorldProfile.trimMaxBackups));
                                        return;
                                    } catch (NumberInvalidException e) {
                                        throw new WrongUsageException(this.getUsage(sender));
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.is",args[0] + "_" + args[1], WorldProfile.currentWorldProfile.trimMaxBackups));
                                    return;
                                }
                        }
                default:
                    throw new WrongUsageException(this.getUsage(sender));
            }

        help(sender);
    }

    private void help(ICommandSender sender) {
        TextComponentBase text;


        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage0");
        text.getStyle().setBold(true);
        sender.sendMessage(text);
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage1");
        text.getStyle().setColor(TextFormatting.GOLD);
        sender.sendMessage(text);
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage2");
        sender.sendMessage(text);
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage3");
        text.getStyle().setColor(TextFormatting.GOLD);
        sender.sendMessage(text);
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage4");
        sender.sendMessage(text);
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage5");
        text.getStyle().setColor(TextFormatting.GOLD);
        sender.sendMessage(text);
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage6");
        sender.sendMessage(text);
    }
}
