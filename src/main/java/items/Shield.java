package items;

import attributes.ArmorClass;

import java.text.MessageFormat;

public class Shield extends Item implements Wieldable {
    private ArmorClass armorClass;

    public Shield() {
        super("Shield");
        this.armorClass = new ArmorClass(2);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} +{1}AC", getName(), armorClass.getCurrentValue());
    }

    public ArmorClass getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(ArmorClass armorClass) {
        this.armorClass = armorClass;
    }
}
