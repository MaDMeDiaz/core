package com.envyclient.core.impl.files;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.api.file.CustomFile;
import com.envyclient.core.impl.components.clickgui.ComponentPanel;
import com.envyclient.core.impl.components.clickgui.console.ComponentTitle;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.envyclient.core.Envy.Managers.CLICK_GUI;

public class ClickGUIFile extends CustomFile {

    // static strings for saving
    private static final String NAME = "name";
    private static final String OPEN = "open";
    private static final String X = "x";
    private static final String Y = "y";

    public ClickGUIFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() throws IOException {
        FileReader fileReader = new FileReader(getFile());
        JsonArray componentsArray = getGson().fromJson(fileReader, JsonArray.class);
        fileReader.close();

        if (componentsArray == null) {
            return;
        }

        componentsArray.forEach(jsonElement -> {
            JsonObject componentObject = jsonElement.getAsJsonObject();

            if (!componentObject.has(NAME)
                    || !componentObject.has(OPEN)
                    || !componentObject.has(X)
                    || !componentObject.has(Y)) {
                return;
            }

            GuiComponent guiComponent = CLICK_GUI.getGuiComponent(componentObject.get(NAME).getAsString());

            if (guiComponent == null) {
                return;
            }

            guiComponent.setX(componentObject.get(X).getAsInt());
            guiComponent.setY(componentObject.get(Y).getAsInt());

            boolean open = componentObject.get(OPEN).getAsBoolean();
            if (guiComponent instanceof ComponentPanel) { // clickgui panel check
                ((ComponentPanel) guiComponent).setOpen(open);
            } else if (guiComponent instanceof ComponentTitle) { // console check
                ((ComponentTitle) guiComponent).setOpen(open);
            }

        });
    }

    @Override
    public void saveFile() throws IOException {
        JsonArray componentsArray = new JsonArray();

        CLICK_GUI.getClickGUI().getComponentList().forEach(component -> {
            JsonObject componentObject = new JsonObject();

            componentObject.addProperty(NAME, component.getName());
            componentObject.addProperty(X, component.getX());
            componentObject.addProperty(Y, component.getY());

            if (component instanceof ComponentPanel) { // clickgui panel check
                componentObject.addProperty(OPEN, ((ComponentPanel) component).isOpen());
            } else if (component instanceof ComponentTitle) { // console check
                componentObject.addProperty(OPEN, ((ComponentTitle) component).isOpen());
            }

            componentsArray.add(componentObject);
        });

        FileWriter printWriter = new FileWriter(getFile());
        printWriter.write(getGson().toJson(componentsArray));
        printWriter.close();
    }
}
