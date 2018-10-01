package shcm.shsupercm.forge.shcmbackupreborn.client;

import net.minecraft.client.Minecraft;
import shcm.shsupercm.forge.shcmbackupreborn.SHCMBackupReborn;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shcm.shsupercm.forge.shcmbackupreborn.client.gui.GuiBackups;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.minecraftforge.fml.relauncher.ReflectionHelper.findField;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class InjectorGuiWorldSelection {
    private static GuiButton button;

    public static Injectors.IGuiInjector guiInjector;

    @SubscribeEvent
    public static void inject(GuiScreenEvent.InitGuiEvent.Post event) throws ClassNotFoundException {
        button = null;
        guiInjector.inject(event);
    }
    @SubscribeEvent
    public static void render(GuiScreenEvent.DrawScreenEvent.Pre event) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException {
        guiInjector.render(event);
    }
    @SubscribeEvent
    public static void click(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalAccessException, InvocationTargetException {
        guiInjector.click(event);
    }


    public static class Injectors {
        public static IGuiInjector getSuggestedGuiInjector() {
            if(Loader.isModLoaded("openterraingenerator")) return new InjectorOpenTerrainGenerator();

            return new InjectorVanilla();
        }

        public interface IGuiInjector {
            void inject(GuiScreenEvent.InitGuiEvent.Post event);
            void render(GuiScreenEvent.DrawScreenEvent.Pre event) throws IllegalAccessException, InvocationTargetException;
            void click(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalAccessException, InvocationTargetException;
        }

        public static class InjectorVanilla implements IGuiInjector {
            private Field GuiWorldSelection_selectionList;
            private Field GuiListWorldSelectionEntry_worldSummary;

            InjectorVanilla() {
                try {
                    GuiWorldSelection_selectionList = findField(GuiWorldSelection.class, "selectionList", "field_184866_u");
                    GuiListWorldSelectionEntry_worldSummary = findField(GuiListWorldSelectionEntry.class, "worldSummary", "field_186786_g");
                } catch (ReflectionHelper.UnableToFindFieldException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void inject(GuiScreenEvent.InitGuiEvent.Post event) {
                if (event.getGui() instanceof GuiWorldSelection)
                    event.getButtonList().add(button = new GuiButton(97001, 5, 5, 50, 20, I18n.format("gui.shcmbackupreborn.backups")));
            }

            @Override
            public void render(GuiScreenEvent.DrawScreenEvent.Pre event) throws IllegalAccessException {
                if(event.getGui() instanceof GuiWorldSelection)
                    button.enabled = ((GuiListWorldSelection)GuiWorldSelection_selectionList.get(event.getGui())).getSelectedWorld() != null;
            }

            @Override
            public void click(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalAccessException {
                if(event.getGui() instanceof GuiWorldSelection && event.getButton().id == button.id) {
                    GuiListWorldSelection list = (GuiListWorldSelection) GuiWorldSelection_selectionList.get(event.getGui());
                    if(list.getSelectedWorld() != null) {
                        WorldSummary worldSummary = (WorldSummary)GuiListWorldSelectionEntry_worldSummary.get(list.getSelectedWorld());
                        Minecraft.getMinecraft().displayGuiScreen(new GuiBackups(worldSummary,Minecraft.getMinecraft().currentScreen));
                    }
                }
            }
        }

        public static class InjectorOpenTerrainGenerator implements IGuiInjector {
            private Class OTGGuiWorldSelection;
            private Field OTGGuiWorldSelection_selectionList;
            private Method OTGGuiListWorldSelection_getSelectedWorld;
            private Field OTGGuiListWorldSelectionEntry_worldSummary;

            InjectorOpenTerrainGenerator() {
                try {
                    OTGGuiWorldSelection = Class.forName("com.pg85.otg.forge.gui.OTGGuiWorldSelection");
                    (OTGGuiWorldSelection_selectionList = OTGGuiWorldSelection.getDeclaredField("selectionList")).setAccessible(true);
                    OTGGuiListWorldSelection_getSelectedWorld = Class.forName("com.pg85.otg.forge.gui.OTGGuiListWorldSelection").getDeclaredMethod("getSelectedWorld");
                    (OTGGuiListWorldSelectionEntry_worldSummary = Class.forName("com.pg85.otg.forge.gui.OTGGuiListWorldSelectionEntry").getDeclaredField("worldSummary")).setAccessible(true);
                } catch (ClassNotFoundException|NoSuchFieldException|NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void inject(GuiScreenEvent.InitGuiEvent.Post event) {
                if(OTGGuiWorldSelection.equals(event.getGui().getClass()))
                    event.getButtonList().add(button = new GuiButton(97001, 5, 5, 50, 20, I18n.format("gui.shcmbackupreborn.backups")));
            }

            @Override
            public void render(GuiScreenEvent.DrawScreenEvent.Pre event) throws IllegalAccessException, InvocationTargetException {
                if(OTGGuiWorldSelection.equals(event.getGui().getClass()))
                    button.enabled = OTGGuiListWorldSelection_getSelectedWorld.invoke(OTGGuiWorldSelection_selectionList.get(event.getGui())) != null;
            }

            @Override
            public void click(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalAccessException, InvocationTargetException {
                if(OTGGuiWorldSelection.equals(event.getGui().getClass()) && event.getButton().id == button.id) {
                    Object selectedWorld = OTGGuiListWorldSelection_getSelectedWorld.invoke(OTGGuiWorldSelection_selectionList.get(event.getGui()));
                    if(selectedWorld != null) {
                        WorldSummary worldSummary = (WorldSummary) OTGGuiListWorldSelectionEntry_worldSummary.get(selectedWorld);
                        Minecraft.getMinecraft().displayGuiScreen(new GuiBackups(worldSummary,Minecraft.getMinecraft().currentScreen));
                    }
                }
            }
        }

    }
}
