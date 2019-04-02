package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;

import java.io.File;

public class ScreenshotEvent extends Event {

    private File screenShot;

    public ScreenshotEvent(File screenShot) {
        this.screenShot = screenShot;
    }

    public File getScreenShot() {
        return screenShot;
    }
}
