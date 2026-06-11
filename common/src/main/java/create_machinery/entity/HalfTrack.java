package create_machinery.entity;

import immersive_aircraft.cobalt.network.NetworkHandler;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import create_machinery.Common;
import create_machinery.Items;
import create_machinery.Sounds;
import create_machinery.Utils;
import create_machinery.client.KeyBindings;
import create_machinery.network.c2s.HalfTrackControlsUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import com.simibubi.create.AllItems;
import java.util.LinkedList;
import java.util.List;

import static create_machinery.Utils.doubleToPos;

public class HalfTrack extends MachineEntity {
    public boolean drilling = false;
    public boolean assistOn = true;

    public HalfTrack(EntityType<? extends HalfTrack> entityType, Level world) {
        super(entityType, world, true);
    }

    @Override
    public Item asItem() {
        return Items.HALF_TRACK.get();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!isVehicle()) {
            drilling = false;
        }

        setMaxUpStep(1.1f);

        // Exhaust particles
        if (level().isClientSide()) {
            double chance = engineSpinUpStrength + getEnginePower();
            if (level().random.nextDouble() < chance) {
                boolean fire = level().random.nextFloat() < engineSpinUpStrength;
                Matrix4f transform = getVehicleTransform();
                Vector4f pos = transformPosition(transform, 1.0f, 2.85f, -0.9375f);
                if (fire == true) {
                    level().addParticle(ParticleTypes.SMALL_FLAME, pos.x, pos.y, pos.z, 0.0, 0.1, 0.0);
                }
                else {
                    level().addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0.0, 0.1, 0.0);
                }
            }
        }
    }

    public boolean hasCakes() {
        return getSlots(Common.SLOT_CAKES).stream().anyMatch(slot -> !slot.isEmpty());
    }

    public boolean isCreativeFuel(List<ItemStack> cakes) {
        return AllItems.CREATIVE_BLAZE_CAKE.isIn(cakes.get(0));
    }

    public void burnCakes(float engineSpeed) {
        if (random.nextFloat() < engineSpeed / 640.0) {
            List<ItemStack> cakes = getSlots(Common.SLOT_CAKES);
            if (!cakes.isEmpty() && !isCreativeFuel(cakes)) {
                cakes.get(random.nextInt(cakes.size())).shrink(1);
            }
        }
    }

    @Override
    protected void updateController() {
        super.updateController();

        if (!level().isClientSide()) {
            return;
        }
        
       if (assistOn){
        // Lock into increments
            if (movementX == 0) {
                float yRot = getYRot();
                double step = 90;
                double targetRotation = Math.round(yRot / step) * step;
                double speed = 0.06f;
                setYRot((float) (yRot * (1.0 - speed) + targetRotation * speed));

                // Lock onto full blocks
                if (movementZ == 0) {
                    double vSpeed = 0.04f;
                    Vec3 deltaMovement = getDeltaMovement();
                    double dx = getX() - (Math.round(getX() - 0.5) + 0.5);
                    double dz = getZ() - (Math.round(getZ() - 0.5) + 0.5);
                    setDeltaMovement(deltaMovement.add(-dx * vSpeed, 0, -dz * vSpeed));
                }
            }
        }

        // Turn off steer assist
        if (KeyBindings.HORN.consumeClick()) {
            toggleAssist();
        }
    }

    public void toggleAssist() {
        assistOn = !assistOn;
        LivingEntity pilot = getControllingPassenger();
        if (pilot != null) {
            pilot.sendSystemMessage(Component.translatable(assistOn ? "create_machinery.half_track.assist_on" : "create_machinery.half_track.assist_off"));
        }
    }

    public Vec3 attemptToDismount(LivingEntity passenger, float ox, float oy, float oz) {
        Vector3f p = new Vector3f((float) getX() + ox * 2.0f, (float) getY() + oy * 2.0f, (float) getZ() + oz * 2.0f);
        Vec3 position = new Vec3(p.x, p.y, p.z);
        for (Pose entityPose : passenger.getDismountPoses()) {
            if (DismountHelper.canDismountTo(level(), position, passenger, entityPose)) {
                passenger.setPose(entityPose);
                return position;
            }
        }
        return null;
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 dismountLocation;
        Vector3f forwardDirection = getForwardDirection();
        dismountLocation = attemptToDismount(passenger, -forwardDirection.x(), 0.5f, -forwardDirection.z());
        if (dismountLocation != null) return dismountLocation;
        Vector3f rightDirection = getRightDirection();
        dismountLocation = attemptToDismount(passenger, rightDirection.x(), 0.0f, rightDirection.z());
        if (dismountLocation != null) return dismountLocation;
        dismountLocation = attemptToDismount(passenger, -rightDirection.x(), 0.0f, -rightDirection.z());
        if (dismountLocation != null) return dismountLocation;
        dismountLocation = attemptToDismount(passenger, forwardDirection.x(), 0.5f, forwardDirection.z());
        if (dismountLocation != null) return dismountLocation;
        return super.getDismountLocationForPassenger(passenger);
    }

    public boolean isTrackMoving() {
        return getSpeedVector().lengthSqr() > 0.00001f;
    }

    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        double p = enginePower.getSmooth();
        BBAnimationVariables.set("engine_vibration_x", (float) ((random.nextDouble() - 0.5) * p));
        BBAnimationVariables.set("engine_vibration_y", (float) ((random.nextDouble() - 0.5) * p));
        BBAnimationVariables.set("engine_vibration_z", (float) ((random.nextDouble() - 0.5) * p));
    }

    @Override
    public double getZoom() {
        return 4.0;
    }

    @Override
    protected SoundEvent getEngineSound() {
        return drilling ? Sounds.TUNNEL_DIGGER_DRILLING.get() : Sounds.TUNNEL_DIGGER.get();
    }
}
