package combat;

import java.lang.reflect.Method;

public class Skill {
    private String name;
    private String description;
    private Method method;
    private int usagePerBattle;
    private Target target;

    public Skill(String name, String description, Method method, int usagePerBattle, Target target) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.usagePerBattle = usagePerBattle;
        this.target = target;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


    public int getUsagePerBattle() {
        return usagePerBattle;
    }

    public void setUsagePerBattle(int usagePerBattle) {
        this.usagePerBattle = usagePerBattle;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
