package nl.reneax.sumostom.features.global;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import nl.reneax.sumostom.Server;
import nl.reneax.sumostom.features.GlobalFeature;
import nl.reneax.sumostom.managers.GameManager;
import nl.reneax.sumostom.objects.GameRoom;
import nl.reneax.sumostom.objects.SumoPlayer;
import nl.reneax.sumostom.utils.Constants;

public class BlockBreakFeature implements GlobalFeature {
    @Override
    public void hook(Server server, EventNode<? super Event> node) {
        node.addListener(PlayerBlockBreakEvent.class, event -> {
           event.setCancelled(true);
        });
    }
}
