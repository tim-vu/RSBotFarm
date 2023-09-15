package net.rlbot.api.script.randoms;

public abstract class RandomEventHandler {

    public abstract boolean shouldActivate();

    public abstract int loop();

    public boolean shouldStopScript() {
        return stop;
    }

    protected void stopScript() {
        this.stop = true;
    }

    private boolean stop;

}
