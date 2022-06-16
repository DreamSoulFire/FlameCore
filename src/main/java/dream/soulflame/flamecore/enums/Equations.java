package dream.soulflame.flamecore.enums;

public enum Equations {
    LT("<"),
    LE("<="),
    EQ("="),
    NE("!="),
    GE(">="),
    GT(">");
    private final String args;
    Equations(String args) {
        this.args = args;
    }
    public String getArgs() {
        return args;
    }
}
