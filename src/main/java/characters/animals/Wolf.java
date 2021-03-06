package characters.animals;

import app.GeneralAI;
import characters.Character;
import combat.skills.ownable.Bite;
import combat.skills.ownable.Howl;
import items.Equipment.Equipment;
import items.Equipment.ownable.WolfClaw;

public class Wolf extends Animal {

    public Wolf() {
        this(false, null, "Enemy Wolf");
    }

    public Wolf(boolean isFriendly, Character owner, String givenName) {
        super("Wolf", givenName, 11, 15, 5, 5,
                new Equipment(new WolfClaw(), new WolfClaw()), isFriendly, owner);
        getSkills().add(new Bite(this));
        getSkills().add(new Howl(this));
    }

    @Override
    public void letAiDecide() {
        GeneralAI ai = new GeneralAI();
        ai.letGeneralAIDecide(this);
    }


}
