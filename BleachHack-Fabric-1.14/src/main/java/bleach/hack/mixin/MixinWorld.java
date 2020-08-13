package bleach.hack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventSkyColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld {

	@Inject(at = @At("HEAD"), method = "getSkyColor", cancellable = true)
	public void getSkyColor(BlockPos blockPos, float f, CallbackInfoReturnable<Vec3d> ci) {
		EventSkyColor.SkyColor event = new EventSkyColor.SkyColor(blockPos, f);
		BleachHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.setReturnValue(Vec3d.ZERO);
			ci.cancel();
		} else if (event.getColor() != null) {
			ci.setReturnValue(event.getColor());
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "getCloudColor", cancellable = true)
	public void getCloudColor(float f, CallbackInfoReturnable<Vec3d> ci) {
		EventSkyColor.CloudColor event = new EventSkyColor.CloudColor(f);
		BleachHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.setReturnValue(Vec3d.ZERO);
			ci.cancel();
		} else if (event.getColor() != null) {
			ci.setReturnValue(event.getColor());
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "getFogColor", cancellable = true)
	public void getFogColor(float f, CallbackInfoReturnable<Vec3d> ci) {
		EventSkyColor.FogColor event = new EventSkyColor.FogColor(f);
		BleachHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.setReturnValue(Vec3d.ZERO);
			ci.cancel();
		} else if (event.getColor() != null) {
			ci.setReturnValue(event.getColor());
			ci.cancel();
		}
	}
}
