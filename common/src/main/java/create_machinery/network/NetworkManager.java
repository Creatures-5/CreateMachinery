package create_machinery.network;

import create_machinery.network.c2s.BambooBeeConfigurationUpdate;

public interface NetworkManager {
    void handleBambooBeeConfiguration(BambooBeeConfigurationUpdate bambooBeeConfigurationUpdate);
}
