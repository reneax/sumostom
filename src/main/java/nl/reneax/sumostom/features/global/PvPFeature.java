package nl.reneax.sumostom.features.global;

import io.github.togar2.pvp.MinestomPvP;
import io.github.togar2.pvp.feature.CombatFeatures;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import nl.reneax.sumostom.Server;
import nl.reneax.sumostom.features.GlobalFeature;
import nl.reneax.sumostom.features.InstanceFeature;

public class PvPFeature implements GlobalFeature {
    @Override
    public void hook(Server server, EventNode<? super Event> node) {
        MinestomPvP.init();
        node.addChild(CombatFeatures.legacyVanilla().createNode());
    }
}
