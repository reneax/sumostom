package nl.reneax.sumostom.features.global;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import nl.reneax.sumostom.Server;
import nl.reneax.sumostom.features.GlobalFeature;
import nl.reneax.sumostom.managers.GameManager;
import nl.reneax.sumostom.objects.GameRoom;
import nl.reneax.sumostom.objects.SumoPlayer;
import nl.reneax.sumostom.utils.Constants;

public class PlayerInitializeFeature implements GlobalFeature {
    @Override
    public void hook(Server server, EventNode<? super Event> node) {
        node.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            SumoPlayer sumoPlayer = new SumoPlayer(event.getPlayer());
            GameManager gameManager = server.getGameManager();
            GameRoom gameRoom = gameManager.findAvailableGame();

            if (gameRoom == null) gameRoom = gameManager.createGame();

            event.setSpawningInstance(gameRoom.getInstanceContainer());
            gameRoom.addPlayer(sumoPlayer);
        });
    }
}
