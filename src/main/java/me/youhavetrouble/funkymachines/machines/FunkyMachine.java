package me.youhavetrouble.funkymachines.machines;

import me.youhavetrouble.funkymachines.FunkyMachines;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public abstract class FunkyMachine {

    private final String id;

    public FunkyMachine(@NotNull String id) {
        this.id = id;
    }

    public @NotNull String getId() {
        return id;
    }

    /**
     * Called when the machine is placed
     * @param block The block of the machine
     * @param player The player who placed the machine, or null if the machine was placed by a non-player
     */
    public void onPlace(@NotNull Block block, @Nullable Player player) {}

    /**
     * Called when the machine is destroyed
     * @param blockState The block of the machine
     * @param player The player who destroyed the machine, or null if the machine was destroyed by a non-player
     * @return Items that should be dropped when the machine is destroyed
     */
    public Collection<ItemStack> onDestroy(@NotNull BlockState blockState, @Nullable Player player) {
        return List.of(getItem());
    }

    /**
     * Called when the machine is activated
     * @param block The block of the machine
     */
    public void onActivate(@NotNull Block block) {}

    /**
     * Called when the machine is interacted with by a player
     * @param player The player who interacted with the machine
     */
    public void onInteract(@NotNull Player player) {}

    /**
     * Whether this machine can be interacted with by a hopper
     */
    public boolean canBeInteractedWithHopper() {
        return false;
    }

    /**
     * Wheter this machine should open its default inventory when interacted with
     */
    public boolean canBeInteractedWith() {
        return false;
    }

    /**
     * Gets an item that when placed will create this machine
     */
    public @NotNull ItemStack getItem() {
        ItemStack item = new ItemStack(Material.DISPENSER);
        ItemMeta meta = item.getItemMeta();
        String name = id.replace("_", " ");
        meta.displayName(Component.text(name.substring(0, 1).toUpperCase() + name.substring(1), Style.style().decoration(TextDecoration.ITALIC, false).build()));
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(FunkyMachines.getInstance().getFunkyMachineKey(), PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }

    public enum MachineType {
        BLOCK_BREAKER("block_breaker");

        private final String id;
        MachineType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
