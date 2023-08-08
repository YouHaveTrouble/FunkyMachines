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

public abstract class FunkyMachine {

    private final String id;

    public FunkyMachine(@NotNull String id) {
        this.id = id;
    }

    public @NotNull String getId() {
        return id;
    }

    public void onPlace(@NotNull Block block, @Nullable Player player) {}

    /**
     * Called when the machine is destroyed
     * @param blockState The block of the machine
     */
    public ItemStack onDestroy(@NotNull BlockState blockState) {
        return getItem();
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

}
