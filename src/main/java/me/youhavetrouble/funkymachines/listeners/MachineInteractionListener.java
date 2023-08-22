package me.youhavetrouble.funkymachines.listeners;

import me.youhavetrouble.funkymachines.FunkyMachines;
import me.youhavetrouble.funkymachines.machines.FunkyMachine;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;

public class MachineInteractionListener implements Listener {

    private final FunkyMachines plugin;

    public MachineInteractionListener(FunkyMachines plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineInteractedWith(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof BlockInventoryHolder blockInventoryHolder)) return;
        Block block = blockInventoryHolder.getBlock();
        FunkyMachine funkyMachine = plugin.getMachine(block);
        if (funkyMachine == null) return;
        event.setCancelled(true);
        if (!(event.getPlayer() instanceof Player player)) return;
        funkyMachine.onInteract(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPushedIntoMachine(InventoryMoveItemEvent event) {
        if (!(event.getDestination().getHolder() instanceof BlockInventoryHolder blockInventoryHolder)) return;
        Block block = blockInventoryHolder.getBlock();
        FunkyMachine funkyMachine = plugin.getMachine(block);
        if (funkyMachine == null) return;
        if (funkyMachine.canBeInteractedWithHopper()) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPulledFromMachine(InventoryMoveItemEvent event) {
        if (!(event.getSource().getHolder() instanceof BlockInventoryHolder blockInventoryHolder)) return;
        Block block = blockInventoryHolder.getBlock();
        FunkyMachine funkyMachine = plugin.getMachine(block);
        if (funkyMachine == null) return;
        if (funkyMachine.canBeInteractedWithHopper()) return;
        event.setCancelled(true);
    }

}
