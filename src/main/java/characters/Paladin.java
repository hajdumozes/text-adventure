package characters;

import items.Handaxe;

public class Paladin extends Character {
    public Paladin() {
        super("You", 50, 10, new Handaxe());
    }

    @Override
    public void special() {
        getHealth().setValue(getHealth().getValue() + 5);
    }

    @Override
    public void letAiDecide() {

    }
}
