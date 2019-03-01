package combat.effects;

import characters.Character;

import java.util.Objects;

public class StatusEffect extends Effect {
    private String statusName;
    private boolean statusValue;

    public StatusEffect(String name, int duration, String statusName, boolean statusValue) {
        super(name, duration);
        this.statusName = statusName;
        this.statusValue = statusValue;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public boolean getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(boolean statusValue) {
        this.statusValue = statusValue;
    }

    public void increaseEffectDuration(Character target) {
        if (target.getEffects().contains(this)) {
            StatusEffect existent = (StatusEffect) target.getEffects().get(target.getEffects().indexOf(this));
            existent.setDuration(existent.getDuration() + this.getDuration());
        } else {
            target.getEffects().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEffect that = (StatusEffect) o;
        return statusValue == that.statusValue &&
                Objects.equals(statusName, that.statusName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusName, statusValue);
    }
}
