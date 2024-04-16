package spring.ch5_service_abstraction;

import java.util.Arrays;

public enum Level {
    GOLD(3, null),
    SILVER(2, Level.GOLD),
    BASIC(1, Level.SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int getValue() {
        return value;
    }

    public static Level valueOf(int value) {
        return Arrays.stream(Level.values())
                .filter(level -> level.value == value)
                .findFirst()
                .orElse(null);
    }

    public Level nextLevel() {
        return next;
    }
}
