package basics.tobyspring6.chapter61;

public enum Level611 {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level611 next;

    Level611(int value, Level611 next) {
        this.value = value;
        this.next = next;
    }

    public int intVal() {
        return this.value;
    }

    public Level611 nextLevel() {
        return this.next;
    }

    public static Level611 valueOf(int value) {
        switch(value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown Level value: " + value);
        }
    }

}
