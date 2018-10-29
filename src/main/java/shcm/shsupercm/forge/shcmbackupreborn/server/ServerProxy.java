package shcm.shsupercm.forge.shcmbackupreborn.server;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shcm.shsupercm.forge.shcmbackupreborn.common.CommonProxy;

import java.io.File;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {
    @Override public Side getSide() { return Side.SERVER; }
}
