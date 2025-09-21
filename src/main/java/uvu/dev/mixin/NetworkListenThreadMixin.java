package uvu.dev.mixin;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.NetServerHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import uvu.dev.NoCrashes;

@Mixin(
        value = NetworkListenThread.class,
        remap = false
)
public abstract class NetworkListenThreadMixin {

    @Unique
    private static final ThreadLocal<Boolean> suppress =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Unique
    private final ThreadLocal<Boolean> placement =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Redirect(
            method = "method_34",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/logging/Logger;log(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V"
            )
    )
    private void redirectLogging(Logger logger, Level level, String msg, Throwable throwable)
    {
        logger.log(level, msg, throwable);

        if (throwable instanceof ArrayIndexOutOfBoundsException)
        {
            boolean placement = java.util.Arrays.stream(throwable.getStackTrace())
                    .anyMatch(el -> el.getClassName().contains("Packet15Place")
                            || el.getMethodName().contains("onBlockPlaced")
                            || el.getMethodName().contains("stationapi_handlePlace")
                            || el.getMethodName().contains("setBlockStateWithMetadata"));

            this.placement.set(placement);
            suppress.set(Boolean.TRUE);
            NoCrashes.LOGGER.info("ArrayIndexOutOfBoundsException detected - player won't be kicked");
        } else
        {
            placement.set(Boolean.FALSE);
            suppress.set(Boolean.FALSE);
        }
    }

    @Redirect(
            method = "method_34",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/NetServerHandler;method_833(Ljava/lang/String;)V"
            )
    )
    private void redirectKickPlayer(NetServerHandler handler, String reason)
    {
        if (Boolean.TRUE.equals(suppress.get()))
        {
            try
            {
                if (Boolean.TRUE.equals(this.placement.get()))
                {
                    EntityPlayerMP player = ((INetServerHandler) handler).getPlayerEntity();
                    ItemStack stack = player.inventory.getCurrentItem();
                    int slot = player.inventory.currentItem;

                    if (stack != null)
                        player.inventory.mainInventory[slot] =
                                (--stack.stackSize <= 0 ? null : ItemStack.copyItemStack(stack));
                }
            } catch (Throwable t)
            {
                NoCrashes.LOGGER.warn("Failed to decrement held item after suppressing kick: {}", t.toString());
            } finally
            {
                this.placement.set(Boolean.FALSE);
            }

            suppress.set(Boolean.FALSE);
            NoCrashes.LOGGER.warn("Suppressed player kick for ArrayIndexOutOfBoundsException");
            return;
        }
        handler.method_833(reason);
    }
}