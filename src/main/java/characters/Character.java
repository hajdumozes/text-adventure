package characters;

import attributes.Attribute;
import attributes.DepletableAttribute;
import attributes.ownable.*;
import combat.Effect;
import combat.Position;
import combat.skills.Skill;
import combat.skills.SkillWithCountDown;
import items.Equipment.Equipment;
import items.Equipment.ownable.Shield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Character {
    private String name;
    private String className;
    private List<Effect> effects = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();
    private List<SkillWithCountDown> skillWithCountDowns = new ArrayList<>();
    private Equipment equipment;
    private HashMap<String, Boolean> statuses = new HashMap<>();
    private Position position = new Position(0, 0);

    public Character(String className, String name, int health, int dexterity, int armorClass, int speed,
                     Equipment equipment, boolean isFriendly) {
        this.name = name;
        this.className = className;
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

    public abstract void letAiDecide();


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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
