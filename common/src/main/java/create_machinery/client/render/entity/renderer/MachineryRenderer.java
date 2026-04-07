package create_machinery.client.render.entity.renderer;

import immersive_aircraft.client.render.entity.renderer.InventoryVehicleRenderer;
import create_machinery.entity.MachineEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public abstract class MachineryRenderer<T extends MachineEntity> extends InventoryVehicleRenderer<T> {
    public MachineryRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}

