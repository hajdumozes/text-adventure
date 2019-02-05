package characters;

import combat.Skill;
import items.*;
import items.Equipment.Equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paladin extends Character implements Playable {
    private Skill layOnHands = new Skill("Lay on Hands", "Heal self for 5HP",
            findMethod("layOnHands"), 3);

    public Paladin() {
        super("Paladin", 50, 10, 10, new Equipment(new Longsword(), new Shield()), true);
    }

    @Override
    public List<Skill> showSpecialAttacks() {
        List<Skill> specialAttacks = new ArrayList<>();
        specialAttacks.add(layOnHands);
        return specialAttacks;
    }

    public void layOnHands() {
        int currentHealth = getHealth().getCurrentValue();
        int maxHealth = getHealth().getMaxValue();
        if (currentHealth + 5 > maxHealth) {
            getHealth().setCurrentValue(maxHealth);
        } else {
            getHealth().setCurrentValue(currentHealth + 5);
        }
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
