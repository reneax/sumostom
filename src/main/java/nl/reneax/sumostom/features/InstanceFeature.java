package nl.reneax.sumostom.features;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.InstanceEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class InstanceFeature {
    private final List<EventListener<? extends InstanceEvent>> listeners = new ArrayList<>();

    protected <T extends InstanceEvent> void addListener(EventListener<T> listener) {
        listeners.add(listener);
    }

    public void hook(EventNode<? super InstanceEvent> node) {
        listeners.forEach(node::addListener);
    }

    public void unhook(EventNode<? super InstanceEvent> node) {
        listeners.forEach(node::removeListener);
    }
}
