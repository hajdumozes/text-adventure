package combat.skills.ownable;

import attributes.ownable.DamageBonus;
import characters.Character;
import combat.Effect;
import combat.skills.Skill;

public class Rage extends Skill {

    public Rage(Character owner) {
        super("Rage", "Increases damage by 2 for 3 turns.",
                1, owner);
    }


    public void useSkill(Character target) {
        getOwner().getDamageBonus().setCurrentValue(getOwner().getDamageBonusValue() + 2);
        getOwner().addToEffects(new Effect(new DamageBonus(), "Rage", 2, 3));
        decreaseSkillUsage();
    }
}
