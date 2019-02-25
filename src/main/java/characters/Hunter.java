package characters;

import app.Main;
import characters.animals.Wolf;
import combat.skills.ownable.SummonAnimal;
import items.Equipment.Equipment;
import items.Equipment.ownable.Longbow;
import items.Equipment.ownable.Shortsword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hunter extends Character implements Playable {
    public Hunter() {
        super("Hunter", "Player", 50, 10, 5, 4, new Equipment(new Longbow()),
                true);
        getSkills().add(new SummonAnimal(this));
        Main.SUMMONABLE_CHARACTERS.add(new Wolf(true, this));
    }

    @Override
    public void letAiDecide() {
    }

    @Override
    public List<Equipment> showStartingEquipments() {
        return new ArrayList<>(Arrays.asList(
                new Equipment(new Longbow()),
                new Equipment(new Shortsword(), new Shortsword())));
    }
}
