package com.envyclient.core.impl.files;

import com.envyclient.core.api.file.CustomFile;
import com.envyclient.core.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModulesFile extends CustomFile {

    public ModulesFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() throws IOException {
        FileReader reader = new FileReader(getFile());
        GsonUtils.loadModuleData(getGson().fromJson(reader, JsonArray.class));
        reader.close();
    }

    @Override
    public void saveFile() throws IOException {
        FileWriter writer = new FileWriter(getFile());
        writer.write(getGson().toJson(GsonUtils.getModuleData(true)));
        writer.close();
    }
}
