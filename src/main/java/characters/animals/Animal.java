package characters.animals;

import characters.Character;
import characters.Summonable;
import items.Equipment.Equipment;

public abstract class Animal extends Character implements Summonable {
    private Character owner;

    public Animal(String name, String givenName, int health, int dexterity, int armorClass, int speed,
                  Equipment equipment, boolean isFriendly, Character owner) {
        super(name, givenName, health, dexterity, armorClass, speed, equipment, isFriendly);
        this.owner = owner;
    }

    public Character getOwner() {
        return owner;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }
}
