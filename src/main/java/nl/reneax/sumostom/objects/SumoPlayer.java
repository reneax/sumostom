package nl.reneax.sumostom.objects;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import nl.reneax.sumostom.utils.Constants;

public class SumoPlayer {
    private final Player entity;
    private boolean isDead;

    public SumoPlayer(Player entity) {
        this.entity = entity;
        this.isDead = false;
    }

    /**
     * Returns the player entity associated with this object.
     * @return Player
     */
    public Player entity() {
        return entity;
    }

    /**
     * Returns if the player died.
     * @return boolean
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets the dead status and allow the player to spectate when dead.
     * @param isDead The new dead status
     */
    public void setDead(boolean isDead) {
        this.isDead = isDead;

        if (this.isDead) {
            // allow player to spectate the game.
            this.entity.teleport(this.entity.getRespawnPoint());
            this.entity.setGameMode(GameMode.SPECTATOR);
        }
    }
}
