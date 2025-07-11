package ah.poiar.data.generic;

import ah.poiar.util.math.types.EvictingList;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@Data
public class PoIarPlayer {
    private final Player player;
    private Location to = null, from = null;
    private boolean onGround = false, lastOnGround = false, isSprinting = false, teleportTiming = false;
    private final List<Location> pastLoc = new EvictingList<>(20);
    private final TransactionData transactionData = new TransactionData();

    public PoIarPlayer(Player player) {
        this.player = player;
    }
}
