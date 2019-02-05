package combat;

public class Target {
    private boolean targetable;
    private boolean enemyTarget;

    public Target(boolean targetable, boolean friendly) {
        this.targetable = targetable;
        this.enemyTarget = friendly;
    }

    public boolean isTargetable() {
        return targetable;
    }

    public void setTargetable(boolean targetable) {
        this.targetable = targetable;
    }

    public boolean isEnemyTarget() {
        return enemyTarget;
    }

    public void setEnemyTarget(boolean enemyTarget) {
        this.enemyTarget = enemyTarget;
    }
}
