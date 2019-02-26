package objects;

import app.Main;
import characters.Character;
import combat.effects.StatusEffect;
import combat.exceptions.CantFinishAction;

import java.text.MessageFormat;

public class Trap extends BattlefieldObject {
    private int numberOfDices;
    private int damage;
    private int stunDuration;

    public Trap(int numberOfDices, int damage, int stunDuration) {
        super("Trap");
        this.numberOfDices = numberOfDices;
        this.damage = damage;
        this.stunDuration = stunDuration;
    }

    public void trigger(Character target) {
        System.out.println(MessageFormat.format("\n\t{0} Stepped on a trap.", target.getName()));
        int dexterityCheck = new Main().roll(1, 20) + target.getDexterityValue();
        boolean evade = dexterityCheck >= 20;
        System.out.println(MessageFormat.format("\t{0} rolled {1} for dexterity check.",
                target.getName(), dexterityCheck));
        if (evade) {
            evade(target);
        } else {
            hit(target);
        }
    }

    private void evade(Character target) {
        System.out.println(MessageFormat.format("\t{0} successfully evaded the trap.", target.getName()));
        throw new CantFinishAction("because the unexpected trap.");
    }

    private void hit(Character target) {
        System.out.println(MessageFormat.format("\t{0} failed to evade the trap.", target.getName()));
        int trapDamage = new Main().roll(numberOfDices, damage);
        target.getHealth().setCurrentValue(target.getHealthCurrentValue() - trapDamage);
        target.getStatuses().put("Stunned", true);
        StatusEffect stunEffect = new StatusEffect("Stun", stunDuration, "Stunned", false);
        stunEffect.increaseEffectDuration(target, stunEffect);
        System.out.println(MessageFormat.format("\tTrap dealt {0} damage to {1}. {1} is stunned for {2} turns.",
                trapDamage, target.getName(), stunDuration));
        throw new CantFinishAction("because the unexpected trap.");
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

    public int getStunDuration() {
        return stunDuration;
    }

    public void setStunDuration(int stunDuration) {
        this.stunDuration = stunDuration;
    }
}
