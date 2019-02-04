package characters;

import app.Main;
import combat.Skill;
import items.Equipment.Equipment;
import items.WolfClaw;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static app.Combat.findPossibleTargets;
import static app.Combat.rollInitiative;

public class Wolf extends Character {

    private boolean usedHowl;

    public Wolf() {
        super("Wolf", 11, 15, 5, new Equipment(new WolfClaw(), new WolfClaw()), false);
    }

    @Override
    public List<Method> showSpecialAttacks() {
        List<Method> specialAttacks = new ArrayList<>();
        specialAttacks.add(findMethod("howl"));
        return specialAttacks;
    }

    private void howl() {
        System.out.println("\n\tWolf howled!");
        usedHowl = true;
        getSkills().add(new Skill("Howl", 3, findMethod("wolfArrive")));
    }

    public void wolfArrive() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        rollInitiative(reinforcement);
    }

    @Override
    public void letAiDecide() {
        if (getHealth().getValue() < 10 && !usedHowl) {
            howl();
        } else {
            attack(findPossibleTargets(this).get(0));
        }
    }
}
