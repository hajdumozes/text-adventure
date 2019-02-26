package combat.skills.ownable;

import attributes.ownable.ArmorClass;
import attributes.ownable.AttackBonus;
import characters.Character;
import combat.Effect;
import combat.skills.Skill;


public class Berserk extends Skill {
    public Berserk(Character owner) {
        super("Berserk", "Increase attacking rolls by 5, but decreases AC by 5 for 3 turns.",
                1, owner);
    }

    public void useSkill(Character target) {
        getOwner().getAttackBonus().setCurrentValue(getOwner().getAttackBonusValue() + 5);
        getOwner().addToEffects(new Effect(new AttackBonus(), "increased attack of Berserk", 5, 3));
        getOwner().getArmorClass().setCurrentValue(getOwner().getArmorClassValue() - 5);
        getOwner().addToEffects(new Effect(new ArmorClass(), "decreased defense of Berserk", -5, 3));
        decreaseSkillUsage();
    }
}
