package shcm.shsupercm.forge.shcmbackupreborn.client;

import shcm.shsupercm.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLPostInitializationEvent event) {
        super.init(event);
        InjectorGuiWorldSelection.guiInjector = InjectorGuiWorldSelection.Injectors.getSuggestedGuiInjector();
    }
}
