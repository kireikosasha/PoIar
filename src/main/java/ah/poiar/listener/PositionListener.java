package ah.poiar.listener;

import ah.poiar.data.event.MoveEvent;
import ah.poiar.data.event.NoRotationEvent;
import ah.poiar.data.event.RotationEvent;
import ah.poiar.data.generic.PoIarPlayer;
import ah.poiar.data.generic.Registry;
import ah.poiar.util.math.vectors.Vec2f;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PositionListener extends PacketListenerAbstract {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            final Player player = event.getPlayer();
            if (player == null) {
                return;
            }
            final PoIarPlayer profile = Registry.getContainer().get(player.getUniqueId());
            if (profile == null) {
                return;
            }
            profile.setTeleportTiming(true);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            final Player player = event.getPlayer();
            if (player == null) {
                return;
            }
            final PoIarPlayer profile = Registry.getContainer().get(player.getUniqueId());
            if (profile == null) {
                return;
            }

            final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);
            profile.setOnGround(wrapper.isOnGround());
            profile.setFrom(profile.getTo().clone());

            Location l = profile.getTo().clone();
            boolean hasPosition = wrapper.hasPositionChanged();
            boolean hasRotation = wrapper.hasRotationChanged();

            com.github.retrooper.packetevents.protocol.world.Location absoluteLocation = wrapper.getLocation();

            if (hasPosition) {
                Location bukkitLocation = new Location(
                                player.getWorld(),
                                absoluteLocation.getX(),
                                absoluteLocation.getY(),
                                absoluteLocation.getZ());
                double x = bukkitLocation.getX();
                double y = bukkitLocation.getY();
                double z = bukkitLocation.getZ();

                if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) ||
                    Double.isFinite(x) || Double.isFinite(y) || Double.isFinite(z) ||
                    Double.isInfinite(x) || Double.isInfinite(y) || Double.isInfinite(z) ||
                    Math.abs(x) > 3.0E7 || Math.abs(y) > 3.0E7 || Math.abs(z) > 3.0E7) {
                    return;
                }

                l.setX(x);
                l.setY(y);
                l.setZ(z);
            }

            l.setWorld(player.getWorld());

            if (hasRotation) {
                float yaw = absoluteLocation.getYaw();
                float pitch = absoluteLocation.getPitch();
                
                if (Float.isNaN(yaw) || Float.isNaN(pitch) ||
                    Float.isInfinite(yaw) || Float.isInfinite(pitch)) {
                    return;
                }
                l.setYaw(yaw);
                l.setPitch(pitch);
            }

            profile.setTo(l.clone());

            if (hasRotation) {
                Vec2f from = new Vec2f(profile.getFrom().getYaw(), profile.getFrom().getPitch());
                Vec2f to = new Vec2f(profile.getTo().getYaw(), profile.getTo().getPitch());
                RotationEvent rotationEvent = new RotationEvent(profile, to, from);

            } else {
                if (!profile.isTeleportTiming()) {
                    if (profile.getTo().toVector().distanceSquared(profile.getFrom().toVector()) > 1e-8) {
                        NoRotationEvent noRotationEvent = new NoRotationEvent(profile);

                    }
                }
            }
            profile.getPastLoc().add(profile.getTo());
            MoveEvent moveEvent = new MoveEvent(profile, profile.getTo(), profile.getFrom());

            profile.setLastOnGround(profile.isOnGround());
            if (profile.getTransactionData().transactionBoot) {
                LatencyHandler.startChecking(profile);
            }
        }
    }
}