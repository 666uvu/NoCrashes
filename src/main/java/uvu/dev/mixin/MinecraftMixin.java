package uvu.dev.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.Minecraft;
import uvu.dev.NoCrashes;

@Mixin(
        value = Minecraft.class,
        remap = false
)
public abstract class MinecraftMixin
{
    @WrapOperation(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;runTick()V"
            )
    )
    private void wrapRunTickWithExceptionHandler(Minecraft instance, Operation<Void> original)
    {
        try
        {
            original.call(instance);
        } catch (RuntimeException e)
        {
            NoCrashes.LOGGER.error(e.getMessage());
        }
    }
}