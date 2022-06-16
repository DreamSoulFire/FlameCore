package dream.soulflame.flamecore.enums;

public enum Conditions {
    PAPI("papi"),
    PERM("perm");

    private final String args;

    Conditions(String args) {
        this.args = args;
    }

    public String getArgs() {
        return args;
    }
}
