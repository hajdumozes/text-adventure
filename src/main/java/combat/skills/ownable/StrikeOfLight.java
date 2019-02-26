package combat.skills.ownable;

import characters.Character;
import combat.DistanceBased;
import combat.Targetable;
import combat.effects.StatusEffect;
import combat.skills.Skill;


public class StrikeOfLight extends Skill implements Targetable, DistanceBased {
    public StrikeOfLight(Character owner) {
        super("Strike of Light", "Stun target for 2 turns.",
                1, owner);
    }

    public void useSkill(Character target) {
        target.getStatuses().put("Stunned", true);
        StatusEffect statusEffect = new StatusEffect("Stun", 2, "Stunned", false);
        statusEffect.increaseEffectDuration(target, statusEffect);
        decreaseSkillUsage();
    }

    @Override
    public int getReach() {
        return 1;
    }

    @Override
    public boolean isTargetOnPlayersSide() {
        return false;
    }
}
