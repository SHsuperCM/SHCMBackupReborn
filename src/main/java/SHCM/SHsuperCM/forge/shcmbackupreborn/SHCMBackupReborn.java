package SHCM.SHsuperCM.forge.shcmbackupreborn;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.CommonProxy;

@Mod(modid = SHCMBackupReborn.MODID)
public class SHCMBackupReborn {
    public static final String MODID = "shcmbackupreborn";

    @SidedProxy(clientSide = "SHCM.SHsuperCM.forge." + MODID + ".client.ClientProxy", serverSide = "SHCM.SHsuperCM.forge." + MODID + ".server.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static SHCMBackupReborn INSTANCE;
}
