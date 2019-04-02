package com.envyclient.core.impl.managers;

import com.envyclient.core.api.friend.Friend;
import com.envyclient.core.api.manager.Manager;
import com.envyclient.core.impl.files.FriendsFile;
import net.minecraft.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import static com.envyclient.core.Envy.Managers.FILE;

public class FriendManager extends Manager<Friend> {

    public void addFriend(String name, String alias) {
        add(new Friend(name, alias));
    }

    public void deleteFriend(String name) {
        Friend friend = getFriend(name);
        if (friend != null) {
            remove(friend);
        }
    }

    public boolean isFriend(String name) {
        return getFriend(name) != null;
    }

    @Nullable
    public Friend getFriend(String name) {
        String strippedName = StringUtils.stripControlCodes(name);
        return getContents().stream()
                .filter(friend -> friend.getName().equalsIgnoreCase(strippedName) || friend.getAlias().equalsIgnoreCase(strippedName))
                .findFirst().orElse(null);
    }

    @Override
    public void add(Friend e) {
        super.add(e);
        saveFile();
    }

    @Override
    public void remove(Friend e) {
        super.remove(e);
        saveFile();
    }

    @Override
    public void clear() {
        super.clear();
        saveFile();
    }

    private void saveFile() {
        try {
            FILE.getFile(FriendsFile.class).saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
