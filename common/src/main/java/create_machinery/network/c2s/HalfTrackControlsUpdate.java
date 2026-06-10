package create_machinery.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import create_machinery.entity.HalfTrack;
import create_machinery.entity.TunnelDigger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class HalfTrackControlsUpdate extends Message {
    private final int drillY;
    private final boolean drilling;

    public HalfTrackControlsUpdate(int drillY, boolean drilling) {
        this.drillY = drillY;
        this.drilling = drilling;
    }

    public HalfTrackControlsUpdate(FriendlyByteBuf b) {
        drillY = b.readInt();
        drilling = b.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(drillY);
        b.writeBoolean(drilling);
    }

    @Override
    public void receive(Player e) {
        if (e.getRootVehicle() instanceof HalfTrack entity) {
            entity.drillY = drillY;
            entity.drilling = drilling;
        }
    }
}
