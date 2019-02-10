package attributes.ownable;

import attributes.DepletableAttribute;

public class Health extends DepletableAttribute {

    public Health(int value, int max) {
        super("Health", value, max);
    }

    public Health() {
        super("Health", 0, 0);
    }
}
