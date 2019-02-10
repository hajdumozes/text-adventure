package characters;

import app.Combat;
import combat.Skill;
import combat.Target;
import items.Equipment.Equipment;
import items.Longsword;
import items.Shield;
import items.Shortsword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paladin extends Character implements Playable {
    private Skill layOnHands = new Skill("Lay on Hands", "Heal self for 5HP",
            findMethod("layOnHands", Character.class), 3, new Target(true, true));

    public Paladin() {
        super("Paladin", "Player", 500, 10, 10, 4, new Equipment(new Longsword(), new Shield()), true);
    }

    @Override
    public List<Skill> showSpecialAttacks() {
        List<Skill> specialAttacks = new ArrayList<>();
        specialAttacks.add(layOnHands);
        return specialAttacks;
    }

    public void layOnHands(Character target) {
        int currentHealth = target.getHealth().getCurrentValue();
        int maxHealth = target.getHealth().getMaxValue();
        if (currentHealth + 5 > maxHealth) {
            target.getHealth().setCurrentValue(maxHealth);
        } else {
            target.getHealth().setCurrentValue(currentHealth + 5);
        }
        Combat.useSkill(layOnHands);
    }

    @Override
    public List<Equipment> showStartingEquipments() {
        return new ArrayList<>(Arrays.asList(
                new Equipment(new Longsword(), new Shield()),
                new Equipment(new Shortsword(), new Shortsword())));
    }

    @Override
    public void letAiDecide() {

    }
}
