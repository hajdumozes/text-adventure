package app;

import characters.Character;
import combat.Effect;
import combat.OutOfAmmunitionException;
import combat.Position;
import combat.UnreachablePositionException;
import items.Equipment.RangedWeapon;
import items.Equipment.Weapon;
import items.Equipment.Wieldable;
import items.Equipment.ownable.Quiver;

import java.text.MessageFormat;
import java.util.Random;

import static app.Battlefield.*;
import static app.Combat.getAliveCharactersFromBothSides;
import static app.Combat.printOptionsForCurrentFriendlyCharacter;
import static app.Main.*;

public class CharacterActions {
    protected static void attack(Character attacker, Character defender) {
        if (attacker.getEquipment().getRightHand() instanceof RangedWeapon) {
            rangedAttack(attacker);
        }
        System.out.println(MessageFormat.format("\n\t{0} decided to attack.", attacker.getName()));
        int attackingRoll = (new Random().nextInt(20) + 1) + attacker.getAttackBonusValue();
        System.out.println(MessageFormat.format("\t{0} rolled {1}.", attacker.getName(), attackingRoll));
        evaluateAttackRoll(attackingRoll, attacker, defender);
    }

    private static void rangedAttack(Character attacker) {
        try {
            Quiver quiver = ((RangedWeapon) attacker.getEquipment().getRightHand()).getQuiver();
            quiver.decreaseAmmunition(1);
        } catch (OutOfAmmunitionException outOfAmmo) {
            System.out.println(MessageFormat.format("\t{0}. Press Enter to get back.", outOfAmmo.getMessage()));
            CONSOLE.nextLine();
            printOptionsForCurrentFriendlyCharacter(attacker);
        }
    }

    public static void evaluateAttackRoll(int attackingRoll, Character attacker, Character defender) {
        if (attackingRoll == 1) {
            System.out.println(MessageFormat.format(
                    "\tCritical failure! {0} get stunned for 1 turn lamenting over stupidity", attacker.getName()));
            //Stun duration not implemented yet
        } else if (attackingRoll == 20) {
            int damage = dealDamage(attacker, defender) * 2;
            System.out.println(MessageFormat.format("\tCritical hit! {0} dealt {1} damage to {2}.",
                    attacker.getName(), damage, defender.getName()));
        } else if (attackingRoll < defender.getArmorClassValue()) {
            System.out.println(MessageFormat.format("\t{0} failed to hit {1}.",
                    attacker.getName(), defender.getName()));
        } else if (attackingRoll >= defender.getArmorClassValue()) {
            int damage = dealDamage(attacker, defender);
            System.out.println(MessageFormat.format("\t{0} hit {1}, and dealt {2} damage.",
                    attacker.getName(), defender.getName(), damage));
        }
        if (defender.getHealthCurrentValue() <= 0) {
            kill(defender);
        }
    }

    private static void kill(Character defender) {
        defender.modifyStatus("Alive", false);
        System.out.println(MessageFormat.format("\t{0} died!", defender.getName()));
        Main.CHARACTERS_ALIVE.remove(defender);
        if (!getAliveCharactersFromBothSides()) {
            decideOutcome();
        }
    }

    private static int rollDamage(Character attacker) {
        Weapon rightHandedWeapon = attacker.getEquipment().getRightHand();
        Wieldable leftHandedWeapon = attacker.getEquipment().getLeftHand();
        int damage = 0;
        if (rightHandedWeapon != null) {
            damage += roll(rightHandedWeapon.getNumberOfDices(), rightHandedWeapon.getDamage());
        }
        if (leftHandedWeapon instanceof Weapon) {
            damage += (roll(((Weapon) leftHandedWeapon).getNumberOfDices(), ((Weapon) leftHandedWeapon).getDamage()));
        }
        return damage;
    }

    private static int dealDamage(Character attacker, Character defender) {
        int damage = rollDamage(attacker) + attacker.getDamageBonusValue();
        defender.getHealth().decrease(damage);
        return damage;
    }

    protected static void defend(Character character) {
        System.out.println(MessageFormat.format("\t{0} decided to defend.", character.getName()));
        character.getArmorClass().increase(5);
        character.getEffects().add(new Effect(character.getArmorClass(), "Defend", 5, 1));
        System.out.println(MessageFormat.format("\t{0}''s AC increased by {1} for {2} turns.",
                character.getName(), 5, 1));
    }

    protected static void wait(Character character) {
        // useless right now
    }

    protected static void move(Character character, Position position) {
        if (checkIfDestinationIsReacheable(character, position) && checkIfPositionIsOccupied(position)) {
            System.out.println(MessageFormat.format("\t{0} moved from {1} to {2}",
                    character.getName(), character.getPosition(), position));
            character.setPosition(position);
            refreshBattlefield();
        } else {
            throw new UnreachablePositionException("Destination is too far away");
        }
    }
}
