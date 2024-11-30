package nl.reneax.sumostom.features.global;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import nl.reneax.sumostom.Server;
import nl.reneax.sumostom.features.GlobalFeature;
import nl.reneax.sumostom.utils.ChatUtils;

public class TabTextFeature implements GlobalFeature {
    @Override
    public void hook(Server server, EventNode<? super Event> node) {
        node.addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().sendPlayerListHeaderAndFooter(
                    Component.text(ChatUtils.format("&7      Welkom op      ")),
                    Component.text(ChatUtils.format("&9      Sumostom       "))
            );
        });
    }
}
