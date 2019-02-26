package combat.skills.ownable;

import app.Movement;
import characters.Character;
import combat.Position;
import combat.skills.Skill;
import objects.Trap;

import java.util.List;

import static app.Battlefield.BATTLEFIELD;

public class SetTrap extends Skill {
    public SetTrap(Character owner) {
        super("Set Trap", "Set a trap 1 tile away from your character", 1, owner);
    }


    @Override
    public void useSkill(Character character) {
        Movement movement = new Movement();
        List<Position> emptySpaces = movement.findAllPossibleRoutesByGivenDistance(getOwner(), 1);
        movement.printPositions(emptySpaces);
        Position selectedPosition = movement.getPositionFromUser();
        BATTLEFIELD[selectedPosition.getRow()][selectedPosition.getColumn()] = new Trap(1, 4, 3);
        decreaseSkillUsage();
    }
}
