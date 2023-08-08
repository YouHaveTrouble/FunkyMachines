package me.youhavetrouble.funkymachines.listeners;

import me.youhavetrouble.funkymachines.FunkyMachines;
import me.youhavetrouble.funkymachines.machines.FunkyMachine;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class MachinePlaceListener implements Listener {

    private final NamespacedKey funkyMachineKey;
    private final FunkyMachines plugin;

    public MachinePlaceListener(FunkyMachines plugin, NamespacedKey funkyMachineKey) {
        this.plugin = plugin;
        this.funkyMachineKey = funkyMachineKey;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachinePlace(BlockPlaceEvent event) {
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta == null) return;

        String id = meta.getPersistentDataContainer().get(funkyMachineKey, PersistentDataType.STRING);
        if (id == null) return;

        FunkyMachine funkyMachine = plugin.getMachine(id);
        if (funkyMachine == null) return;

        Block block = event.getBlockPlaced();
        BlockState state = block.getState(false);
        if (!(state instanceof PersistentDataHolder holder)) {
            plugin.getLogger().warning("Block placed at " + block.getLocation() + " is not a PersistentDataHolder, but is marked as a funky machine. This is a bug. Please report it.");
            return;
        }

        holder.getPersistentDataContainer().set(funkyMachineKey, PersistentDataType.STRING, id);
        funkyMachine.onPlace(block, event.getPlayer());
    }

}
