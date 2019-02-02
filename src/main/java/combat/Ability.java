package combat;

import java.lang.reflect.Method;

public class Ability {
    private String name;
    private int countdown;
    private Method method;

    public Ability(String name, int countdown, Method method) {
        this.name = name;
        this.countdown = countdown;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
