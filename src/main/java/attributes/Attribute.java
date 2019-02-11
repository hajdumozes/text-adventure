package attributes;


import java.util.Objects;

public abstract class Attribute implements Comparable<Attribute> {
    private String name;
    private int currentValue;

    public Attribute(String name) {
        this(name, 0);
    }

    public Attribute(String name, int value) {
        this.name = name;
        this.currentValue = value;
    }

    @Override
    public int compareTo(Attribute attribute) {
        return attribute.getCurrentValue() - currentValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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
