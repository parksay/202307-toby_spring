package basics.tobyspring5.chapter51;

public enum Level511 {
    BASIC(1), SILVER(2), GOLD(3);

    private final int value;

    Level511(int value) {
        this.value = value;
    }

    public int intVal() {
        return this.value;
    }

    public static Level511 valueOf(int value) {
        switch(value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown Level value: " + value);
        }
    }
}
