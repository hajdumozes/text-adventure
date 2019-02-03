package characters;

import attributes.DamageBonus;
import items.Equipment.Equipment;
import items.Greataxe;
import items.Handaxe;
import combat.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Barbarian extends Character implements Playable {
    public Barbarian() {
        super("Barbarian", 50, 10, 5, new Equipment(new Handaxe(), new Handaxe()), true);
    }

    @Override
    public void special() {
        this.getDamageBonus().setValue(getDamageBonus().getValue() + 2);
        addToStatuses(new Status(new DamageBonus(), "Rage", 2, 3));
    }

    @Override
    public List<Equipment> showStartingEquipments() {
        return new ArrayList<>(Arrays.asList(
                new Equipment(new Handaxe(), new Handaxe()),
                new Equipment(new Greataxe(), new Greataxe())));
    }

    @Override
    public void letAiDecide() {

    }
}
