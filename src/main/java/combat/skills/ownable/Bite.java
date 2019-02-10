package combat.skills.ownable;

import attributes.ownable.DamageBonus;
import characters.Character;
import combat.Status;
import combat.Targetable;
import combat.skills.Skill;

import static app.Main.roll;
import static app.SkillManagement.findMethod;
import static app.SkillManagement.useSkill;

public class Bite extends Skill implements Targetable {
    public Bite(Character owner) {
        super("Bite", "2d4 +2 damage, +4 to hit",
                findMethod(Bite.class, "bite", Character.class), 3, owner);
    }

    public void bite(Character target) {
        getOwner().getDamageBonus().setCurrentValue(getOwner().getDamageBonusValue() + 2);
        getOwner().addToStatuses(new Status(new DamageBonus(), "Bite", 2, 1));
        System.out.println("\tWolf bit!");
        int hitRoll = roll(1, 20) + 4;
        getOwner().evaluateAttackRoll(hitRoll, target);
        useSkill(this);
    }

    @Override
    public boolean isTargetOnPlayersSide() {
        return false;
    }
}
