package characters;

import attributes.DamageBonus;
import items.Equipment.Equipment;
import items.Handaxe;
import combat.Status;

public class Barbarian extends Character {
    public Barbarian() {
        super("You", 50, 5, new Equipment(new Handaxe(), new Handaxe()));
    }

    @Override
    public void special() {
        this.getDamageBonus().setValue(getDamageBonus().getValue() + 2);
        addToStatuses(new Status(new DamageBonus(), "Rage", 2, 3));
    }

    @Override
    public void letAiDecide() {

    }
}
