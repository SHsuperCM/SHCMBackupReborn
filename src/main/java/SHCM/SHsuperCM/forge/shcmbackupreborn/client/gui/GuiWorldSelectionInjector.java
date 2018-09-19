package SHCM.SHsuperCM.forge.shcmbackupreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class GuiWorldSelectionInjector {
    private static GuiButton button;

    @SubscribeEvent
    public static void inject(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.getGui() instanceof GuiWorldSelection) {
            event.getButtonList().add(button = new GuiButton(97001,5,5,50,20, I18n.format("gui.shcmbackupreborn.backups")));
        }
    }

    @SubscribeEvent
    public static void render(GuiScreenEvent.DrawScreenEvent.Pre event) throws IllegalAccessException {
        if(event.getGui() instanceof GuiWorldSelection)
            button.enabled = ((GuiListWorldSelection)GuiWorldSelection_selectionList.get(event.getGui())).getSelectedWorld() != null;
    }

    @SubscribeEvent
    public static void click(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalAccessException {
        if(event.getGui() instanceof GuiWorldSelection && event.getButton().id == button.id) {
            GuiListWorldSelection list = (GuiListWorldSelection) GuiWorldSelection_selectionList.get(event.getGui());
            if(list.getSelectedWorld() != null)
                System.out.println(((WorldSummary)GuiListWorldSelectionEntry_worldSummary.get(list.getSelectedWorld())).getFileName());
        }
    }


    private static Field GuiWorldSelection_selectionList = ReflectionHelper.findField(GuiWorldSelection.class,"selectionList", "field_184866_u");
    private static Field GuiListWorldSelectionEntry_worldSummary = ReflectionHelper.findField(GuiListWorldSelectionEntry.class,"worldSummary", "field_186786_g");
}
