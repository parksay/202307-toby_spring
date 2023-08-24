package basics.tobyspring5.chapter51;

public enum Level512 {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);
    // 이게 생성자에다가 다른 Level 객체를 넣어야 하는 상황이야
    // 그러다 보니 순서가 또 중요해.
    // BASIC 생정자에는 SILVER 를 넣고 있으니까, 먼저 SILVER 부터 선언해야 하고,
    // SILVER 생성자에는 GOLD 가 필요하니까 SILVER 보다 GOLD 가 먼저 나와야 해.


    private final int value;
    private final Level512 next;

    Level512(int value, Level512 next) {
        this.value = value;
        this.next = next;
    }

    public int intVal() {
        return this.value;
    }

    public Level512 nextLevel() {
        return this.next;
    }

    public static Level512 valueOf(int value) {
        switch(value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown Level value: " + value);
        }
    }

}
