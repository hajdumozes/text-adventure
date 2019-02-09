package characters;

import app.Battlefield;
import app.Main;
import attributes.*;
import combat.*;
import items.Equipment.Equipment;
import items.Shield;
import items.Weapon;

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
    private List<Status> statuses = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<SkillWithCountDown> skillWithCountDowns = new ArrayList<>();
    private Equipment equipment;
    private boolean isAlive = true;
    private boolean isFriendly;
    private Position position = new Position(0, 0);
    private boolean movedThisTurn = false;

    public Character(String name, int health, int dexterity, int armorClass, int speed, Equipment equipment, boolean isFriendly) {
        this.name = name;
        this.equipment = equipment;
        this.isFriendly = isFriendly;
        attributes.add(new Health(health, health));
        attributes.add(new ArmorClass(armorClass));
        attributes.add(new DamageBonus());
        attributes.add(new Dexterity(dexterity));
        attributes.add(new Initiative());
        attributes.add(new Speed(speed));
        if (equipment.getLeftHand() instanceof Shield) {
            getArmorClass().increase(((Shield) equipment.getLeftHand()).getArmorClass().getCurrentValue());
        }
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
        } else if (attackingRoll < defender.getArmorClassValue()) {
            System.out.println(MessageFormat.format("\t{0} failed to hit {1}.", name, defender.name));
        } else if (attackingRoll >= defender.getArmorClassValue()) {
            int damage = dealDamage(defender);
            System.out.println(MessageFormat.format("\t{0} hit {1}, and dealt {2} damage.", name, defender.name, damage));
        }
        if (defender.getHealthCurrentValue() <= 0) {
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
        int damage = rollDamage() + getDamageBonusValue();
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
        getArmorClass().increase(5);
        statuses.add(new Status(getArmorClass(), "Defend", 5, 1));
        System.out.println(MessageFormat.format("\t{0}''s AC increased by {1} for {2} turns.", 5, 1, 1));
    }

    public void wait(Character character) {
        // useless right now
    }

    public void move(Position position) {
        if (Battlefield.checkIfDestinationIsReacheable(this, position)) {
            setPosition(position);
        } else {
            throw new OutOfReachException("Destination is too far away");
        }
    }

    protected void addToStatuses(Status status) {
        statuses.add(status);
    }

    protected void addToAttributes(Attribute attribute) {
        attributes.add(attribute);
    }

    public Attribute getDamageBonus() {
        return attributes.get(attributes.indexOf(new DamageBonus()));
    }

    public int getDamageBonusValue() {
        return attributes.get(attributes.indexOf(new DamageBonus())).getCurrentValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepletableAttribute getHealth() {
        return (DepletableAttribute) attributes.get(attributes.indexOf(new Health()));
    }

    public int getHealthCurrentValue() {
        return attributes.get(attributes.indexOf(new Health())).getCurrentValue();
    }

    public Attribute getArmorClass() {
        return attributes.get(attributes.indexOf(new ArmorClass()));
    }

    public int getArmorClassValue() {
        return attributes.get(attributes.indexOf(new ArmorClass())).getCurrentValue();
    }

    public List<Status> getStatuses() {
        return statuses;
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


    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Attribute getDexterity() {
        return attributes.get(attributes.indexOf(new Dexterity()));
    }

    public int getDexterityValue() {
        return attributes.get(attributes.indexOf(new Dexterity())).getCurrentValue();
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

    public Attribute getInitiative() {
        return attributes.get(attributes.indexOf(new Initiative()));
    }

    public int getInitiativeValue() {
        return attributes.get(attributes.indexOf(new Initiative())).getCurrentValue();
    }

    public void setInitiativeValue(int value) {
        getInitiative().setCurrentValue(value);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Attribute getSpeed() {
        return attributes.get(attributes.indexOf(new Speed()));
    }


    public int getSpeedValue() {
        return attributes.get(attributes.indexOf(new Speed())).getCurrentValue();
    }

    public void setSpeedValue(int value) {
        getSpeed().setCurrentValue(value);
    }

    public boolean isMovedThisTurn() {
        return movedThisTurn;
    }

    public void setMovedThisTurn(boolean movedThisTurn) {
        this.movedThisTurn = movedThisTurn;
    }
}
