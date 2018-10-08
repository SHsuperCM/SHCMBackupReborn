package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shcm.shsupercm.forge.shcmbackupreborn.common.CommonProxy;

import java.io.File;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {

    @Override
    public boolean tryRestore(File worldDirectory, String backup, boolean running) {
        return RestoreHandler.tryRestore(Side.SERVER,running,worldDirectory,backup);
    }
}
