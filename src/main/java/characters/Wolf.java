package characters;

import app.Main;
import combat.Skill;
import items.Equipment.Equipment;
import items.WolfClaw;


import static app.Combat.findPossibleTargets;
import static app.Combat.rollInitiative;

public class Wolf extends Character {

    private boolean usedHowl;

    public Wolf() {
        super("Wolf", 11, 15, 5, new Equipment(new WolfClaw(), new WolfClaw()), false);
    }

    @Override
    public void special() {
        System.out.println("\n\tWolf howled!");
        try {
            getSkills().add(new Skill("Howl", 3, this.getClass().getMethod("howl")));
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException("Skill not found" + nsme);
        }
    }

    public void howl() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        rollInitiative(reinforcement);
    }

    @Override
    public void letAiDecide() {
        if (getHealth().getValue() < 10 && !usedHowl) {
            special();
            usedHowl = true;
        } else {
            attack(findPossibleTargets(this).get(0));
        }
    }
}
