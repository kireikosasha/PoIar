package ah.poiar.service;

import ah.poiar.PoIar;
import ah.poiar.listener.LatencyHandler;
import ah.poiar.listener.PositionListener;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.experimental.UtilityClass;

@SuppressWarnings("ALL")
@UtilityClass
public class BootService {
    private final PacketListenerCommon[] listeners = new PacketListenerCommon[]{
                    new PositionListener(), new LatencyHandler()
    };
    public void init() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(PoIar.getInstance()));
        PacketEvents.getAPI().getSettings()
                        .fullStackTrace(true)
                        .kickOnPacketException(true)
                        .checkForUpdates(false)
                        .reEncodeByDefault(false)
                        .debug(false);
        PacketEvents.getAPI().init();
        // init listeners
        for (PacketListenerCommon listener : listeners) {
            PacketEvents.getAPI().getEventManager().registerListener(listener);
        }
    }
}
