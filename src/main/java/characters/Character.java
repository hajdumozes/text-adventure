package characters;

import attributes.Armor;
import attributes.Attribute;
import attributes.Damage;
import attributes.Health;
import statuses.Status;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    private Attribute health;
    private Attribute damage;
    private Attribute armor;
    private boolean alive = true;
    private List<Status> statuses = new ArrayList<Status>();
    private List<Attribute> attributes = new ArrayList<Attribute>();

    public Character(int health, int damage, int armor) {
        this.health = new Health(health);
        this.damage = new Damage(damage);
        this.armor = new Armor(armor);
        attributes.add(this.health);
        attributes.add(this.damage);
        attributes.add(this.armor);
    }

    public void attack(Character defender) {
        if (damage.getValue() > defender.getArmor().getValue()) {
            defender.getHealth().decrease(damage.getValue() - defender.getArmor().getValue());
        }
        if (defender.health.getValue() <= 0) {
            defender.alive = false;
        }
    }

    public void defend() {
        armor.increase(5);
        statuses.add(new Status(armor, 5, 3));
    }

    public void wait(Character character) {
        // useless right now
    }

    public Attribute getHealth() {
        return health;
    }

    public void setHealth(Attribute health) {
        this.health = health;
    }

    public Attribute getDamage() {
        return damage;
    }

    public void setDamage(Attribute damage) {
        this.damage = damage;
    }

    public Attribute getArmor() {
        return armor;
    }

    public void setArmor(Attribute armor) {
        this.armor = armor;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
