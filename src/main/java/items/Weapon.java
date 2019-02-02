package items;

public abstract class Weapon {
    private int numberOfDices;
    private int damage;

    public Weapon(int numberOfDices, int damage) {
        this.numberOfDices = numberOfDices;
        this.damage = damage;
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
}
