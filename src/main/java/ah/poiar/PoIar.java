package ah.poiar;

import ah.poiar.service.BootService;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("ALL")
public final class PoIar extends JavaPlugin {

    private static PoIar instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onLoad() {
        BootService.init();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        // TODO: Server files delete logic
    }

    public static PoIar getInstance() {
        return instance;
    }
}
