package basics.tobyspring5.chapter52;

public enum Level521 {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level521 next;

    Level521(int value, Level521 next) {
        this.value = value;
        this.next = next;
    }

    public int intVal() {
        return this.value;
    }

    public Level521 nextLevel() {
        return this.next;
    }

    public static Level521 valueOf(int value) {
        switch(value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown Level value: " + value);
        }
    }

}
