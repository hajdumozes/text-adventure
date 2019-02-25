package characters.playable;

import characters.Character;
import combat.skills.ownable.Berserk;
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
        getSkills().add(new Berserk(this));
    }

    @Override
    public List<Equipment> showStartingEquipments() {
        return new ArrayList<>(Arrays.asList(
                new Equipment(new Handaxe(), new Handaxe()),
                new Equipment(new Greataxe())));
    }

    @Override
    public void letAiDecide() {

    }
}
