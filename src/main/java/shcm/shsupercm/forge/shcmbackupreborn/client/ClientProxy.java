package shcm.shsupercm.forge.shcmbackupreborn.client;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shcm.shsupercm.forge.shcmbackupreborn.Config;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;
import shcm.shsupercm.forge.shcmbackupreborn.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override public Side getSide() { return Side.CLIENT; }

    @Override
    public void init(FMLPostInitializationEvent event) {
        super.init(event);
        InjectorGuiWorldSelection.guiInjector = InjectorGuiWorldSelection.Injectors.getSuggestedGuiInjector();
    }

    @SubscribeEvent
    public static void chatReceived(ClientChatReceivedEvent event) {
        if(event.getMessage() instanceof TextComponentTranslation) {
            TextComponentTranslation message = (TextComponentTranslation) event.getMessage();
            if (
                    (!Config.chatBackups && message.getKey().equals("chat.shcmbackupreborn.backup.startbackup")) ||
                            (!Config.chatBackups && message.getKey().equals("chat.shcmbackupreborn.backup.endbackup")) ||
                            (!Config.chatTrims && message.getKey().equals("chat.shcmbackupreborn.backup.starttrim")))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void guiShown(GuiOpenEvent event) {
        SHCMBackupReborn.logger.info(event.getGui());
    }
}
