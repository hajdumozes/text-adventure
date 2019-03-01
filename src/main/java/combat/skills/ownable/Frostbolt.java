package combat.skills.ownable;

import app.Main;
import app.actions.CharacterActions;
import attributes.ownable.Speed;
import characters.Character;
import combat.DistanceBased;
import combat.Targetable;
import combat.effects.AttributeEffect;
import combat.effects.StunEffect;
import combat.skills.Skill;

import java.text.MessageFormat;
import java.util.Random;

public class Frostbolt extends Skill implements Targetable, DistanceBased {
    public Frostbolt(Character owner) {
        super("Frostbolt", "Deal 1d6 damage, slows and potentially freezes target", 2, owner);
    }

    @Override
    public int getReach() {
        return 5;
    }

    @Override
    public boolean isTargetOnPlayersSide() {
        return !getOwner().isFriendly();
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(MessageFormat.format("\n\t{0} casted Frostbolt on {1}.",
                getOwner().getName(), target.getName()));
        int attackingRoll = (new Random().nextInt(20) + 1);
        System.out.println(MessageFormat.format("\t{0} rolled {1}.", getOwner().getName(), attackingRoll));
        shootFrostbolt(attackingRoll, getOwner(), target);
    }

    private void shootFrostbolt(int attackingRoll, Character attacker, Character defender) {
        evaluateAttackingRoll(attackingRoll, attacker, defender);
        if (defender.getHealthCurrentValue() <= 0) {
            new CharacterActions().kill(defender);
        }
    }

    private void evaluateAttackingRoll(int attackingRoll, Character attacker, Character defender) {
        int damage = new Main().roll(1, 6);
        if (attackingRoll == 1) {
            criticalFailure(attacker);
        } else if (attackingRoll == 20) {
            criticalHit(attacker, defender, damage);
        } else if (attackingRoll < defender.getArmorClassValue()) {
            miss(attacker, defender);
        } else if (attackingRoll >= defender.getArmorClassValue()) {
            hit(attacker, defender, damage);
        }
    }

    private void criticalFailure(Character attacker) {
        System.out.println(MessageFormat.format(
                "\tCritical failure! {0} blasts itself with its own Frostbolt and get frozen for one turn.",
                attacker.getName()));
        new StunEffect(1).increaseEffectDuration(attacker);
    }

    private void criticalHit(Character attacker, Character defender, int damage) {
        defender.getHealth().decrease(damage);
        defender.getStatuses().put("Stunned", true);
        new StunEffect(3).increaseEffectDuration(defender);
        System.out.println(MessageFormat.format(
                "\tCritical hit! {0} dealt {1} damage to {2} and froze {2} for 3 turns.",
                attacker.getName(), damage, defender.getName()));
    }

    private void miss(Character attacker, Character defender) {
        System.out.println(MessageFormat.format("\t{0} failed to hit {1}.",
                attacker.getName(), defender.getName()));
    }

    private void hit(Character attacker, Character defender, int damage) {
        defender.getHealth().decrease(damage);
        int speed = defender.getSpeedValue();
        int newSpeed = speed / 2;
        defender.getSpeed().setCurrentValue(newSpeed);
        defender.getEffects().add(new AttributeEffect(
                new Speed(), "slowed down by frostbolt", 0 - (speed - newSpeed), 3));
        System.out.println(MessageFormat.format(
                "\t{0} hit {1}, and dealt {2} damage and slowed down by 50% for 3 turns.",
                attacker.getName(), defender.getName(), damage));
    }
}
