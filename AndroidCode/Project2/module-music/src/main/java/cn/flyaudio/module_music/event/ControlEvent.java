package cn.flyaudio.module_music.event;

public class ControlEvent {

    // 播放暂停
    public static final int CONTROL_PLAY = 1;
    // 下一曲
    public static final int CONTROL_NEXT = 2;
    // 上一曲
    public static final int CONTROL_PREV = 3;
    // 列表循环
    public static final int CONTROL_LIST_LOOP = 4;
    // 单曲循环
    public static final int CONTROL_SINGLE_LOOP = 5;
    // 随机播放
    public static final int CONTROL_SHUFFLE_LOOP = 6;

    int controlType = -1;

    public ControlEvent(int controlType) {
        this.controlType = controlType;
    }

    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }
}

