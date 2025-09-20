package uvu.dev.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uvu.dev.GuiException;

@Mixin(
        value = Minecraft.class,
        remap = false
)
public abstract class MinecraftMixin
{
    @Shadow
    public abstract void changeWorld1(World var1);

    @Shadow
    public abstract void displayGuiScreen(GuiScreen screen);

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
            e.printStackTrace();
            this.changeWorld1(null);
            this.displayGuiScreen(new GuiException(e));
        }
    }
}