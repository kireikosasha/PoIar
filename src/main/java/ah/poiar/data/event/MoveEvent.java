package ah.poiar.data.event;

import ah.poiar.data.generic.PoIarPlayer;
import ah.poiar.util.math.vectors.Vec3;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public final class MoveEvent {
    private PoIarPlayer profile;
    private Location from;
    private Location to;

    public Vec3 getDelta() {
        return new Vec3(
                to.getX() - from.getX(),
                to.getY() - from.getY(),
                to.getZ() - from.getZ()
        );
    }
}
