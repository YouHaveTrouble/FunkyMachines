package me.youhavetrouble.funkymachines.machines;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class BlockBreaker extends FunkyMachine {

    private final ItemStack pickToBreakWith;

    public BlockBreaker() {
        super("block_breaker");
        this.pickToBreakWith = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = this.pickToBreakWith.getItemMeta();
        meta.setUnbreakable(true);
        this.pickToBreakWith.setItemMeta(meta);
    }

    @Override
    public void onPlace(@NotNull Block block, @Nullable Player player) {
        if (!Bukkit.isPrimaryThread()) throw new IllegalStateException("Must be called from main thread");
        if (!(block.getState(false) instanceof Container container)) return;
        // PreDispenseEvent requires dispenser to have any item in its inventory, so give it dirt for now
        container.getInventory().addItem(new ItemStack(Material.DIRT));
    }

    @Override
    public void onActivate(@NotNull Block block) {
        if (!Bukkit.isPrimaryThread()) throw new IllegalStateException("Must be called from main thread");
        if (!(block.getBlockData() instanceof Dispenser dispenser)) return;

        BlockFace facing = dispenser.getFacing();
        Block targetBlock = block.getRelative(facing);

        if (targetBlock.getType() == Material.BEDROCK) return;

        Collection<ItemStack> drops = targetBlock.getDrops(pickToBreakWith);
        BlockBreakBlockEvent event = new BlockBreakBlockEvent(targetBlock, block, new ArrayList<>(drops));
        Bukkit.getPluginManager().callEvent(event);
        for (ItemStack item : event.getDrops()) {
            if (item.getAmount() < 0) continue;
            targetBlock.getWorld().dropItemNaturally(targetBlock.getLocation(), item);
        }
        targetBlock.setType(Material.AIR, true);
    }

}
