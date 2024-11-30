package nl.reneax.sumostom.features.global;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import nl.reneax.sumostom.Server;
import nl.reneax.sumostom.features.GlobalFeature;

public class DamageFeature implements GlobalFeature {
    @Override
    public void hook(Server server, EventNode<? super Event> node) {
        node.addListener(EntityDamageEvent.class, event -> {
           event.getDamage().setAmount(0.1f);
        });
    }
}
