package characters;

public class CharacterDiedException extends RuntimeException {
    private Character character;

    public CharacterDiedException(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
