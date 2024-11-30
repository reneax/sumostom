package nl.reneax.sumostom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.InstanceManager;
import nl.reneax.sumostom.managers.FeatureManager;
import nl.reneax.sumostom.managers.GameManager;
import nl.reneax.sumostom.utils.Constants;

public class Server {
    private final MinecraftServer minecraftServer = MinecraftServer.init();
    private final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
    private final GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    private final FeatureManager featureManager = new FeatureManager();
    private final GameManager gameManager = new GameManager(this.featureManager, this.instanceManager);

    /**
     * Get the feature manager instance of this server.
     * @return FeatureManager
     */
    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    /**
     * Get the game manager instance of this server.
     * @return GameManager
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Get the instance manager of this server.
     * @return InstanceManager
     */
    public InstanceManager getInstanceManager() {
        return instanceManager;
    }

    /**
     * Start the server
     * @param host The address to use
     * @param port The port to use
     */
    public void start(String host, int port) {
        featureManager.registerGlobalFeatures(this, this.globalEventHandler);
        minecraftServer.start(host, port);
    }
}
