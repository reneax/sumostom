package nl.reneax.sumostom.features;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import nl.reneax.sumostom.Server;

public interface GlobalFeature {
    void hook(Server server, EventNode<? super Event> node);
}
