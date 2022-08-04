package mc.craig.software.notnotyet.networking.packets;

import mc.craig.software.notnotyet.common.capability.ModCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageSyncCap {
    public UUID entityID;
    public CompoundTag nbt;

    public MessageSyncCap(UUID entityID, CompoundTag nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public MessageSyncCap(FriendlyByteBuf buf) {
        this.entityID = buf.readUUID();
        this.nbt = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.entityID);
        buf.writeNbt(this.nbt);
    }

    public static void handle(MessageSyncCap message, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        Player entity = level.getPlayerByUUID(message.entityID);

        ctx.get().enqueueWork(() -> {
            if (entity != null)
                ModCapability.get(entity).ifPresent((c) -> c.deserializeNBT(message.nbt));
                System.out.println(message.nbt);
            ctx.get().setPacketHandled(true);
        });
    }
}