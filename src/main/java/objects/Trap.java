package objects;

import app.Main;
import characters.Character;

import java.text.MessageFormat;

public class Trap extends BattlefieldObject {
    private int numberOfDices;
    private int damage;

    public Trap(int numberOfDices, int damage) {
        super("Trap");
        this.numberOfDices = numberOfDices;
        this.damage = damage;
    }

    public int getNumberOfDices() {
        return numberOfDices;
    }

    public void setNumberOfDices(int numberOfDices) {
        this.numberOfDices = numberOfDices;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void trigger(Character target) {
        System.out.println(MessageFormat.format("\n\t{0} Stepped on a trap.", target.getName()));
        int dexterityCheck = new Main().roll(1, 20) + target.getDexterityValue();
        boolean evade = dexterityCheck >= 20;
        System.out.println(MessageFormat.format("\t{0} rolled {1} for dexterity check",
                target.getName(), dexterityCheck));
        System.out.println(evade ?
                MessageFormat.format("\t{0} successfully evaded the trap", target.getName()) :
                MessageFormat.format("\t{0} failed to evade the trap", target.getName()));
        if (!evade) {
            int trapDamage = new Main().roll(numberOfDices, damage);
            target.getHealth().setCurrentValue(target.getHealthCurrentValue() - trapDamage);
            System.out.println(MessageFormat.format("\tTrap dealt {0} damage to {1}",
                    trapDamage, target.getName()));
        }
    }
}
