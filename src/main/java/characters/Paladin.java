package characters;

import items.Equipment.Equipment;
import items.Greataxe;
import items.Handaxe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paladin extends Character implements Playable {
    public Paladin() {
        super("Paladin", 50, 10, new Equipment(new Handaxe(), new Handaxe()));
    }

    @Override
    public void special() {
        getHealth().setValue(getHealth().getValue() + 5);
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
