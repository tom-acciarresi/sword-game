package it.unicam.cs.mpgc.rpg130730.environment;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Level;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public class RoomTransition {
    private static RoomTeleporter roomTeleporter;

    public RoomTransition(Vector2 fromCoords, Level fromRoom, Vector2 toCoords, Level toRoom,
            boolean bidirectional) {
        roomTeleporter = new RoomTeleporter(fromCoords, fromRoom, toCoords, toRoom, bidirectional);
    }

    public record RoomTeleporter(Vector2 fromCoords, Level fromRoom, Vector2 toCoords, Level toRoom,
            boolean bidirectional) {
    }
}
