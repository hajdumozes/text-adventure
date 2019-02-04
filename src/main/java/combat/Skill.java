package combat;

import java.lang.reflect.Method;

public class Skill {
    private String name;
    private String description;
    private Method method;

    public Skill(String name, String description, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
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
}
