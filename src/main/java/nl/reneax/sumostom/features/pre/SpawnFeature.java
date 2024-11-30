package nl.reneax.sumostom.features.pre;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import nl.reneax.sumostom.features.InstanceFeature;
import nl.reneax.sumostom.objects.GameRoom;
import nl.reneax.sumostom.utils.Constants;

public class SpawnFeature extends InstanceFeature {
    public SpawnFeature(GameRoom gameRoom) {
        addListener(EventListener.of(PlayerSpawnEvent.class, event -> {
            gameRoom.broadcast(String.format("%s heeft het spel betreden (%s/%s)",
                    event.getPlayer().getUsername(),
                    gameRoom.getPlayers().size(),
                    Constants.PLAYER_POSITIONS.size()
            ));

            // if the game is full, start the game.
            if (!gameRoom.isAvailable()) {
                gameRoom.start();
            }
        }));
    }
}
