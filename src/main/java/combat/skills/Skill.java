package combat.skills;

import app.actions.SkillManagement;
import characters.Character;

import java.util.Collection;

public abstract class Skill {
    private String name;
    private String description;
    private int usagePerBattle;
    private Character owner;
    private Character target;

    public Skill(String name, String description, int usagePerBattle, Character owner) {
        this.name = name;
        this.description = description;
        this.usagePerBattle = usagePerBattle;
        this.owner = owner;
    }

    public void applyTo(Collection<Character> characters) {
        if (characters == null) {
            useSkill(null);
        } else {
            for (Character character : characters) {
                useSkill(character);
            }
        }
    }

    protected void decreaseSkillUsage() {
        new SkillManagement().decreaseSkillUsage(this);
    }

    public abstract void useSkill(Character character);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Character getOwner() {
        return owner;
    }

    public int getUsagePerBattle() {
        return usagePerBattle;
    }

    public void setUsagePerBattle(int usagePerBattle) {
        this.usagePerBattle = usagePerBattle;
    }

    public Character getTarget() {
        return target;
    }

    public void setTarget(Character target) {
        this.target = target;
    }
}
