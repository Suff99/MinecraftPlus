package mc.craig.software.craftplus.networking.packets;

import mc.craig.software.craftplus.common.ModSounds;
import mc.craig.software.craftplus.common.advancement.TriggerManager;
import mc.craig.software.craftplus.common.items.ParagliderItem;
import mc.craig.software.craftplus.networking.Network;
import mc.craig.software.craftplus.util.GliderUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageToggleGlide {

    public MessageToggleGlide() {
    }

    public MessageToggleGlide(FriendlyByteBuf buffer) {
    }

    public static void handle(MessageToggleGlide message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> {
            @Nullable ServerPlayer sender = ctx.get().getSender();
            if (sender != null) {
                if (GliderUtil.hasParagliderEquipped(sender)) {
                    ItemStack chestItem = sender.getItemBySlot(EquipmentSlot.CHEST);
                    ParagliderItem.setGlide(chestItem, !ParagliderItem.glidingEnabled(chestItem));
                    if (ParagliderItem.glidingEnabled(chestItem)) {
                        TriggerManager.FIRST_TIME_FLYER.trigger(Objects.requireNonNull(ctx.get().getSender()));
                        sender.level.playSound(null, sender.getX(), sender.getY(), sender.getZ(), ParagliderItem.isSpaceGlider(chestItem) ? ModSounds.SPACE_DEPLOY.get() : ModSounds.GLIDER_OPEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                        Network.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> sender.getCommandSenderWorld().dimension()), new MessagePlaySound(ParagliderItem.isSpaceGlider(chestItem) ? ModSounds.SPACE_GLIDE.get().getLocation() : SoundEvents.ELYTRA_FLYING.getLocation(), sender.getUUID()));

                        // Damage Glider as used
                        chestItem.hurtAndBreak(1, sender, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

}