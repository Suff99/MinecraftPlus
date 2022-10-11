package mc.craig.software.craftplus.networking.packets;

import mc.craig.software.craftplus.common.capability.ExtendedInventoryCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncExtendedInv {

    public int entityID;
    public CompoundTag nbt;

    public MessageSyncExtendedInv(int entityID, CompoundTag nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public MessageSyncExtendedInv(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.nbt = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeNbt(this.nbt);
    }

    public static void handle(MessageSyncExtendedInv message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(message.entityID);

            if (entity instanceof Player) {
                entity.getCapability(ExtendedInventoryCapability.CAPABILITY).ifPresent(inv -> inv.deserializeNBT(message.nbt));
            }
            ctx.get().setPacketHandled(true);
        });
        ctx.get().setPacketHandled(true);

    }

}