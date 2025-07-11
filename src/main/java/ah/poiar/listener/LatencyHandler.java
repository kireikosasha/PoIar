package ah.poiar.listener;

import ah.poiar.PoIar;
import ah.poiar.data.event.CTransactionEvent;
import ah.poiar.data.generic.PoIarPlayer;
import ah.poiar.data.generic.Registry;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class LatencyHandler extends PacketListenerAbstract {

    public static void startChecking(PoIarPlayer protocol) {
        protocol.getTransactionData().transactionId = -1939;
        protocol.getTransactionData().transactionBoot = false;
        sendTransaction(protocol, (short) protocol.getTransactionData().transactionId);
    }

    public static void sendTransaction(PoIarPlayer protocol, short id) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(protocol.getPlayer());
        if (user == null) {
            return;
        }

        ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(user, new WrapperPlayServerPing(id));
        } else {
            PacketEvents.getAPI().getPlayerManager().sendPacket(user, new WrapperPlayServerKeepAlive(id));
        }

        protocol.getTransactionData().transactionId--;
        if (protocol.getTransactionData().transactionId < -1945) {
            protocol.getTransactionData().transactionId = -1939;
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        final Player player = event.getPlayer();
        final PoIarPlayer protocol = Registry.getContainer().get(player.getUniqueId());
        if (protocol == null) {
            return;
        }

        long id = 0;
        boolean isLatencyPacket = true;
        PacketTypeCommon packetType = event.getPacketType();
        ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            if (packetType.equals(PacketType.Play.Client.PONG)) {
                id = new WrapperPlayClientPong(event).getId();
            } else {
                isLatencyPacket = false;
            }
        } else {
            if (packetType.equals(PacketType.Play.Client.KEEP_ALIVE)) {
                id = new WrapperPlayClientKeepAlive(event).getId();
            } else {
                isLatencyPacket = false;
            }
        }

        if (isLatencyPacket && id <= -1939 && id >= -1945) {
            protocol.getTransactionData().transactionPing = System.currentTimeMillis() - protocol.getTransactionData().transactionTime;
            protocol.getTransactionData().transactionLastTime = System.currentTimeMillis();
            protocol.getTransactionData().transactionSentKeep = false;
            new CTransactionEvent(protocol);

            Bukkit.getScheduler().runTaskLaterAsynchronously(PoIar.getInstance(),
                            () -> sendTransaction(protocol, (short) protocol.getTransactionData().transactionId), 10L);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon packetType = event.getPacketType();
        boolean isLatencyPacket = packetType.equals(PacketType.Play.Server.PING)
                        || packetType.equals(PacketType.Play.Server.KEEP_ALIVE);
        if (isLatencyPacket) {
            if (!(event.getPlayer() instanceof Player)) {
                return;
            }
            final Player player = event.getPlayer();
            final PoIarPlayer protocol = Registry.getContainer().get(player.getUniqueId());
            if (protocol == null) {
                return;
            }
            protocol.getTransactionData().transactionSentKeep = true;
            protocol.getTransactionData().transactionTime = System.currentTimeMillis();
        }
    }
}