package combat.skills.ownable;

import app.Main;
import app.actions.CharacterActions;
import app.actions.SkillManagement;
import characters.Character;
import combat.Position;
import combat.PositionBased;
import combat.effects.StunEffect;
import combat.exceptions.NoTargetException;
import combat.skills.Skill;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

public class Fireball extends Skill implements PositionBased {
    public Fireball(Character owner) {
        super("Fireball", "Deals 1d12 damage on a space, and characters adjacent to it",
                3, owner);
    }

    @Override
    public int getReach() {
        return 5;
    }

    @Override
    public void useSkill(Character character) {
        Position targetPosition = selectPosition(getOwner());
        applyFireballOnPosition(targetPosition);
    }

    private void applyFireballOnPosition(Position targetPosition) {
        try {
            List<Character> charactersInReach = new SkillManagement().
                    filterReachableCharacters(targetPosition, Main.CHARACTERS_ALIVE, 1);
            System.out.println(MessageFormat.format("\n\t{0} casted Fireball on coordinate {1}.",
                    getOwner().getName(), targetPosition));
            int attackingRoll = (new Random().nextInt(20) + 1);
            System.out.println(MessageFormat.format("\t{0} rolled {1}.", getOwner().getName(), attackingRoll));
            shootFireball(attackingRoll, getOwner(), charactersInReach);
        } catch (NoTargetException noTarget) {
            System.out.println("\tInvalid coordinate. Please, check available coordinates and choose again," +
                    "or press Enter to get back.");
            applyFireballOnPosition(selectPosition(getOwner()));
        }
    }

    private void shootFireball(int attackingRoll, Character caster, List<Character> targets) {
        if (attackingRoll == 1) {
            criticalFailure(caster);
        } else {
            for (Character target : targets) {
                evaluateAttackingRoll(attackingRoll, target);
                if (target.getHealthCurrentValue() <= 0) {
                    new CharacterActions().kill(target);
                }
            }
        }
    }

    private void evaluateAttackingRoll(int attackingRoll, Character defender) {
        int damage = new Main().roll(1, 12);
        if (attackingRoll == 20) {
            criticalHit(defender, damage);
        } else if (attackingRoll < defender.getArmorClassValue()) {
            miss(defender);
        } else if (attackingRoll >= defender.getArmorClassValue()) {
            hit(defender, damage);
        }
    }

    private void criticalFailure(Character caster) {
        System.out.println(MessageFormat.format(
                "\tCritical failure! {0}''s hand slipped and the fireball blasted in {0}''s palm.",
                caster.getName()));
        new StunEffect(1).increaseEffectDuration(caster);
        caster.getHealth().decrease(24);
        if (caster.getHealthCurrentValue() <= 0) {
            new CharacterActions().kill(caster);
        }
    }

    private void criticalHit(Character defender, int damage) {
        defender.getHealth().decrease(damage * 2);
        System.out.println(MessageFormat.format(
                "\tCritical hit! Fireball dealt {0} damage to {1}.",
                damage, defender.getName()));
    }

    private void miss(Character defender) {
        System.out.println(MessageFormat.format("\t{0} managed to evade the Fireball.",
                defender.getName()));
    }

    private void hit(Character defender, int damage) {
        defender.getHealth().decrease(damage);
        System.out.println(MessageFormat.format(
                "\tFireball dealt {0} damage to {1}.",
                damage, defender.getName()));
    }
}
