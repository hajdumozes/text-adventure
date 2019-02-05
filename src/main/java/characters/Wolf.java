package characters;

import app.Main;
import attributes.DamageBonus;
import combat.Skill;
import combat.SkillWithCountDown;
import combat.Status;
import combat.Target;
import items.Equipment.Equipment;
import items.WolfClaw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static app.Combat.*;

public class Wolf extends Character {

    private SkillWithCountDown howl = new SkillWithCountDown("Howl",
            "Calls for another wolf, which arrives in 3 turns.",
            findMethod("howl", null), 1,
            new Target(false, false), 3,
            findMethod("wolfArrive", null));
    private Skill bite = new Skill("Bite", "2d4 +2 damage, +4 to hit",
            findMethod("bite", Character.class),
            3, new Target(true, true));

    public Wolf() {
        super("Wolf", 11, 15, 5, new Equipment(new WolfClaw(), new WolfClaw()), false);
    }

    @Override
    public List<Skill> showSpecialAttacks() {
        List<Skill> specialAttacks = new ArrayList<>();
        specialAttacks.add(howl);
        specialAttacks.add(bite);
        return specialAttacks;
    }

    public void howl() {
        System.out.println("\tWolf howled!");
        getSkillWithCountDowns().add(howl);
        useSkill(howl);
    }

    public void bite(Character target) {
        this.getDamageBonus().setCurrentValue(getDamageBonus().getCurrentValue() + 2);
        addToStatuses(new Status(new DamageBonus(), "Bite", 2, 1));
        System.out.println("\tWolf bit!");
        int hitRoll = Main.roll(1, 20) + 4;
        evaluateAttackRoll(hitRoll, target);
        useSkill(bite);
    }

    public void wolfArrive() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        rollInitiative(reinforcement);
    }

    @Override
    public void letAiDecide() {
        List<Skill> allAvailableSkills = getUsableSkills(this);
        if (allAvailableSkills.size() > 0) {
            Skill selectedSkill = allAvailableSkills.get(new Random().nextInt(allAvailableSkills.size()));
            if (selectedSkill.getTarget().isTargetable()) {
                invokeMethod(selectedSkill.getMethod(), this, letAiChooseTarget());
            } else {
                invokeMethod(selectedSkill.getMethod(), this, null);
            }
        } else {
            attack(letAiChooseTarget());
        }
    }

    private Character letAiChooseTarget() {
        int percentageChance = new Random().nextInt(100) + 1;
        List<Character> enemies = findPossibleTargets(true);
        enemies.sort(Comparator.comparing(character -> character.getHealth().getCurrentValue()));
        if (percentageChance < 70) {
            return enemies.get(enemies.size() - 1);
        } else {
            return enemies.get(new Random().nextInt(enemies.size()));
        }
    }
}
