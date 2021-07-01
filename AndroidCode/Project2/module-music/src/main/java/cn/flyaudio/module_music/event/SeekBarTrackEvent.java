package cn.flyaudio.module_music.event;

public class SeekBarTrackEvent {
    public static final int EVENT_START = 1;
    public static final int EVENT_STOP = 2;


    private int currentProgress;
    private int maxProgress;

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public SeekBarTrackEvent(int event) {
        this.event = event;
    }

    int event;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}


