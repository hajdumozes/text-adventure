package combat.skills.ownable;

import attributes.ownable.DamageBonus;
import characters.Character;
import combat.Effect;
import combat.skills.Skill;

import static app.SkillManagement.findMethod;
import static app.SkillManagement.useSkill;

public class Rage extends Skill {

    public Rage(Character owner) {
        super("Rage", "Increases damage by 2 for 3 turns.",
                findMethod(Rage.class, "rage", null), 1, owner);
    }


    public void rage() {
        getOwner().getDamageBonus().setCurrentValue(getOwner().getDamageBonusValue() + 2);
        getOwner().addToEffects(new Effect(new DamageBonus(), "Rage", 2, 3));
        useSkill(this);
    }
}
