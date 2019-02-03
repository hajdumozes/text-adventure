package characters;

import items.Equipment.Equipment;
import items.Handaxe;

public class Paladin extends Character {
    public Paladin() {
        super("Paladin", 50, 10, new Equipment(new Handaxe(), new Handaxe()));
    }

    @Override
    public void special() {
        getHealth().setValue(getHealth().getValue() + 5);
    }

    @Override
    public void letAiDecide() {

    }
}
