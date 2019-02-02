package characters;

import attributes.ArmorClass;
import attributes.Attribute;
import attributes.Damage;
import attributes.Health;
import statuses.Status;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Character {
    private String name;
    private Attribute health;
    private Attribute damage;
    private Attribute armorClass;
    private boolean alive = true;
    private List<Status> statuses = new ArrayList<Status>();
    private List<Attribute> attributes = new ArrayList<Attribute>();

    public Character(String name, int health, int damage, int armorClass) {
        this.name = name;
        this.health = new Health(health);
        this.damage = new Damage(damage);
        this.armorClass = new ArmorClass(armorClass);
        attributes.add(this.health);
        attributes.add(this.damage);
        attributes.add(this.armorClass);
    }

    public void attack(Character defender) {
        int attackingRoll = new Random().nextInt(20) + 1;
        System.out.println(MessageFormat.format("\t{0} rolled {1}.", name, attackingRoll));
        if (attackingRoll == 1) {
            //critical failure
        } else if (attackingRoll == 20) {
            int damage = dealDamage(defender) + dealDamage(defender);
            System.out.println(MessageFormat.format("\tCritical hit! {0} dealt {1} damage to {2}.", name, damage, defender.name));
        } else if (attackingRoll < defender.armorClass.getValue()) {
            System.out.println(MessageFormat.format("\t{0} failed to hit {1}.", name, defender.name));
        } else if (attackingRoll >= defender.armorClass.getValue()) {
            int damage = dealDamage(defender);
            System.out.println(MessageFormat.format("\t{0} hit {1}, and dealt {2} damage.", name, defender.name, damage));
        }
        if (defender.health.getValue() <= 0) {
            defender.alive = false;
        }
    }

    public int dealDamage(Character defender) {
        defender.getHealth().decrease(damage.getValue());
        return damage.getValue();
    }

    public void defend() {
        armorClass.increase(5);
        statuses.add(new Status(armorClass, 5, 3));
    }

    public void wait(Character character) {
        // useless right now
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Attribute getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(Attribute armorClass) {
        this.armorClass = armorClass;
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
