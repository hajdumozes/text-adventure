package characters;

import app.Main;
import combat.Skill;
import combat.SkillWithCountDown;
import items.Equipment.Equipment;
import items.WolfClaw;

import java.util.ArrayList;
import java.util.List;

import static app.Combat.findPossibleTargets;
import static app.Combat.rollInitiative;

public class Wolf extends Character {

    private boolean usedHowl;
    private SkillWithCountDown howl = new SkillWithCountDown("Howl",
            "Calls for another wolf, which arrives in 3 turns.", findMethod("howl"), 1,
            3, findMethod("wolfArrive"));

    public Wolf() {
        super("Wolf", 11, 15, 5, new Equipment(new WolfClaw(), new WolfClaw()), false);
    }

    @Override
    public List<Skill> showSpecialAttacks() {
        List<Skill> specialAttacks = new ArrayList<>();
        specialAttacks.add(howl);
        return specialAttacks;
    }

    public void howl() {
        System.out.println("\n\tWolf howled!");
        usedHowl = true;
        getSkillWithCountDowns().add(howl);
    }

    public void wolfArrive() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        rollInitiative(reinforcement);
    }

    @Override
    public void letAiDecide() {
        if (getHealth().getCurrentValue() < 10 && !usedHowl) {
            howl();
        } else {
            attack(findPossibleTargets(this).get(0));
        }
    }
}
