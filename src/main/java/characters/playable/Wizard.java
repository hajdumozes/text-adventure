package characters.playable;

import characters.Character;
import items.Equipment.Equipment;
import items.Equipment.ownable.Quarterstaff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wizard extends Character implements Playable {

    public Wizard() {
        super("Wizard", "Player", 50, 5, 2, 3, new Equipment(new Quarterstaff()),
                true);

    }

    @Override
    public void letAiDecide() {

    }

    @Override
    public List<Equipment> showStartingEquipments() {
        return new ArrayList<>(Arrays.asList(
                new Equipment(new Quarterstaff())));
    }
}