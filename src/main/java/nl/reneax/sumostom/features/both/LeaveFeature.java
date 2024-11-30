package nl.reneax.sumostom.features.both;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import nl.reneax.sumostom.enums.GameStatus;
import nl.reneax.sumostom.features.InstanceFeature;
import nl.reneax.sumostom.objects.GameRoom;

public class LeaveFeature extends InstanceFeature {
    public LeaveFeature(GameRoom gameRoom) {
        addListener(EventListener.of(PlayerDisconnectEvent.class, (event) -> {
            // if there are no more players, remove the game.
            if(gameRoom.getPlayers().isEmpty()) {
                gameRoom.remove();
                return;
            }

            if(gameRoom.getGameStatus() != GameStatus.FINISHED) {
                gameRoom.checkFinished();
            }
        }));
    }
}
