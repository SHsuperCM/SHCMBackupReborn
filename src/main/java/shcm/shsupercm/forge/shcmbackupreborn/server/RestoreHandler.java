package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.io.File;

public class RestoreHandler {
    public static boolean tryRestore(Side physicalSide, boolean running, File worldDirectory, String backup) {
        if (running) {
            if (physicalSide.isServer()) {

            } else {
                
                return tryRestore(physicalSide,false,worldDirectory,backup);
            }
        } else {
            if (physicalSide.isServer()) {

            } else {

            }
        }
        return false;
    }
}