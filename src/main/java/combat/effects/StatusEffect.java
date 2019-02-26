package combat.effects;

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
}
