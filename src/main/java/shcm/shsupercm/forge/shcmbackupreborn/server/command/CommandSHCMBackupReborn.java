package shcm.shsupercm.forge.shcmbackupreborn.server.command;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import shcm.shsupercm.forge.shcmbackupreborn.common.storage.WorldProfile;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import shcm.shsupercm.forge.shcmbackupreborn.server.RestoreHandler;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandSHCMBackupReborn extends CommandBase {
    private static String restoreConfirm = null;

    @Override
    public String getName() {
        return "shcmbackupreborn";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("backups","bks");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "chat.shcmbackupreborn.shcmbackupreborn.usage";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length >= 1 && !args[0].isEmpty())
            switch(args[0]) {
                case "restore":
                    if (args.length == 2)
                        return Arrays.asList("list");
                    break;
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
        else return Arrays.asList("restore", "auto_backup_interval", "trim");
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        assert WorldProfile.currentWorldProfile != null;
        if(args.length >= 1)
            switch(args[0]) {
                case "restore":
                    if(args.length >= 2) {
                        if(args[1].equals("list")) {
                            sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.restore.list"));
                            TextComponentString output = new TextComponentString("");
                            TextComponentString backupComponent = null;
                            for(String backup : RestoreHandler.getBackupsList(WorldProfile.currentWorldProfile.file.getParentFile().getParentFile())) {
                                if(backupComponent != null) {
                                    output.appendText(", ");
                                }
                                backupComponent = new TextComponentString("[" + backup + "]");
                                backupComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/shcmbackupreborn restore " + backup));
                                backupComponent.getStyle().setColor(TextFormatting.YELLOW);
                                output.appendSibling(backupComponent);
                            }
                            output.appendText(".");
                            sender.sendMessage(output);
                            return;
                        } else {
                            String backup = buildString(args,1);
                            if(new File(WorldProfile.currentWorldProfile.file.getParentFile(),backup).exists()) {
                                if(backup.equals(restoreConfirm)) {
                                    sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.restore.restoring",restoreConfirm));
                                    try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                                    RestoreHandler.tryRestore(true,WorldProfile.currentWorldProfile.file.getParentFile().getParentFile(),restoreConfirm);
                                    return;
                                } else {
                                    restoreConfirm = backup;
                                    sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.restore.confirm", backup));
                                    return;
                                }
                            } else {
                                sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.restore.nobackup",backup));
                                return;
                            }
                        }
                    } else {
                        throw new WrongUsageException(this.getUsage(sender));
                    }
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
                                    if(WorldProfile.TrimBehavior.valueNames.contains(args[2])) {
                                        WorldProfile.currentWorldProfile.trimBehavior = WorldProfile.TrimBehavior.valueOf(args[2]);
                                        WorldProfile.currentWorldProfile.writeFile();
                                        sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.set", args[0] + "_" + args[1], WorldProfile.currentWorldProfile.trimBehavior.name()));
                                        return;
                                    } else {
                                        sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.behavior.nobehavior",args[2]));
                                        return;
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.is",args[0] + "_" + args[1], WorldProfile.currentWorldProfile.trimBehavior.name()));
                                    return;
                                }
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
        text = new TextComponentTranslation("chat.shcmbackupreborn.shcmbackupreborn.usage3",String.join("|", WorldProfile.TrimBehavior.valueNames));
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
