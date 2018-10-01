package SHCM.SHsuperCM.forge.shcmbackupreborn.client;

import SHCM.SHsuperCM.forge.shcmbackupreborn.client.gui.GuiWorldSelectionInjector;
import SHCM.SHsuperCM.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLPostInitializationEvent event) {
        super.init(event);
        GuiWorldSelectionInjector.guiInjector = GuiWorldSelectionInjector.Injectors.getSuggestedGuiInjector();
    }
}
