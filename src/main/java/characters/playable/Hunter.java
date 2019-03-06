package characters.playable;

import app.Main;
import characters.Character;
import characters.animals.Wolf;
import combat.skills.ownable.SetTrap;
import combat.skills.ownable.SummonAnimal;
import items.Equipment.Equipment;
import items.Equipment.ownable.Longbow;
import items.Equipment.ownable.Shortsword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hunter extends Character implements Playable {
    public Hunter() {
        this("Player");
    }

    public Hunter(String name) {
        super("Hunter", name, 50, 10, 5, 4, new Equipment(new Longbow()),
                true);
        getSkills().add(new SummonAnimal(this));
        getSkills().add(new SetTrap(this));
        Main.SUMMONABLE_CHARACTERS.add(new Wolf(true, this, "Hunter's Wolf"));
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
