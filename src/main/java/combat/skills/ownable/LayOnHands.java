package combat.skills.ownable;

import characters.Character;
import combat.DistanceBased;
import combat.Targetable;
import combat.skills.Skill;

import static app.SkillManagement.decreaseSkillUsage;

public class LayOnHands extends Skill implements Targetable, DistanceBased {

    public LayOnHands(Character owner) {
        super("Lay on Hands", "Heal ally for 5HP",
                3, owner);
    }

    public void useSkill(Character target) {
        int currentHealth = target.getHealthCurrentValue();
        int maxHealth = target.getHealthMaxValue();
        if (currentHealth + 5 > maxHealth) {
            target.getHealth().setCurrentValue(maxHealth);
        } else {
            target.getHealth().setCurrentValue(currentHealth + 5);
        }
        decreaseSkillUsage(this);
    }

    @Override
    public int getReach() {
        return 1;
    }

    @Override
    public boolean isTargetOnPlayersSide() {
        return true;
    }
}
