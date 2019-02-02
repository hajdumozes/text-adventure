package characters;

import attributes.ArmorClass;
import attributes.Attribute;
import attributes.DamageBonus;
import attributes.Health;
import items.Weapon;
import statuses.Status;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Character {
    private String name;
    private Attribute health;
    private Attribute armorClass;
    private Attribute damageBonus = new DamageBonus();
    private List<Status> statuses = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private Weapon weaponEquipped;

    public Character(String name, int health, int armorClass, Weapon weaponEquipped) {
        this.name = name;
        this.health = new Health(health);
        this.armorClass = new ArmorClass(armorClass);
        this.weaponEquipped = weaponEquipped;
        attributes.add(this.health);
        attributes.add(this.armorClass);
        attributes.add(new DamageBonus());
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
            throw new CharacterDiedException(defender);
        }
    }

    public abstract void special();

    private int dealDamage(Character defender) {
        int damage = rollDamage() + damageBonus.getValue();
        defender.getHealth().decrease(damage);
        return damage;
    }

    private int rollDamage() {
        return weaponEquipped.getNumberOfDices() *
                new Random().nextInt(weaponEquipped.getDamage()) + 1;
    }

    public void defend() {
        armorClass.increase(5);
        statuses.add(new Status(armorClass, "Defend", 5, 3));
    }

    public void wait(Character character) {
        // useless right now
    }

    protected void addToStatuses(Status status) {
        statuses.add(status);
    }

    protected void addToAttributes(Attribute attribute) {
        attributes.add(attribute);
    }

    public Weapon getWeaponEquipped() {
        return weaponEquipped;
    }

    public Attribute getDamageBonus() {
        return damageBonus;
    }

    public void setDamageBonus(Attribute damageBonus) {
        this.damageBonus = damageBonus;
    }

    public void setWeaponEquipped(Weapon weaponEquipped) {
        this.weaponEquipped = weaponEquipped;
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

    public Attribute getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(Attribute armorClass) {
        this.armorClass = armorClass;
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
