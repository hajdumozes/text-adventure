package attributes;


public abstract class Attribute implements Comparable<Attribute> {
    private String name;
    private int value;

    public Attribute(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Attribute attribute) {
        return value - attribute.getValue();
    }

    public void increase(int amount) {
        value += amount;
    }

    public void decrease(int amount) {
        value -= amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
