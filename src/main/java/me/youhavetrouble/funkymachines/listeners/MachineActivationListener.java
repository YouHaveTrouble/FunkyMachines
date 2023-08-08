package me.youhavetrouble.funkymachines.listeners;

import io.papermc.paper.event.block.BlockPreDispenseEvent;
import me.youhavetrouble.funkymachines.FunkyMachines;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class MachineActivationListener implements Listener {

    private final NamespacedKey funkyMachineKey;
    private final FunkyMachines plugin;

    public MachineActivationListener(FunkyMachines plugin, NamespacedKey funkyMachineKey) {
        this.plugin = plugin;
        this.funkyMachineKey = funkyMachineKey;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMachineActivated(BlockPreDispenseEvent event) {

        Block block = event.getBlock();
        BlockState state = block.getState(false);
        if (!(state instanceof PersistentDataHolder holder)) return;

        String id = holder.getPersistentDataContainer().get(funkyMachineKey, PersistentDataType.STRING);
        if (id == null) return;
        event.setCancelled(true);
        plugin.getMachine(id).onActivate(block);
    }

}
