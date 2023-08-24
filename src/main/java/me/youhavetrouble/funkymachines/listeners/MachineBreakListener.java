package me.youhavetrouble.funkymachines.listeners;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import me.youhavetrouble.funkymachines.FunkyMachines;
import me.youhavetrouble.funkymachines.machines.FunkyMachine;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class MachineBreakListener implements Listener {

    private final NamespacedKey funkyMachineKey;
    private final FunkyMachines plugin;

    public MachineBreakListener(FunkyMachines plugin, NamespacedKey funkyMachineKey) {
        this.plugin = plugin;
        this.funkyMachineKey = funkyMachineKey;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineDestroyed(BlockDropItemEvent event) {
        Block block = event.getBlock();
        BlockState state = event.getBlockState();
        if (!(state instanceof PersistentDataHolder holder)) return;

        String id = holder.getPersistentDataContainer().get(funkyMachineKey, PersistentDataType.STRING);
        if (id == null) return;
        event.getItems().clear();
        for (ItemStack itemStack : plugin.getMachine(id).onDestroy(state, event.getPlayer())) {
            Item item = block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
            event.getItems().add(item);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineDestroyedByPlayer(BlockBreakEvent event) {
        if (plugin.getMachine(event.getBlock()) == null) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            return;
        }
        if (!event.getBlock().isPreferredTool(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineDestroyedByBlock(BlockBreakBlockEvent event) {

        Block block = event.getBlock();
        BlockState state = block.getState();
        if (!(state instanceof PersistentDataHolder holder)) return;

        String id = holder.getPersistentDataContainer().get(funkyMachineKey, PersistentDataType.STRING);
        if (id == null) return;

        event.getDrops().clear();
        for (ItemStack itemStack : plugin.getMachine(id).onDestroy(state, null)) {
            block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
            event.getDrops().add(itemStack);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineDestroyedByExplodingEntity(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> {
            FunkyMachine machine = plugin.getMachine(block);
            if (machine == null) return false;
            for (ItemStack itemStack : machine.onDestroy(block.getState(), null)) {
                block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
            }
            block.setType(Material.AIR, true);
            return true;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineDestroyedByExplodingBlock(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> {
            FunkyMachine machine = plugin.getMachine(block);
            if (machine == null) return false;
            for (ItemStack itemStack : machine.onDestroy(block.getState(), null)) {
                block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
            }
            block.setType(Material.AIR, true);
            return true;
        });
    }

}
