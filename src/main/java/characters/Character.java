package characters;

import app.Main;
import attributes.Attribute;
import attributes.DepletableAttribute;
import attributes.ownable.*;
import combat.Effect;
import combat.OutOfAmmunitionException;
import combat.Position;
import combat.UnreachablePositionException;
import combat.skills.Skill;
import combat.skills.SkillWithCountDown;
import items.Equipment.Equipment;
import items.Equipment.RangedWeapon;
import items.Equipment.Weapon;
import items.Equipment.Wieldable;
import items.Equipment.ownable.Quiver;
import items.Equipment.ownable.Shield;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static app.Battlefield.*;
import static app.Combat.getAliveCharactersFromBothSides;
import static app.Main.decideOutcome;
import static app.Main.roll;

public abstract class Character {
    private String name;
    private String givenName;
    private List<Effect> effects = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();
    private List<SkillWithCountDown> skillWithCountDowns = new ArrayList<>();
    private Equipment equipment;
    private HashMap<String, Boolean> statuses = new HashMap<>();
    private Position position = new Position(0, 0);

    public Character(String name, String givenName, int health, int dexterity, int armorClass, int speed,
                     Equipment equipment, boolean isFriendly) {
        this.name = name;
        this.givenName = givenName;
        this.equipment = equipment;
        attributes.add(new Health(health, health));
        attributes.add(new ArmorClass(armorClass));
        attributes.add(new DamageBonus());
        attributes.add(new AttackBonus());
        attributes.add(new Dexterity(dexterity));
        attributes.add(new Initiative());
        attributes.add(new Speed(speed));
        if (equipment.getLeftHand() instanceof Shield) {
            getArmorClass().increase(((Shield) equipment.getLeftHand()).getArmorClass().getCurrentValue());
        }
        statuses.put("Alive", true);
        statuses.put("Friendly", isFriendly);
        statuses.put("MovedThisTurn", false);
        statuses.put("Stunned", false);
    }

    public void attack(Character defender) {
        if (getEquipment().getRightHand() instanceof RangedWeapon) {
            rangedAttack();
        }
        System.out.println(MessageFormat.format("\n\t{0} decided to attack.", name));
        int attackingRoll = (new Random().nextInt(20) + 1) + getAttackBonusValue();
        System.out.println(MessageFormat.format("\t{0} rolled {1}.", name, attackingRoll));
        evaluateAttackRoll(attackingRoll, defender);
    }

    private void rangedAttack() {
        try {
            Quiver quiver = ((RangedWeapon) getEquipment().getRightHand()).getQuiver();
            quiver.decreaseAmmunition(1);
        } catch (OutOfAmmunitionException outOfAmmo) {
            System.out.println(MessageFormat.format("\t{0}. Press Enter to get back.", outOfAmmo.getMessage()));
        }
    }

    public void evaluateAttackRoll(int attackingRoll, Character defender) {
        if (attackingRoll == 1) {
            System.out.println(MessageFormat.format(
                    "\tCritical failure! {0} get stunned for 1 turn lamenting over stupidity", name));
            //Stun duration not implemented yet
        } else if (attackingRoll == 20) {
            int damage = dealDamage(defender) * 2;
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
        defender.modifyStatus("Alive", false);
        System.out.println(MessageFormat.format("\t{0} died!", defender.getName()));
        Main.CHARACTERS_ALIVE.remove(defender);
        if (!getAliveCharactersFromBothSides()) {
            decideOutcome();
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
        Wieldable leftHandedWeapon = equipment.getLeftHand();
        int damage = 0;
        if (rightHandedWeapon != null) {
            damage += roll(rightHandedWeapon.getNumberOfDices(), rightHandedWeapon.getDamage());
        }
        if (leftHandedWeapon instanceof Weapon) {
            damage += (roll(((Weapon) leftHandedWeapon).getNumberOfDices(), ((Weapon) leftHandedWeapon).getDamage()));
        }
        return damage;
    }

    public void defend() {
        System.out.println(MessageFormat.format("\t{0} decided to defend.", name));
        getArmorClass().increase(5);
        effects.add(new Effect(getArmorClass(), "Defend", 5, 1));
        System.out.println(MessageFormat.format("\t{0}''s AC increased by {1} for {2} turns.", getName(), 5, 1));
    }

    public void wait(Character character) {
        // useless right now
    }

    public void move(Position position) {
        if (checkIfDestinationIsReacheable(this, position) && checkIfPositionIsOccupied(position)) {
            System.out.println(MessageFormat.format("\t{0} moved from {1} to {2}", getName(), getPosition(), position));
            setPosition(position);
            refreshBattlefield();
        } else {
            throw new UnreachablePositionException("Destination is too far away");
        }
    }

    public void addToEffects(Effect effect) {
        effects.add(effect);
    }

    public Attribute getDamageBonus() {
        return attributes.get(attributes.indexOf(new DamageBonus()));
    }

    public int getDamageBonusValue() {
        return attributes.get(attributes.indexOf(new DamageBonus())).getCurrentValue();
    }

    public int getAttackBonusValue() {
        return attributes.get(attributes.indexOf(new AttackBonus())).getCurrentValue();
    }

    public Attribute getAttackBonus() {
        return attributes.get(attributes.indexOf(new AttackBonus()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public DepletableAttribute getHealth() {
        return (DepletableAttribute) attributes.get(attributes.indexOf(new Health()));
    }

    public int getHealthCurrentValue() {
        return attributes.get(attributes.indexOf(new Health())).getCurrentValue();
    }

    public int getHealthMaxValue() {
        Health health = (Health) (attributes.get(attributes.indexOf(new Health())));
        return health.getMaxValue();
    }

    public Attribute getArmorClass() {
        return attributes.get(attributes.indexOf(new ArmorClass()));
    }

    public int getArmorClassValue() {
        return attributes.get(attributes.indexOf(new ArmorClass())).getCurrentValue();
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public List<Skill> getSkills() {
        return skills;
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

    public HashMap<String, Boolean> getStatuses() {
        return statuses;
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

    public int getWeaponReach() {
        return getEquipment().getRightHand().getReach();
    }

    public boolean isAlive() {
        return getStatuses().get("Alive");
    }

    public boolean isFriendly() {
        return getStatuses().get("Friendly");
    }

    public boolean hasMovedThisTurn() {
        return getStatuses().get("MovedThisTurn");
    }

    public boolean isStunned() {
        return getStatuses().get("Stunned");
    }

    public void modifyStatus(String status, boolean value) {
        getStatuses().put(status, value);
    }
}
