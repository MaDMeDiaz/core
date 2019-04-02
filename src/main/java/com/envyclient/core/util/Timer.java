package com.envyclient.core.util;

public class Timer {

    private long lastMS = getCurrentMS();

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(long milliseconds) {
        if (getCurrentMS() - lastMS >= milliseconds) {
            reset();
            return true;
        }
        return false;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }
}