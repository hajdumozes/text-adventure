package combat.skills.ownable;

import app.SkillManagement;
import characters.Character;
import combat.DistanceBased;
import combat.Targetable;
import combat.skills.SkillWithCountDown;

import java.text.MessageFormat;


public class StrikeOfLight extends SkillWithCountDown implements Targetable, DistanceBased {
    public StrikeOfLight(Character owner) {
        super("Strike of Light", "Stun target for 2 turns.",
                1, owner,
                2);
    }

    public void useSkill(Character target) {
        target.getStatuses().put("Stunned", true);
        target.getSkillWithCountDowns().add(this);
        new SkillManagement().decreaseSkillUsage(this);
        setTarget(target);
    }

    @Override
    public void activateCountdownSkill() {
        nullifyStun();
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
