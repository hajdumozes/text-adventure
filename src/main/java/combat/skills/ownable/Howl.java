package combat.skills.ownable;

import app.Main;
import characters.Character;
import characters.Wolf;
import combat.skills.SkillWithCountDown;

import static app.Combat.rollInitiative;
import static app.SkillManagement.findMethod;
import static app.SkillManagement.useSkill;

public class Howl extends SkillWithCountDown {

    public Howl(Character owner) {
        super("Howl", "Calls for another wolf, which arrives in 3 turns.",
                findMethod(Howl.class, "howl", null), 1, owner, 3,
                findMethod(Howl.class, "wolfArrive", null));
    }

    public void howl() {
        System.out.println("\tWolf howled!");
        getOwner().getSkillWithCountDowns().add(this);
        useSkill(this);
    }

    public void wolfArrive() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        rollInitiative(reinforcement);
    }
}
