package combat.skills.ownable;

import app.CharacterActions;
import app.Main;
import attributes.ownable.DamageBonus;
import characters.Character;
import combat.DistanceBased;
import combat.Targetable;
import combat.effects.AttributeEffect;
import combat.skills.Skill;

public class Bite extends Skill implements Targetable, DistanceBased {
    public Bite(Character owner) {
        super("Bite", "2d4 +2 damage, +4 to hit",
                3, owner);
    }

    public void useSkill(Character target) {
        getOwner().getDamageBonus().setCurrentValue(getOwner().getDamageBonusValue() + 2);
        getOwner().addToEffects(new AttributeEffect(new DamageBonus(), "Bite", 2, 1));
        System.out.println("\tWolf bit!");
        int hitRoll = new Main().roll(1, 20) + 4;
        new CharacterActions().evaluateAttackRoll(hitRoll, getOwner(), target);
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
