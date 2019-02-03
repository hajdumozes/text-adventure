package items;

import attributes.ArmorClass;

public class Shield extends Weapon {
    private ArmorClass armorClass;

    public Shield() {
        super("Shield", 0, 1, false);
        this.armorClass = new ArmorClass(2);
    }

    public ArmorClass getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(ArmorClass armorClass) {
        this.armorClass = armorClass;
    }
}
