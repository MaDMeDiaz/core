package com.envyclient.core.impl.managers;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.api.manager.Manager;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.impl.components.clickgui.ComponentPanel;
import com.envyclient.core.impl.components.clickgui.console.ComponentBody;
import com.envyclient.core.impl.components.clickgui.console.ComponentTitle;
import com.envyclient.core.impl.files.ClickGUIFile;
import com.envyclient.core.impl.guiscreen.ClickGUI;
import com.envyclient.core.util.Container;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.envyclient.core.Envy.Managers.FILE;

public class ClickGUIManager extends Manager<Void> {

    private ClickGUI clickGUI;
    private Console console;

    @Override
    public void enable() {
        clickGUI = new ClickGUI();

        // adding the console
        console = new Console(clickGUI);

        double x = 10;
        int width = 110;

        // adding all the panels(category's) to the component list
        for (Category category : Category.values()) {
            clickGUI.getComponentList().add(
                    new ComponentPanel(category, x, 10, width)
            );
            x += width + 10;
        }
    }

    @Override
    public void disable() {
        try {
            FILE.getFile(ClickGUIFile.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }

    public Console getConsole() {
        return console;
    }

    @Nullable
    public GuiComponent getGuiComponent(String name) {
        return getClickGUI().getComponentList().stream()
                .filter(guiComponent -> guiComponent.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public static class Console extends Container<String> {

        private ComponentBody body;

        public Console(ClickGUI clickGUI) {

            // console
            int width = 280;

            ComponentTitle title = new ComponentTitle("Console", 10 + 25 + 5, width, 25);
            body = (ComponentBody) title.getChildren().get(0);
            clickGUI.getComponentList().add(title);
        }

        public void addReply(String text, boolean isReply) {
            getContents().add((!isReply ? "-" : ChatFormatting.GRAY + "  ") + text);
            if (body.canFit()) {
                body.setScrollIndex(body.getScrollIndex() + 10);
            }
        }

        public ComponentBody getBody() {
            return body;
        }
    }

}