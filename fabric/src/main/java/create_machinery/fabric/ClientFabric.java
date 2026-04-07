package create_machinery.fabric;

import create_machinery.Client;
import create_machinery.Renderer;
import create_machinery.client.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public final class ClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Renderer.bootstrap();
        Client.init();

        KeyBindings.list.forEach(KeyBindingHelper::registerKeyBinding);
    }
}
