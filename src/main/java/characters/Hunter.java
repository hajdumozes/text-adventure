package characters;

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
