package create_machinery.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import create_machinery.entity.HalfTrack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class HalfTrackControlsUpdate extends Message {
    private final boolean drilling;

    public HalfTrackControlsUpdate(boolean drilling) {
        this.drilling = drilling;
    }

    public HalfTrackControlsUpdate(FriendlyByteBuf b) {
        drilling = b.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeBoolean(drilling);
    }

    @Override
    public void receive(Player e) {
        if (e.getRootVehicle() instanceof HalfTrack entity) {
            entity.drilling = drilling;
        }
    }
}
