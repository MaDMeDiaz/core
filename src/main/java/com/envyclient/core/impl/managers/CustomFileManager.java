package com.envyclient.core.impl.managers;

import com.envyclient.core.api.file.CustomFile;
import com.envyclient.core.api.manager.Manager;
import com.envyclient.core.impl.files.ClickGUIFile;
import com.envyclient.core.impl.files.FriendsFile;
import com.envyclient.core.impl.files.ModulesFile;
import com.envyclient.core.util.GsonUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import static com.envyclient.core.Envy.Info.NAME;

public class CustomFileManager extends Manager<CustomFile> {

    @Override
    public void enable() {

        File directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + NAME);

        // Making the directory
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Registering the files
        add(new FriendsFile(GsonUtils.gson, new File(directory, "Friends.json")));
        add(new ModulesFile(GsonUtils.gson, new File(directory, "Modules.json")));
        add(new ClickGUIFile(GsonUtils.gson, new File(directory, "ClickGUI.json")));

        // Loading the files
        getContents().forEach(file -> {
            try {
                file.loadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void disable() {
        getContents().forEach(file -> {
            try {
                file.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Nullable
    public CustomFile getFile(Class<? extends CustomFile> clazz) {
        return getContents().stream()
                .filter(customFile -> customFile.getClass() == clazz)
                .findFirst().orElse(null);
    }

}