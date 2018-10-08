package shcm.shsupercm.forge.shcmbackupreborn.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy {
    public void init(FMLPreInitializationEvent event) {

    }
    public void init(FMLInitializationEvent event) {

    }
    public void init(FMLPostInitializationEvent event) {

    }

    public boolean tryRestore(File worldDirectory, String backup, boolean running) {
        return false;
    }
}
