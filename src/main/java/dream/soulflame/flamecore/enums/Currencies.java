package dream.soulflame.flamecore.enums;

public enum Currencies {
    MONEY("money"),
    POINTS("points");
    private final String args;
    Currencies(String args) {
        this.args = args;
    }
    public String getArgs() {
        return args;
    }
}
