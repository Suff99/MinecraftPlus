package mc.craig.software.craftplus.common.menu;

import mc.craig.software.craftplus.common.ModMenus;
import mc.craig.software.craftplus.common.capability.ExtendedInventoryCapability;
import mc.craig.software.craftplus.common.capability.IExtendedInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ExtendedInventoryMenu extends AbstractContainerMenu {

    public static final Component CONTAINER_TITLE = Component.translatable("container.minecraft_plus.extended_inventory");

    public ExtendedInventoryMenu(int pContainerId, Inventory pPlayerInventory) {
        super(ModMenus.EXTENDED_INVENTORY.get(), pContainerId);

        IExtendedInventory extendedInventory = pPlayerInventory.player.getCapability(ExtendedInventoryCapability.CAPABILITY).resolve().orElseThrow();

        // Main Inv
        for(int row = 0; row < 4; ++row) {
            for(int col = 0; col < 11; ++col) {
                this.addSlot(new Slot(pPlayerInventory, col + (row + 1) * 11, 87 + 8 + col * 18, 216 + 8 + row * 18));
            }
        }

        // Hotbar
        for(int col = 0; col < 11; ++col) {
            this.addSlot(new Slot(pPlayerInventory, col, 87 + 8 + col * 18, 300));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
