package items.Equipment;

import items.Equipment.ownable.Quiver;

public abstract class RangedWeapon extends Weapon {
    private Quiver quiver;

    public RangedWeapon(String name, int numberOfDices, int damage, int reach, boolean twoHanded, Quiver quiver) {
        super(name, numberOfDices, damage, reach, twoHanded);
        this.quiver = quiver;
    }

    public Quiver getQuiver() {
        return quiver;
    }

    public void setQuiver(Quiver quiver) {
        this.quiver = quiver;
    }
}
