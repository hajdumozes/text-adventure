package items.Equipment.ownable;

import items.Equipment.RangedWeapon;

public class Longbow extends RangedWeapon {
    public Longbow() {
        super("Longbow", 1, 8, 8, true, new Quiver(20));
    }
}
