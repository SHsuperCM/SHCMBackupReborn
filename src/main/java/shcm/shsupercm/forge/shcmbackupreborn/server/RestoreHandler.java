package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

public class RestoreHandler {
    public static boolean tryRestore(Side physicalSide, boolean running, File worldDirectory, String backup) {
        if (running) {
            if (physicalSide.isServer()) {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                for(EntityPlayerMP player : playerList.getPlayers())
                    player.connection.disconnect(new TextComponentTranslation("misc"));
            } else { // isClient()
                Minecraft.getMinecraft().loadWorld(null);
                return tryRestore(physicalSide,false,worldDirectory,backup);
            }
        } else { // !running
            if (physicalSide.isServer()) {

            } else { // isClient()

            }
        }
        return false;
    }
}