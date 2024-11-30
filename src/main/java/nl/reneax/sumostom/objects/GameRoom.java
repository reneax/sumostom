package nl.reneax.sumostom.objects;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.timer.TaskSchedule;
import nl.reneax.sumostom.enums.GameStatus;
import nl.reneax.sumostom.features.InstanceFeature;
import nl.reneax.sumostom.managers.FeatureManager;
import nl.reneax.sumostom.managers.GameManager;
import nl.reneax.sumostom.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameRoom {
    private final ArrayList<SumoPlayer> players = new ArrayList<>();
    private final LinkedList<InstanceFeature> features = new LinkedList<>();
    private final FeatureManager featureManager;
    private final GameManager gameManager;
    private final InstanceContainer instanceContainer;
    private GameStatus gameStatus = GameStatus.WAITING;
    private final EventNode<InstanceEvent> eventNode;

    public GameRoom(FeatureManager featureManager,
                    GameManager gameManager,
                    InstanceContainer instanceContainer) {
        this.gameManager = gameManager;
        this.featureManager = featureManager;
        this.instanceContainer = instanceContainer;
        this.eventNode = this.instanceContainer.eventNode();
        this.fixLightning();
        this.generateTerrain();
        this.registerFeatures(this.gameStatus);
    }

    /**
     * Broadcasts a message to every player in the game room.
     *
     * @param message The message to send
     */
    public void broadcast(String message) {
        this.getPlayers().forEach(player -> player.entity().sendMessage(message));
    }

    /**
     * Play a sound to every player in the game room.
     * @param sound The sound to play
     */
    public void playSound(Sound sound) {
        this.getPlayers().forEach(player -> player.entity().playSound(sound));
    }

    /**
     * Get the SumoPlayer object by player entity.
     *
     * @param player The player to get the sumo player object from
     * @return SumoPlayer
     */
    public SumoPlayer getSumoPlayer(Player player) {
        return this.getPlayers().stream().filter(sumoPlayer -> sumoPlayer.entity().equals(player))
                .findFirst().orElse(null);
    }

    /**
     * Get every feature currently in use by the game room.
     *
     * @return LinkedList<InstanceFeature>
     */
    public LinkedList<InstanceFeature> getFeatures() {
        return features;
    }

    /**
     * Get the sumo players that are currently online
     *
     * @return List<SumoPlayer>
     */
    public List<SumoPlayer> getPlayers() {
        return players.stream().filter(player -> player.entity().isOnline()).toList();
    }

    /**
     * Get the sumo players that are alive.
     *
     * @return List<SumoPlayer>
     */
    public List<SumoPlayer> getAlivePlayers() {
        return this.getPlayers().stream().filter(player -> !player.isDead()).toList();
    }

    /**
     * Get the instance container of the game room.
     *
     * @return InstanceContainer
     */
    public InstanceContainer getInstanceContainer() {
        return instanceContainer;
    }

    /**
     * Get the current game status of the game room.
     *
     * @return GameStatus
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Set the game status of the game room.
     *
     * @param gameStatus The new game status
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.registerFeatures(this.gameStatus);
    }

    /**
     * Checks if the game is over.
     */
    public void checkFinished() {
        List<SumoPlayer> alivePlayers = this.getAlivePlayers();

        if (alivePlayers.size() == 1) {
            finish(alivePlayers.getFirst());
        }
    }

    /**
     * Ends the game, announces the player that won, spawns effects and automatically cleans up the game room.
     *
     * @param sumoPlayer The sumo player that won the game.
     */
    public void finish(SumoPlayer sumoPlayer) {
        Player player = sumoPlayer.entity();

        this.setGameStatus(GameStatus.FINISHED);
        this.broadcast(String.format("%s heeft het spel gewonnen!", player.getUsername()));

        AtomicInteger countdown = new AtomicInteger(10);

        this.instanceContainer.scheduler().submitTask(() -> {
            int currentCount = countdown.getAndDecrement();

            if (currentCount == 0) {
                this.remove();
            } else {
                ParticlePacket particlePacket = new ParticlePacket(
                        Particle.TOTEM_OF_UNDYING,
                        player.getPosition().add(0, 3, 0),
                        new Pos(0.5, 0.5, 0.5),
                        2,
                        10
                );
                this.playSound(Sound.sound(SoundEvent.ENTITY_FIREWORK_ROCKET_BLAST,
                        Sound.Source.MASTER, 1.0f, 1.0f));
                this.getPlayers().forEach(loopPlayer -> loopPlayer.entity().sendPacket(particlePacket));
            }

            return currentCount > 0 ? TaskSchedule.seconds(1) : TaskSchedule.stop();
        });
    }

    /**
     * Starts a countdown and automatically sets game status as started.
     */
    public void start() {
        AtomicInteger countdown = new AtomicInteger(5);

        this.instanceContainer.scheduler().submitTask(() -> {
            int currentCount = countdown.getAndDecrement();

            // someone left the game during countdown.
            if (this.gameStatus == GameStatus.FINISHED) {
                return TaskSchedule.stop();
            }

            if (currentCount == 0) {
                setGameStatus(GameStatus.STARTED);
                this.broadcast("Het spel is begonnen. Veel succes!");
                this.playSound(Sound.sound(SoundEvent.ENTITY_PLAYER_LEVELUP,
                        Sound.Source.MASTER, 1.0f, 1.0f));
            } else {
                this.broadcast(String.format("Het spel start in %s seconden.", currentCount));
                this.playSound(Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_BELL,
                        Sound.Source.MASTER, 1.0f, 1.0f));
            }

            return currentCount > 0 ? TaskSchedule.seconds(1) : TaskSchedule.stop();
        });
    }

    /**
     * Immediately close the game and kick the players.
     */
    public void remove() {
        this.gameManager.removeGame(this);
        this.getPlayers().forEach(player -> {
            player.entity().kick("Verbind opnieuw om een nieuw spel te starten.");
        });
    }

    /**
     * Get the event node of the game room.
     *
     * @return EventNode<InstanceEvent>
     */
    public EventNode<InstanceEvent> getEventNode() {
        return eventNode;
    }

    /**
     * Unregisters current features and registers features based on game status.
     *
     * @param gameStatus The game status
     */
    private void registerFeatures(GameStatus gameStatus) {
        this.featureManager.unregisterFeatures(this);

        switch (gameStatus) {
            case WAITING:
                this.featureManager.registerPreFeatures(this);
                break;
            case STARTED:
                this.featureManager.registerGameFeatures(this);
                break;
        }
    }

    /**
     * Checks if the game is available for players to join.
     *
     * @return boolean
     */
    public boolean isAvailable() {
        return this.gameStatus == GameStatus.WAITING
                && Constants.PLAYER_POSITIONS.size() >= this.getPlayers().size() + 1;
    }

    /**
     * Get the spawn point for a player.
     *
     * @param player The player to get a spawn point for.
     * @return Pos
     */
    public Pos getSpawnPoint(SumoPlayer player) {
        return Constants.PLAYER_POSITIONS.get(players.indexOf(player));
    }

    /**
     * Add a player to the game room.
     *
     * @param player The player to add.
     */
    public void addPlayer(SumoPlayer player) {
        players.add(player);

        Player entity = player.entity();
        entity.setRespawnPoint(getSpawnPoint(player));
    }

    /**
     * Fixes the lightning issues by setting the chunk supplier to LightningChunk.
     */
    private void fixLightning() {
        this.instanceContainer.setChunkSupplier(LightingChunk::new);
    }

    /**
     * Generate the terrain for the game room.
     */
    private void generateTerrain() {
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                this.instanceContainer.setBlock(x, 0, z, Block.GRASS_BLOCK);
            }
        }
    }

}
