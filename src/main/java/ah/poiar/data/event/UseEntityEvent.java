package ah.poiar.data.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Entity;

@Data
@AllArgsConstructor
public final class UseEntityEvent {
    private Entity target;
    private boolean attack;
    private int entityId;
    private boolean cancelled;
}
