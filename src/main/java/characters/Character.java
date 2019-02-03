package characters;

import app.Main;
import attributes.*;
import combat.Ability;
import items.Equipment.Equipment;
import items.Weapon;
import combat.Status;
import org.w3c.dom.Attr;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public abstract class Character {
    private String name;
    private Attribute health;
    private Attribute armorClass;
    private Attribute dexterity;
    private Attribute damageBonus = new DamageBonus();
    private List<Status> statuses = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<Ability> abilityCountdowns = new ArrayList<>();
    private Equipment equipment;

    public Character(String name, int health, int dexterity, int armorClass, Equipment equipment) {
        this.name = name;
        this.health = new Health(health);
        this.armorClass = new ArmorClass(armorClass);
        this.dexterity = new Dexterity(dexterity);
        this.equipment = equipment;
        attributes.add(this.health);
        attributes.add(this.armorClass);
        attributes.add(new DamageBonus());
        attributes.add(this.dexterity);
    }

    public void attack(Character defender) {
        int attackingRoll = new Random().nextInt(20) + 1;
        System.out.println(MessageFormat.format("\n\t{0} rolled {1}.", name, attackingRoll));
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
            System.out.println(MessageFormat.format("\t{0} died!", defender.getName()));
            if (!Main.HOSTILE_PARTY.remove(defender)) {
                Main.FRIENDLY_PARTY.remove(defender);
            }
        }
    }

    public abstract void special();

    public abstract void letAiDecide();

    private int dealDamage(Character defender) {
        int damage = rollDamage() + damageBonus.getValue();
        defender.getHealth().decrease(damage);
        return damage;
    }

    public static int roll(int numberOfDices, int maxNumber) {
        return numberOfDices * new Random().nextInt(maxNumber) + 1;
    }

    private int rollDamage() {
        Weapon rightHandedWeapon = equipment.getRightHand();
        Weapon leftHandedWeapon = equipment.getLeftHand();
        int damageWithTwoWeapon = (roll(rightHandedWeapon.getNumberOfDices(), rightHandedWeapon.getDamage())) +
                (roll(leftHandedWeapon.getNumberOfDices(), leftHandedWeapon.getDamage()));
        if (!rightHandedWeapon.isTwoHanded() && !leftHandedWeapon.isTwoHanded()) {
            return damageWithTwoWeapon;
        } else {
            return damageWithTwoWeapon / 2;
        }
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

    public Attribute getDamageBonus() {
        return damageBonus;
    }

    public void setDamageBonus(Attribute damageBonus) {
        this.damageBonus = damageBonus;
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

    public List<Ability> getAbilityCountdowns() {
        return abilityCountdowns;
    }

    public void setAbilityCountdowns(List<Ability> abilityCountdowns) {
        this.abilityCountdowns = abilityCountdowns;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Attribute getDexterity() {
        return dexterity;
    }

    public void setDexterity(Attribute dexterity) {
        this.dexterity = dexterity;
    }
}
