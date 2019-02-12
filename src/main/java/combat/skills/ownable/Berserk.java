package combat.skills.ownable;

import attributes.ownable.ArmorClass;
import attributes.ownable.AttackBonus;
import characters.Character;
import combat.Effect;
import combat.skills.Skill;

import static app.SkillManagement.findMethod;
import static app.SkillManagement.useSkill;

public class Berserk extends Skill {
    public Berserk(Character owner) {
        super("Berserk", "Increase attacking rolls by 5, but decreases AC by 5 for 3 turns.",
                findMethod(Berserk.class, "berserk", null), 1, owner);
    }

    public void berserk() {
        getOwner().getAttackBonus().setCurrentValue(getOwner().getAttackBonusValue() + 5);
        getOwner().addToEffects(new Effect(new AttackBonus(), "increased attack of Berserk", 5, 3));
        getOwner().getArmorClass().setCurrentValue(getOwner().getArmorClassValue() - 5);
        getOwner().addToEffects(new Effect(new ArmorClass(), "decreased defense of Berserk", -5, 3));
        useSkill(this);
    }
}
