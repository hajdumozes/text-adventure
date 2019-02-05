package combat;

public class Target {
    private boolean targetable;
    private boolean isTargetOnPlayersSide;

    public Target(boolean targetable, boolean isTargetOnPlayersSide) {
        this.targetable = targetable;
        this.isTargetOnPlayersSide = isTargetOnPlayersSide;
    }

    public boolean isTargetable() {
        return targetable;
    }

    public void setTargetable(boolean targetable) {
        this.targetable = targetable;
    }

    public boolean isTargetOnPlayersSide() {
        return isTargetOnPlayersSide;
    }

    public void setTargetOnPlayersSide(boolean targetOnPlayersSide) {
        this.isTargetOnPlayersSide = targetOnPlayersSide;
    }
}
