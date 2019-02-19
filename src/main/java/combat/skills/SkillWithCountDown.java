package combat.skills;

import characters.Character;

public abstract class SkillWithCountDown extends Skill {
    private int countdown;

    public SkillWithCountDown(String name, String description, int usagePerBattle, Character owner,
                              int countdown) {
        super(name, description, usagePerBattle, owner);
        this.countdown = countdown;
    }

    public abstract void activateCountdownSkill();

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

}
