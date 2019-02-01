package statuses;

public class Status {
    private int value;
    private int duration;

    public Status(int value, int duration) {
        this.value = value;
        this.duration = duration;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
