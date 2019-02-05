package characters;

import attributes.DamageBonus;
import combat.Skill;
import combat.Target;
import items.Equipment.Equipment;
import items.Greataxe;
import items.Handaxe;
import combat.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Barbarian extends Character implements Playable {
    private Skill rage = new Skill("Rage", "Increases damage by 2 for 3 turns.",
            findMethod("rage", null), 1, new Target(false, false));

    public Barbarian() {
        super("Barbarian", 50, 10, 5, new Equipment(new Handaxe(), new Handaxe()), true);
    }

    @Override
    public List<Skill> showSpecialAttacks() {
        List<Skill> specialAttacks = new ArrayList<>();
        specialAttacks.add(rage);
        return specialAttacks;
    }

    public void rage() {
        this.getDamageBonus().setCurrentValue(getDamageBonus().getCurrentValue() + 2);
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
