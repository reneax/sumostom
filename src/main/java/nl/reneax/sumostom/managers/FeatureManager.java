package nl.reneax.sumostom.managers;

import net.minestom.server.event.GlobalEventHandler;
import nl.reneax.sumostom.Server;
import nl.reneax.sumostom.features.InstanceFeature;
import nl.reneax.sumostom.features.both.LeaveFeature;
import nl.reneax.sumostom.features.game.DeathFeature;
import nl.reneax.sumostom.features.global.*;
import nl.reneax.sumostom.features.global.DamageFeature;
import nl.reneax.sumostom.features.pre.FreezeFeature;
import nl.reneax.sumostom.features.pre.SpawnFeature;
import nl.reneax.sumostom.objects.GameRoom;

import java.util.LinkedList;
import java.util.List;

public class FeatureManager {
    /**
     * Unhooks currently registered features from a game room.
     * @param gameRoom The game room to unregister features from.
     */
    public void unregisterFeatures(GameRoom gameRoom) {
        LinkedList<InstanceFeature> features = gameRoom.getFeatures();

        while (!features.isEmpty()) {
            InstanceFeature feature = features.poll();
            feature.unhook(gameRoom.getEventNode());
        }
    }

    /**
     * Register features that are used before the match starts.
     * @param gameRoom The game room to add pre features to.
     */
    public void registerPreFeatures(GameRoom gameRoom) {
        List.of(
                new FreezeFeature(gameRoom),
                new SpawnFeature(gameRoom),
                // pre & game features
                new LeaveFeature(gameRoom)
        ).forEach(feature -> {
            feature.hook(gameRoom.getEventNode());
            gameRoom.getFeatures().add(feature);
        });
    }

    /**
     * Register features that are used while the match is going.
     * @param gameRoom The game room to add game features to.
     */
    public void registerGameFeatures(GameRoom gameRoom) {
        List.of(
                new DeathFeature(gameRoom),
                // pre & game features
                new LeaveFeature(gameRoom)
        ).forEach(feature -> {
            feature.hook(gameRoom.getEventNode());
            gameRoom.getFeatures().add(feature);
        });
    }

    /**
     * Register features that are server wide and don't need game room context.
     * @param server The server object.
     * @param globalEventHandler The globalEventHandler from the server.
     */
    public void registerGlobalFeatures(Server server, GlobalEventHandler globalEventHandler) {
        List.of(
                new PvPFeature(),
                new TabTextFeature(),
                new PlayerInitializeFeature(),
                new BlockBreakFeature(),
                new DamageFeature()
        ).forEach(globalFeature -> globalFeature.hook(server, globalEventHandler));
    }

}
