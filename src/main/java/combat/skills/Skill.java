package combat.skills;

import characters.Character;

import java.lang.reflect.Method;

public class Skill {
    private String name;
    private String description;
    private Method method;
    private int usagePerBattle;
    private Character owner;

    public Skill(String name, String description, Method method, int usagePerBattle, Character owner) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.usagePerBattle = usagePerBattle;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Method getMethod() {
        return method;
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
}
