package com.envyclient.core.impl.files;

import com.envyclient.core.api.file.CustomFile;
import com.envyclient.core.api.friend.Friend;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.envyclient.core.Envy.Managers.FRIEND;

public class FriendsFile extends CustomFile {

    public FriendsFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() throws IOException {

        FileReader inFile = new FileReader(getFile());

        List<Friend> friends = getGson().fromJson(inFile, new TypeToken<List<Friend>>() {
        }.getType());

        inFile.close();

        if (friends != null) {
            FRIEND.setContents(friends);
        }
    }

    @Override
    public void saveFile() throws IOException {
        FileWriter printWriter = new FileWriter(getFile());
        printWriter.write(getGson().toJson(FRIEND.getContents()));
        printWriter.close();
    }
}
