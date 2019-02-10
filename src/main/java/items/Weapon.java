package items;

import java.text.MessageFormat;

public abstract class Weapon extends Item implements Wieldable {
    private int numberOfDices;
    private int damage;
    private int reach;
    private boolean twoHanded;

    public Weapon(String name, int numberOfDices, int damage, int reach, boolean twoHanded) {
        super(name);
        this.numberOfDices = numberOfDices;
        this.damage = damage;
        this.reach = reach;
        this.twoHanded = twoHanded;
    }

    public int getNumberOfDices() {
        return numberOfDices;
    }


    public int getDamage() {
        return damage;
    }

    public int getReach() {
        return reach;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }


    @Override
    public String toString() {
        String handToWield = twoHanded ? "Two-handed" : "One-handed";
        return MessageFormat.format("{0} {1}d{2}, {3}, Reach: {4}", getName(), getNumberOfDices(),
                getDamage(), handToWield, reach);
    }
}
