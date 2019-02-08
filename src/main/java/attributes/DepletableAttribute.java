package attributes;

public abstract class DepletableAttribute extends Attribute {
    private int maxValue;

    public DepletableAttribute(String name, int value, int max) {
        super(name, value);
        this.maxValue = max;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
