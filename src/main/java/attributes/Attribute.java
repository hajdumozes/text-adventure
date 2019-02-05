package attributes;


public abstract class Attribute implements Comparable<Attribute> {
    private String name;
    private int currentValue;

    public Attribute(String name, int value) {
        this.name = name;
        this.currentValue = value;
    }

    @Override
    public int compareTo(Attribute attribute) {
        return currentValue - attribute.getCurrentValue();
    }

    public void increase(int amount) {
        currentValue += amount;
    }

    public void decrease(int amount) {
        currentValue -= amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }
}
