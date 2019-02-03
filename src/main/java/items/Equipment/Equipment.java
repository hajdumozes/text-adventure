package items.Equipment;

import items.Weapon;

public class Equipment {
    private Weapon rightHand;
    private Weapon leftHand;

    public Equipment(Weapon rightHand, Weapon leftHand) {
        this.rightHand = rightHand;
        this.leftHand = leftHand;
    }

    public Weapon getRightHand() {
        return rightHand;
    }

    public void setRightHand(Weapon weapon) {
        if (weapon.isTwoHanded()) {
            this.rightHand = weapon;
            this.leftHand = weapon;
        } else {
            this.rightHand = weapon;
        }
    }

    public Weapon getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(Weapon weapon) {
        if (weapon.isTwoHanded()) {
            this.leftHand = weapon;
            this.rightHand = weapon;
        } else {
            this.leftHand = weapon;
        }
    }

    @Override
    public String toString() {
        return "\tRight hand: " + rightHand.getName() +
                "\n\t\tLeft hand: " + leftHand.getName() + "\n";
    }
}
