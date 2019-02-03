package characters;

import app.Main;
import combat.Ability;
import items.Equipment.Equipment;
import items.WolfClaw;

public class Wolf extends Character {

    private boolean usedHowl;

    public Wolf() {
        super("Wolf", 20, 5, new Equipment(new WolfClaw(), new WolfClaw()));
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
        Main.HOSTILE_PARTY.add(new Wolf());
    }

    @Override
    public void letAiDecide() {
        if (getHealth().getValue() < 10 && !usedHowl) {
            special();
            usedHowl = true;
        } else {
            attack(Main.FRIENDLY_PARTY.get(0));
        }
    }
}
