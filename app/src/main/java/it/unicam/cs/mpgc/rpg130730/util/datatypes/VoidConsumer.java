package it.unicam.cs.mpgc.rpg130730.util.datatypes;

public interface VoidConsumer {
    default void run() {
        throw new IllegalArgumentException("null function called");
    }
}
