package combat.skills.ownable;

import app.Combat;
import app.Main;
import app.SkillManagement;
import characters.Character;
import characters.animals.Wolf;
import combat.skills.SkillWithCountDown;


public class Howl extends SkillWithCountDown {

    public Howl(Character owner) {
        super("Howl", "Calls for another wolf, which arrives in 3 turns.",
                1, owner, 3);
    }

    public void useSkill(Character target) {
        System.out.println("\tWolf howled!");
        getOwner().getSkillWithCountDowns().add(this);
        new SkillManagement().decreaseSkillUsage(this);
    }

    @Override
    public void activateCountdownSkill() {
        wolfArrive();
    }

    public void wolfArrive() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        new Combat().rollInitiative(reinforcement);
    }
}
