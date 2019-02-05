package combat;

import java.lang.reflect.Method;

public class SkillWithCountDown extends Skill{
    private int countdown;
    private Method methodToInvoke;

    public SkillWithCountDown(String name, String description, Method method, int usagePerBattle, int countdown, Method methodToInvoke) {
        super(name, description, method, usagePerBattle);
        this.countdown = countdown;
        this.methodToInvoke = methodToInvoke;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public Method getMethodToInvoke() {
        return methodToInvoke;
    }

    public void setMethodToInvoke(Method methodToInvoke) {
        this.methodToInvoke = methodToInvoke;
    }
}
