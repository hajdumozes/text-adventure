package characters;

import combat.Position;
import combat.skills.ownable.Bite;
import combat.skills.ownable.Howl;
import items.Equipment.Equipment;
import items.Equipment.ownable.WolfClaw;

import static app.GeneralAI.letGeneralAIDecide;

public class Wolf extends Character {

    {
        setPosition(new Position(5, 6));
    }

    public Wolf() {
        super("Wolf", "Wolf", 11, 15, 5, 5,
                new Equipment(new WolfClaw(), new WolfClaw()), false);
        getSkills().add(new Bite(this));
        getSkills().add(new Howl(this));
    }

    @Override
    public void letAiDecide() {
        letGeneralAIDecide(this);
    }


}
