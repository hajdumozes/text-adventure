package characters.playable;

import characters.Character;
import combat.skills.ownable.LayOnHands;
import combat.skills.ownable.StrikeOfLight;
import items.Equipment.Equipment;
import items.Equipment.ownable.Longsword;
import items.Equipment.ownable.Shield;
import items.Equipment.ownable.Shortsword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paladin extends Character implements Playable {

    public Paladin() {
        super("Paladin", "Player", 500, 10, 5, 4,
                new Equipment(new Longsword(), new Shield()), true);
        getSkills().add(new LayOnHands(this));
        getSkills().add(new StrikeOfLight(this));
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
