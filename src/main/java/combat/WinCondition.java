package combat;

import characters.Character;

import java.util.List;

import static app.Combat.getDeadCharactersBySide;

public class WinCondition {
    private int amount;
    private Class characterClass;
    private boolean playersParty;
    private boolean winIfHappens;

    public WinCondition(int amount, Class characterClass, boolean playersParty, boolean winIfHappens) {
        this.amount = amount;
        this.characterClass = characterClass;
        this.playersParty = playersParty;
        this.winIfHappens = winIfHappens;
    }

    public int getAmount() {
        return amount;
    }

    public Class getCharacterClass() {
        return characterClass;
    }

    public boolean PlayersParty() {
        return playersParty;
    }

    public boolean WinIfHappens() {
        return winIfHappens;
    }

    public boolean determine() {
        List<Character> deadCharactersBySide = getDeadCharactersBySide(playersParty);
        int deadCharactersOfClass = 0;
        for (Character character : deadCharactersBySide) {
            if (character.getClass() == characterClass) {
                deadCharactersOfClass++;
            }
        }
        return deadCharactersOfClass >= amount;
    }
}
