package ah.poiar.data.event;

import com.github.retrooper.packetevents.event.PacketEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CPacketEvent {
    private PacketEvent packetEvent;
}
