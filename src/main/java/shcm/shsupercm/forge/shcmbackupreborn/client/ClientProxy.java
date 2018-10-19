package shcm.shsupercm.forge.shcmbackupreborn.client;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shcm.shsupercm.forge.shcmbackupreborn.Config;
import shcm.shsupercm.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shcm.shsupercm.forge.shcmbackupreborn.server.RestoreHandler;

import java.io.File;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLPostInitializationEvent event) {
        super.init(event);
        InjectorGuiWorldSelection.guiInjector = InjectorGuiWorldSelection.Injectors.getSuggestedGuiInjector();
    }

    @SubscribeEvent
    public static void chatReceived(ClientChatReceivedEvent event) throws ClassCastException {
        TextComponentTranslation message = (TextComponentTranslation) event.getMessage();
        if(
                (!Config.chatBackups && message.getKey().equals("chat.shcmbackupreborn.backup.startbackup")) ||
                (!Config.chatBackups && message.getKey().equals("chat.shcmbackupreborn.backup.endbackup")) ||
                (!Config.chatTrims &&   message.getKey().equals("chat.shcmbackupreborn.backup.starttrim")))
            event.setCanceled(true);
    }
}
