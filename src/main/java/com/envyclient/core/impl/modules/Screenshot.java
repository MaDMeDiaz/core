package com.envyclient.core.impl.modules;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.module.data.Data;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.impl.events.ScreenshotEvent;
import com.google.gson.JsonObject;
import me.ihaq.eventmanager.listener.EventTarget;
import me.ihaq.imguruploader.util.Callback;

import static com.envyclient.core.Envy.ThirdParty.IMGUR;

@Data(name = "Screenshot", category = Category.PLAYER, description = "Uploads your screenshot to the web.")
public class Screenshot extends Module {

    @EventTarget
    public void onScreenshot(ScreenshotEvent e) {
        mc.thePlayer.sendMessage("Uploading...");
        IMGUR.uploadAsync(new Callback() {
            @Override
            public void onSuccess(JsonObject e) {
                mc.thePlayer.sendLink(e.get("link").getAsString());
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                mc.thePlayer.sendMessage("An error occurred.");
            }
        }, e.getScreenShot());
    }
}