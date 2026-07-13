package it.unicam.cs.mpgc.rpg130730.environment;

public enum Level {
    ROOM_1("level1.dat"),
    ROOM_2("level2.dat"),
    ROOM_3("level3.dat"),
    ROOM_4("level4.dat"),
    ROOM_5("level5.dat");

    private final String filename;

    private Level(String filename) {
        this.filename = filename;
    }

    public String filename() {
        return filename;
    }
}
