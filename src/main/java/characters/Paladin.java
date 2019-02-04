package characters;

import items.*;
import items.Equipment.Equipment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paladin extends Character implements Playable {
    public Paladin() {
        super("Paladin", 50,10, 10, new Equipment(new Longsword(), new Shield()), true);
    }

    @Override
    public List<Method> showSpecialAttacks() {
        List<Method> specialAttacks = new ArrayList<>();
        specialAttacks.add(findMethod("layOnHands"));
        return specialAttacks;
    }

    public void layOnHands() {
        getHealth().setValue(getHealth().getValue() + 5);
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
