package ah.poiar.data.generic;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class Registry {
    @Getter
    private final Map<UUID, PoIarPlayer> container = new ConcurrentHashMap<>();
    @Getter
    private final Set<Player> enabledAlerts = new HashSet<>();
}
