package com.envyclient.core.impl.modules.hud.parts;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.api.setting.Setting;
import com.envyclient.core.api.setting.type.BooleanSetting;
import com.envyclient.core.api.setting.type.ClampedSetting;
import com.envyclient.core.api.setting.type.ModeSetting;
import com.envyclient.core.impl.events.KeyPressEvent;
import com.envyclient.core.impl.events.Render2DEvent;
import com.envyclient.core.impl.modules.hud.Hud;
import com.envyclient.core.util.render.FontUtils;
import com.envyclient.core.util.MathUtils;
import com.envyclient.core.util.Timer;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.eventmanager.listener.EventListener;
import me.ihaq.eventmanager.listener.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

import static com.envyclient.core.Envy.Colors.MAIN;
import static com.envyclient.core.Envy.Colors.SECONDARY;
import static com.envyclient.core.Envy.Managers.MODULE;
import static com.envyclient.core.Envy.Managers.SETTING;

public class TabGui implements EventListener {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<Category> categoryArrayList = Arrays.asList(Category.values());
    private final Timer timer = new Timer();

    private int currentCategoryIndex = 0;
    private int currentModIndex = 0;
    private int currentSettingIndex = 0;
    private int screen = -1;
    private boolean editMode = false;

    private Hud hud;

    public TabGui(Hud hud) {
        this.hud = hud;
    }

    @EventTarget
    public void onRender(Render2DEvent e) {

        if (!renderTabGUI()) {
            return;
        }

        // reset the tabgui after 5 seconds of not being used
        if (timer.hasReached(5000) && screen == 0) {
            screen = -1;
        }

        // rendering the logo
        if (screen == -1) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            mc.getTextureManager().bindTexture(new ResourceLocation("textures/custom/logo.png"));
            Gui.drawModalRectWithCustomSizedTexture(-12, 0, 0, 0, 85, 85, 85, 85);

            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

        int startX = 3;
        int startY = 3;

        // rendering the categories first char in a row
        for (Category c : categoryArrayList) {
            Gui.drawRect(startX, startY, startX + 15, startY + 15, SECONDARY);

            // rendering a green background on the current category
            if (getCurrentCategory() == c) {
                Gui.drawRect(startX + 1, startY + 1, startX + 14, startY + 14, MAIN);
            }

            FontUtils.drawCenteredStringWithShadow(c.name().substring(0, 1), startX + 8, startY + 4, -1);
            startX += 16;
        }

        // modules screen
        if (screen == 1 || screen == 2) {
            int startXMods = 3;
            int startYMods = 3 + 17;

            Gui.drawRect(startXMods, startYMods, startXMods + getWidestMod(), startYMods + (getModsForCurrentCategory().size() * 12), SECONDARY);
            Gui.drawRect(startXMods + (currentCategoryIndex * 16), startY + 15, startXMods + (currentCategoryIndex * 16) + 15, startYMods, SECONDARY);

            for (Module m : getModsForCurrentCategory()) {

                if (getCurrentModule() == m) {
                    Gui.drawRect(startXMods, startYMods, startXMods + FontUtils.getStringWidth(m.getName()) + 4, startYMods + 12, MAIN);
                }

                int x = startXMods + getWidestMod() - 7;
                int y = startYMods + 3 + 1;

                Gui.drawRect(x, y, x + 5, y + 5, -16777216);

                if (m.isToggled()) {
                    Gui.drawRect(x + 1, y + 1, x + 4, y + 4, MAIN);
                }

                FontUtils.drawStringWithShadow(m.getName(), startXMods + 2, startYMods + 1 + 1, -1);
                startYMods += 12;
            }
        }


        // module settings screen
        if (screen == 2) {
            int startXSettings = 3 + (getWidestMod() - 7) + 9;
            int startYSettings = 3 + 17 + (currentModIndex * 12);
            Gui.drawRect(startXSettings, startYSettings, startXSettings + getWidestSetting() + 2, startYSettings + (getSettingsForCurrentModule().size() * 12) - 2, SECONDARY);

            for (Setting s : getSettingsForCurrentModule()) {

                // checking if current setting is the selected setting
                if (getCurrentSetting() == s) {
                    Gui.drawRect(startXSettings, startYSettings, startXSettings + FontUtils.getStringWidth(s.getName() + ": "), startYSettings + 9 + 2 - 1, MAIN);
                }

                if (s instanceof ClampedSetting) {
                    FontUtils.drawStringWithShadow(s.getName() + ": " + (editMode && getCurrentSetting() == s ? ChatFormatting.WHITE : ChatFormatting.GRAY) + MathUtils.roundToPlace((double) s.getValue(), 2), 1 + startXSettings, startYSettings + 1, -1);
                } else if (s instanceof BooleanSetting) {
                    FontUtils.drawStringWithShadow(s.getName() + ": " + (editMode && getCurrentSetting() == s ? ChatFormatting.WHITE : ChatFormatting.GRAY) + s.getValue(), 1 + startXSettings, startYSettings + 1, -1);
                } else {
                    FontUtils.drawStringWithShadow(s.getName() + ": " + (editMode && getCurrentSetting() == s ? ChatFormatting.WHITE : ChatFormatting.GRAY) + s.getValue(), 1 + startXSettings, startYSettings + 1, -1);
                }

                startYSettings += 12;
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onKeyPress(KeyPressEvent eventKeyboard) {

        if (!renderTabGUI()) {
            return;
        }

        switch (eventKeyboard.getKey()) {
            case Keyboard.KEY_RETURN: {
                timer.reset();
                enter();
                break;
            }
            case Keyboard.KEY_DOWN: {
                timer.reset();
                down();
                break;
            }
            case Keyboard.KEY_UP: {
                timer.reset();
                up();
                break;
            }
            case Keyboard.KEY_RIGHT: {
                timer.reset();
                right();
                break;
            }
            case Keyboard.KEY_LEFT: {
                timer.reset();
                left();
                break;
            }
        }
    }

    private void left() {
        if (screen == 0) {
            if (currentCategoryIndex > 0) {
                currentCategoryIndex--;
            } else if (currentCategoryIndex == 0) {
                currentCategoryIndex = categoryArrayList.size() - 1;
            }
        } else if (screen == 2) {
            currentSettingIndex = 0;
            editMode = false;
            screen = 1;
        }
    }

    private void right() {
        if (screen == 0) {
            if (currentCategoryIndex < (categoryArrayList.size() - 1)) {
                currentCategoryIndex++;
            } else {
                currentCategoryIndex = 0;
            }
        } else if (screen == 1) {
            if (!getSettingsForCurrentModule().isEmpty()) {
                screen = 2;
            }
        }
    }

    private void down() {
        if (editMode) {
            Setting s = this.getCurrentSetting();
            if (s instanceof ClampedSetting) {

                ClampedSetting clampedSetting = (ClampedSetting) s;
                if (clampedSetting.onlyInt()) {
                    clampedSetting.setValue(clampedSetting.getValue() - 1);
                } else {
                    clampedSetting.setValue(clampedSetting.getValue() - 0.1);
                }

                SETTING.capClampedSetting(clampedSetting);

            } else if (s instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting) s;
                try {
                    modeSetting.setValue(modeSetting.getModes().get(modeSetting.currentIndex() + 1));
                } catch (Exception e) {
                    modeSetting.setValue(modeSetting.getModes().get(0));
                }
            } else if (s instanceof BooleanSetting) {
                ((BooleanSetting) s).setValue(!((boolean) s.getValue()));
            }
        } else {
            if (screen == -1) {
                screen = 0;
            } else if (screen == 0) {
                screen = 1;
            } else if (screen == 1 && currentModIndex < (getModsForCurrentCategory().size() - 1)) {
                currentModIndex++;
            } else if (screen == 1 && currentModIndex == (getModsForCurrentCategory().size() - 1)) {
                currentModIndex = 0;
            } else if (screen == 2 && currentSettingIndex < (getSettingsForCurrentModule().size() - 1)) {
                currentSettingIndex++;
            } else if (screen == 2 && currentSettingIndex == (getSettingsForCurrentModule().size() - 1)) {
                currentSettingIndex = 0;
            }
        }
    }

    private void up() {
        if (editMode) {
            Setting s = this.getCurrentSetting();
            if (s instanceof ClampedSetting) {

                ClampedSetting clampedSetting = (ClampedSetting) s;
                if (clampedSetting.onlyInt()) {
                    clampedSetting.setValue(clampedSetting.getValue() + 1);
                } else {
                    clampedSetting.setValue(clampedSetting.getValue() + 0.1);
                }

                SETTING.capClampedSetting(clampedSetting);

            } else if (s instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting) s;
                try {
                    modeSetting.setValue(modeSetting.getModes().get(modeSetting.currentIndex() - 1));
                } catch (Exception e) {
                    modeSetting.setValue(modeSetting.getModes().get(modeSetting.getModes().size() - 1));
                }
                getCurrentModule().toggle();
                getCurrentModule().toggle();
            } else if (s instanceof BooleanSetting) {
                ((BooleanSetting) s).setValue(!((boolean) s.getValue()));
            }
        } else {
            if (screen == 0) {
                screen = -1;
            } else if (screen == 1 && currentModIndex == 0) {
                screen = 0;
            } else if (screen == 1 && currentModIndex > 0) {
                currentModIndex--;
            } else if (screen == 2 && currentSettingIndex > 0) {
                currentSettingIndex--;
            } else if (screen == 2 && currentSettingIndex == 0) {
                currentSettingIndex = getSettingsForCurrentModule().size() - 1;
            }
        }

    }

    private void enter() {
        if (screen == 1) {
            getCurrentModule().toggle();
        } else if (screen == 2) {
            editMode = !editMode;
        }
    }

    private boolean renderTabGUI() {
        return hud.tabGui && !mc.gameSettings.showDebugInfo;
    }

    private Category getCurrentCategory() {
        return categoryArrayList.get(currentCategoryIndex);
    }

    private List<Module> getModsForCurrentCategory() {
        return MODULE.getModules(getCurrentCategory());
    }

    private Module getCurrentModule() {
        return getModsForCurrentCategory().get(currentModIndex);
    }

    private List<Setting> getSettingsForCurrentModule() {
        return SETTING.getSettings(getCurrentModule());
    }

    private Setting getCurrentSetting() {
        return getSettingsForCurrentModule().get(currentSettingIndex);
    }

    private int getWidestSetting() {
        int maxWidth = 0;
        for (Setting s : getSettingsForCurrentModule()) {
            if (s instanceof ClampedSetting) {
                int width = FontUtils.getStringWidth(s.getName() + ": " + MathUtils.roundToPlace((double) s.getValue(), 2));
                if (width > maxWidth) {
                    maxWidth = width;
                }
            } else {
                int width = FontUtils.getStringWidth(s.getName() + ": " + s.getValue());
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }
        return maxWidth;
    }

    private int getWidestMod() {
        int width = (categoryArrayList.size() * 16);
        for (Module m : getModsForCurrentCategory()) {
            if (FontUtils.getStringWidth(m.getName()) + 14 > width) {
                width = FontUtils.getStringWidth(m.getName()) + 14;
            }
        }
        return width;
    }


}
