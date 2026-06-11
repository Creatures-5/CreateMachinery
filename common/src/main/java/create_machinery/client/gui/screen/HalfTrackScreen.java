package create_machinery.client.gui.screen;

import immersive_aircraft.client.gui.VehicleScreen;
import immersive_aircraft.screen.VehicleScreenHandler;
import create_machinery.Common;
import create_machinery.client.KeyBindings;
import create_machinery.entity.HalfTrack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class HalfTrackScreen extends VehicleScreen {
    private final HalfTrack digger;

    private static final ResourceLocation TEXTURE = Common.locate("textures/gui/container/inventory.png");

    public HalfTrackScreen(HalfTrack digger, VehicleScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);

        this.digger = digger;
    }

    @Override
    protected void init() {
        super.init();

        // Drill button
        MutableComponent text = Component.translatable("gui.create_machinery.half_track.assist", KeyBindings.HORN.getTranslatedKeyMessage());
        ImageButton help = new ImageButton(getX() + 160, getY() + 5,
                10, 10,
                64, 0, 10, TEXTURE, 128, 128,
                b -> digger.toggleAssist(), text);
        help.setTooltip(Tooltip.create(text));
        addRenderableWidget(help);
    }
}
