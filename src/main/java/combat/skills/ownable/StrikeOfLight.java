package combat.skills.ownable;

import characters.Character;
import combat.DistanceBased;
import combat.Targetable;
import combat.skills.SkillWithCountDown;

import java.text.MessageFormat;

import static app.SkillManagement.findMethod;
import static app.SkillManagement.useSkill;


public class StrikeOfLight extends SkillWithCountDown implements Targetable, DistanceBased {
    public StrikeOfLight(Character owner) {
        super("Strike of Light", "Stun target for 2 turns.",
                findMethod(StrikeOfLight.class, "stun", Character.class), 1, owner,
                2, findMethod(StrikeOfLight.class, "nullifyStun", null));
    }

    public void stun(Character target) {
        target.getStatuses().put("Stunned", true);
        target.getSkillWithCountDowns().add(this);
        useSkill(this);
        setTarget(target);
    }

    public void nullifyStun() {
        System.out.println(MessageFormat.format("\n\t{0} is no longer stunned.", getTarget().getName()));
        getTarget().getStatuses().put("Stunned", false);
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
