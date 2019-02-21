package items.Equipment.ownable;

import combat.OutOfAmmunitionException;

public class Quiver {
    private int ammunition;

    public Quiver(int ammunition) {
        this.ammunition = ammunition;
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
    }

    public void decreaseAmmunition(int value) {
        if (ammunition > 0) {
            ammunition -= 1;
        } else {
            throw new OutOfAmmunitionException("Out of ammo");
        }
    }
}
