package uvu.dev.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.EntityPlayerMP;

@Mixin(NetServerHandler.class)
public interface INetServerHandler
{
    @Accessor("playerEntity")
    EntityPlayerMP getPlayerEntity();
}