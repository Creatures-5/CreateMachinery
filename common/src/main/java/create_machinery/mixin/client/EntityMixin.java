package create_machinery.mixin.client;

import create_machinery.entity.Copperfin;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Nullable public abstract Entity getVehicle();

    @Inject(method = "isEyeInFluid", at = @At("HEAD"), cancellable = true)
    private void create_machinery$isEyeInFluid(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
        if (getVehicle() instanceof Copperfin) {
            cir.setReturnValue(false);
        }
    }
}
