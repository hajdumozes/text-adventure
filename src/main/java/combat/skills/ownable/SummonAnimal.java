package combat.skills.ownable;

import app.AttackEvaluation;
import app.Main;
import characters.Character;
import characters.animals.Animal;
import characters.animals.Wolf;
import combat.skills.Skill;

import java.util.ArrayList;

public class SummonAnimal extends Skill {

    public SummonAnimal(Character owner) {
        super("Summon Animal", "Summons a companion", 1, owner);
    }

    @Override
    public void useSkill(Character character) {
        for (Character characterAlive : new AttackEvaluation().getCharactersFromSelectedSide(getOwner().isFriendly())) {
            if (characterAlive instanceof Animal && ((Animal) characterAlive).getOwner() == getOwner()) {
                Main.SUMMONABLE_CHARACTERS.add(characterAlive);
                Main.CHARACTERS_ALIVE.remove(characterAlive);
            }
        }
        for (Character summonableCharacter : new ArrayList<>(Main.SUMMONABLE_CHARACTERS)) {
            if (summonableCharacter instanceof Wolf && ((Wolf) summonableCharacter).getOwner() == getOwner()) {
                Main.CHARACTERS_ALIVE.add(summonableCharacter);
                Main.SUMMONABLE_CHARACTERS.remove(summonableCharacter);
                ((Wolf) summonableCharacter).adjustPositionToSummoner(summonableCharacter, getOwner());
            }
        }
        decreaseSkillUsage();
    }
}
