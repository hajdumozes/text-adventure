package characters;

import app.Main;
import combat.Ability;
import items.Equipment.Equipment;
import items.WolfClaw;

import static app.Combat.findPossibleTargets;

public class Wolf extends Character {

    private boolean usedHowl;

    public Wolf() {
        super("Wolf", 11, 15, 5, new Equipment(new WolfClaw(), new WolfClaw()), false);
    }

    @Override
    public void special() {
        System.out.println("\n\tWolf howled!");
        try {
            getAbilityCountdowns().add(new Ability("Howl", 3, this.getClass().getMethod("howl")));
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException("Ability not found" + nsme);
        }
    }

    public void howl() {
        System.out.println("\n\t# Wolf appeared! #");
        Main.CHARACTERS_ALIVE.add(new Wolf());
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
