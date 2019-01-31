package characters;

public abstract class Character {
    private int health;
    private int damage;
    private int armor;
    private boolean alive = true;

    public Character(int health, int damage, int armor) {
        this.health = health;
        this.damage = damage;
        this.armor = armor;
    }

    public void attack(Character attacker, Character defender) {
        if (attacker.damage > defender.armor) {
            defender.health -= attacker.damage - defender.armor;
        }
        if (defender.health <= 0) {
            defender.alive = false;
        }
    }

    public void wait(Character character) {
        // useless right now
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
