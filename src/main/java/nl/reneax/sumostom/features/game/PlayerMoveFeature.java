package nl.reneax.sumostom.features.game;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import nl.reneax.sumostom.enums.GameStatus;
import nl.reneax.sumostom.features.InstanceFeature;
import nl.reneax.sumostom.objects.GameRoom;
import nl.reneax.sumostom.objects.SumoPlayer;
import nl.reneax.sumostom.utils.Constants;

public class PlayerMoveFeature extends InstanceFeature {
    public PlayerMoveFeature(GameRoom gameRoom) {
        addListener(EventListener.of(PlayerMoveEvent.class, (event) -> {
            if(event.getNewPosition().y() < Constants.DEATH_POSITION_Y) {
                if(gameRoom.getGameStatus() == GameStatus.FINISHED) return;

                Player player = event.getEntity();
                SumoPlayer sumoPlayer = gameRoom.getSumoPlayer(player);
                sumoPlayer.setDead(true);

                gameRoom.checkFinished();
            }
        }));
    }
}
