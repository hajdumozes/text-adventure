package characters;

import combat.skills.ownable.Rage;
import items.Equipment.Equipment;
import items.Equipment.ownable.Greataxe;
import items.Equipment.ownable.Handaxe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Barbarian extends Character implements Playable {

    public Barbarian() {
        super("Barbarian", "Player", 50, 10, 5, 4, new Equipment(new Handaxe(), new Handaxe()), true);
        getSkills().add(new Rage(this));
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
