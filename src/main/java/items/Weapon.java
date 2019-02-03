package items;

public abstract class Weapon {
    private int numberOfDices;
    private int damage;
    private boolean twoHanded;

    public Weapon(int numberOfDices, int damage, boolean twoHanded) {
        this.numberOfDices = numberOfDices;
        this.damage = damage;
        this.twoHanded = twoHanded;
    }

    public int getNumberOfDices() {
        return numberOfDices;
    }

    public void setNumberOfDices(int numberOfDices) {
        this.numberOfDices = numberOfDices;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public void setTwoHanded(boolean twoHanded) {
        this.twoHanded = twoHanded;
    }
}
