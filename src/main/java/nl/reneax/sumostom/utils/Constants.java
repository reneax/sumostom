package nl.reneax.sumostom.utils;

import net.minestom.server.coordinate.Pos;
import java.util.Arrays;
import java.util.List;

public class Constants {
    // TODO: change this to a YAML configuration file.
    public static final String SERVER_HOST = "0.0.0.0";
    public static final int SERVER_PORT = 25565;
    public static final int DEATH_POSITION_Y = -7;
    public static final List<Pos> PLAYER_POSITIONS = Arrays.asList(
            new Pos(5.0, 1, 0.5),
            new Pos(5.0, 1, 9.5).withYaw(-180)
    );
}
