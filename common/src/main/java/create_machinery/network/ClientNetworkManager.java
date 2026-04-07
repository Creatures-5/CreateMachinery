package create_machinery.network;

import create_machinery.client.gui.screen.BambooBeeScreen;
import create_machinery.network.c2s.BambooBeeConfigurationUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientNetworkManager implements NetworkManager {
    private final Minecraft client;

    public ClientNetworkManager(Minecraft instance) {
        this.client = instance;
    }

    @Override
    public void handleBambooBeeConfiguration(BambooBeeConfigurationUpdate bambooBeeConfigurationUpdate) {
        if (client.level != null) {
            Entity entity = client.level.getEntity(bambooBeeConfigurationUpdate.getId());
            bambooBeeConfigurationUpdate.read(entity);
        }

        if (client.screen instanceof BambooBeeScreen screen) {
            screen.updateConfigurations();
        }
    }
}
