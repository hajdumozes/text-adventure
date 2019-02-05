package characters;

import app.Main;
import attributes.*;
import combat.Skill;
import combat.SkillWithCountDown;
import items.Equipment.Equipment;
import items.Weapon;
import combat.Status;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static app.Combat.getAliveCharactersFromBothSides;
import static app.Main.decideOutcome;
import static app.Main.roll;

public abstract class Character {
    private String name;
    private DepletableAttribute health;
    private Attribute armorClass;
    private Attribute dexterity;
    private Attribute damageBonus = new DamageBonus();
    private Initiative initiative = new Initiative();
    private List<Status> statuses = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<SkillWithCountDown> skillWithCountDowns = new ArrayList<>();
    private Equipment equipment;
    private boolean isAlive = true;
    private boolean isFriendly;

    public Character(String name, int health, int dexterity, int armorClass, Equipment equipment, boolean isFriendly) {
        this.name = name;
        this.health = new Health(health, health);
        this.armorClass = new ArmorClass(armorClass);
        this.dexterity = new Dexterity(dexterity);
        this.equipment = equipment;
        this.isFriendly = isFriendly;
        attributes.add(this.health);
        attributes.add(this.armorClass);
        attributes.add(new DamageBonus());
        attributes.add(this.dexterity);
    }

    public void attack(Character defender) {
        System.out.println(MessageFormat.format("\n\t{0} decided to attack.", name));
        int attackingRoll = new Random().nextInt(20) + 1;
        System.out.println(MessageFormat.format("\t{0} rolled {1}.", name, attackingRoll));
        evaluateAttackRoll(attackingRoll, defender);
    }

    protected void evaluateAttackRoll(int attackingRoll, Character defender) {
        if (attackingRoll == 1) {
            //critical failure
        } else if (attackingRoll == 20) {
            int damage = dealDamage(defender) + dealDamage(defender);
            System.out.println(MessageFormat.format("\tCritical hit! {0} dealt {1} damage to {2}.", name, damage, defender.name));
        } else if (attackingRoll < defender.armorClass.getCurrentValue()) {
            System.out.println(MessageFormat.format("\t{0} failed to hit {1}.", name, defender.name));
        } else if (attackingRoll >= defender.armorClass.getCurrentValue()) {
            int damage = dealDamage(defender);
            System.out.println(MessageFormat.format("\t{0} hit {1}, and dealt {2} damage.", name, defender.name, damage));
        }
        if (defender.health.getCurrentValue() <= 0) {
            kill(defender);
        }
    }

    private void kill(Character defender) {
        defender.isAlive = false;
        System.out.println(MessageFormat.format("\t{0} died!", defender.getName()));
        Main.CHARACTERS_ALIVE.remove(defender);
        if (!getAliveCharactersFromBothSides()) {
            decideOutcome();
        }
    }

    public abstract List<Skill> showSpecialAttacks();

    protected Method findMethod(String methodName, Class targetCharacter) {
        try {
            if (targetCharacter == null) {
                return this.getClass().getMethod(methodName);
            } else {
                return this.getClass().getMethod(methodName, targetCharacter);
            }
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException("Method not found" + nsme);
        }
    }

    public abstract void letAiDecide();

    private int dealDamage(Character defender) {
        int damage = rollDamage() + damageBonus.getCurrentValue();
        defender.getHealth().decrease(damage);
        return damage;
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
        System.out.println(MessageFormat.format("\t{0} decided to defend.", name));
        armorClass.increase(5);
        statuses.add(new Status(armorClass, "Defend", 5, 1));
        System.out.println(MessageFormat.format("\t{0}''s AC increased by {1} for {2} turns.", 5, 1));
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

    public DepletableAttribute getHealth() {
        return health;
    }

    public void setHealth(DepletableAttribute health) {
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

    public List<SkillWithCountDown> getSkillWithCountDowns() {
        return skillWithCountDowns;
    }

    public void setSkillWithCountDowns(List<SkillWithCountDown> skillWithCountDowns) {
        this.skillWithCountDowns = skillWithCountDowns;
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
    }

    public Initiative getInitiative() {
        return initiative;
    }

    public void setInitiative(Initiative initiative) {
        this.initiative = initiative;
    }
}
