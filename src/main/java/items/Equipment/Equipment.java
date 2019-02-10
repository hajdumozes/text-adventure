package items.Equipment;

import java.text.MessageFormat;

public class Equipment {
    private Weapon rightHand;
    private Wieldable leftHand;

    public Equipment(Weapon rightHand, Wieldable leftHand) {
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

    public Wieldable getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(Wieldable wieldable) {
        if (wieldable instanceof Weapon) {
            Weapon weapon = (Weapon) wieldable;
            if (weapon.isTwoHanded()) {
                this.leftHand = weapon;
                this.rightHand = weapon;
            } else {
                this.leftHand = weapon;
            }
        } else {
            this.leftHand = wieldable;
        }
    }

    @Override
    public String toString() {
        if (!rightHand.isTwoHanded()) {
            return MessageFormat.format("\tRight hand: {0},\n\t\tLeft hand: {1}.", rightHand, leftHand);
        } else {
            return MessageFormat.format("\tRight hand: {0}", rightHand);
        }
    }
}
