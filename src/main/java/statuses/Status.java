package statuses;

import attributes.Attribute;

public class Status {
    private Attribute attribute;
    private int value;
    private int duration;

    public Status(Attribute attribute, int value, int duration) {
        this.attribute = attribute;
        this.value = value;
        this.duration = duration;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
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
