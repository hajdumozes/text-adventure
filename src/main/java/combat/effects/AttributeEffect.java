package combat.effects;

import attributes.Attribute;

public class AttributeEffect extends Effect {
    private Attribute attribute;
    private int value;

    public AttributeEffect(Attribute attribute, String name, int value, int duration) {
        super(name, duration);
        this.attribute = attribute;
        this.value = value;
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
}
