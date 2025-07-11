package ah.poiar.data.event;

import ah.poiar.data.generic.PoIarPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class NoRotationEvent {
    private PoIarPlayer profile;
}
