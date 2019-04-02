package com.envyclient.core.impl.modules;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.module.data.Data;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.impl.events.MiddleClickEvent;
import me.ihaq.eventmanager.listener.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import static com.envyclient.core.Envy.Managers.FRIEND;

@Data(name = "MCF", category = Category.WORLD, description = "Middle-click on players to add/remove them as friends.")
public class MiddleClickFriend extends Module {

    @EventTarget
    public void onMiddleClick(MiddleClickEvent e) {

        if (mc.objectMouseOver.typeOfHit == null || mc.objectMouseOver.entityHit == null) {
            return;
        }

        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityPlayer) {

            String name = mc.objectMouseOver.entityHit.getName();

            if (FRIEND.isFriend(name)) {
                FRIEND.deleteFriend(name);
                sendMessage("Removed '" + name + "'.");
            } else {
                FRIEND.addFriend(name, name);
                sendMessage("Added '" + name + "'.");
            }
        }
    }
}
