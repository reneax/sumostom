package nl.reneax.sumostom.managers;

import net.minestom.server.instance.InstanceManager;
import nl.reneax.sumostom.objects.GameRoom;

import java.util.ArrayList;

public class GameManager {
    private final ArrayList<GameRoom> gameRooms = new ArrayList<>();
    private final FeatureManager featureManager;
    private final InstanceManager instanceManager;

    public GameManager(FeatureManager featureManager, InstanceManager instanceManager) {
        this.featureManager = featureManager;
        this.instanceManager = instanceManager;
    }

    /**
     * Finds a game that is available to join.
     * @return GameRoom
     */
    public GameRoom findAvailableGame() {
        return this.gameRooms.stream().filter(GameRoom::isAvailable).findFirst().orElse(null);
    }

    /**
     * Removes a game room from the manager.
     * @param gameRoom The game room to remove.
     */
    public void removeGame(GameRoom gameRoom) {
        this.gameRooms.remove(gameRoom);
    }

    /**
     * Create a new game room.
     * @return GameRoom
     */
    public GameRoom createGame() {
        GameRoom gameRoom = new GameRoom(
                this.featureManager,
                this,
                this.instanceManager.createInstanceContainer()
        );

        gameRooms.add(gameRoom);

        return gameRoom;
    }

}
