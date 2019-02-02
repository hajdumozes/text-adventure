package characters;

import attributes.DamageBonus;
import items.Handaxe;
import statuses.Status;

public class Barbarian extends Character {
    public Barbarian() {
        super("You", 100, 5, new Handaxe());
    }

    @Override
    public void special() {
        this.getDamageBonus().setValue(getDamageBonus().getValue() + 2);
        addToStatuses(new Status(new DamageBonus(), "Rage", 2, 3));
    }
}
