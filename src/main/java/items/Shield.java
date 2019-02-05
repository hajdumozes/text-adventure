package items;

import attributes.ArmorClass;

import java.text.MessageFormat;

public class Shield extends Weapon {
    private ArmorClass armorClass;

    public Shield() {
        super("Shield", 0, 1, false);
        this.armorClass = new ArmorClass(2);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} AC:{1}", getName(), armorClass.getCurrentValue());
    }

    public ArmorClass getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(ArmorClass armorClass) {
        this.armorClass = armorClass;
    }
}
