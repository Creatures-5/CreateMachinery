package create_machinery;

import immersive_aircraft.cobalt.network.NetworkHandler;
import create_machinery.network.c2s.BambooBeeConfigurationUpdate;
import create_machinery.network.c2s.SonarMessage;
import create_machinery.network.c2s.TunnelDiggerControlsUpdate;

public class Messages {
    public static void loadMessages() {
        NetworkHandler.registerMessage(Common.MOD_ID, TunnelDiggerControlsUpdate.class, TunnelDiggerControlsUpdate::new);
        NetworkHandler.registerMessage(Common.MOD_ID, BambooBeeConfigurationUpdate.class, BambooBeeConfigurationUpdate::new);
        NetworkHandler.registerMessage(Common.MOD_ID, SonarMessage.class, SonarMessage::new);
    }
}
