package nl.reneax.sumostom.features.pre;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import nl.reneax.sumostom.features.InstanceFeature;
import nl.reneax.sumostom.objects.GameRoom;

public class FreezeFeature extends InstanceFeature {
    public FreezeFeature(GameRoom gameRoom) {
        addListener(EventListener.of(PlayerMoveEvent.class, event -> {
            event.setCancelled(true);
        }));
    }

}
