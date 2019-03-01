package combat.effects;

public class StunEffect extends StatusEffect {
    public StunEffect(int duration) {
        super("Stun", duration, "Stunned", false);
    }
}
