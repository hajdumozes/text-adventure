package combat.skills.ownable;

import characters.Character;
import combat.Targetable;
import combat.skills.Skill;

import static app.SkillManagement.findMethod;
import static app.SkillManagement.useSkill;

public class LayOnHands extends Skill implements Targetable {

    public LayOnHands(Character owner) {
        super("Lay on Hands", "Heal ally for 5HP",
                findMethod(LayOnHands.class, "layOnHands", Character.class), 3, owner);
    }

    public void layOnHands(Character target) {
        int currentHealth = target.getHealthCurrentValue();
        int maxHealth = target.getHealthMaxValue();
        if (currentHealth + 5 > maxHealth) {
            target.getHealth().setCurrentValue(maxHealth);
        } else {
            target.getHealth().setCurrentValue(currentHealth + 5);
        }
        useSkill(this);
    }

    @Override
    public boolean isTargetOnPlayersSide() {
        return true;
    }
}
