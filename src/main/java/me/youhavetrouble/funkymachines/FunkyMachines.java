package me.youhavetrouble.funkymachines;

import me.youhavetrouble.funkymachines.commands.FunkyMachinesCommand;
import me.youhavetrouble.funkymachines.listeners.MachineActivationListener;
import me.youhavetrouble.funkymachines.listeners.MachineBreakListener;
import me.youhavetrouble.funkymachines.listeners.MachineInteractionListener;
import me.youhavetrouble.funkymachines.listeners.MachinePlaceListener;
import me.youhavetrouble.funkymachines.machines.BlockBreaker;
import me.youhavetrouble.funkymachines.machines.FunkyMachine;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FunkyMachines extends JavaPlugin {

    private final HashMap<String, FunkyMachine> machines = new HashMap<>();
    private NamespacedKey funkyMachineKey;

    private static FunkyMachines instance;

    @Override
    public void onEnable() {
        instance = this;

        this.funkyMachineKey = new NamespacedKey(this, "funky_machine");

        reloadPluginConfigs();

        getServer().getCommandMap().register("funkymachines", new FunkyMachinesCommand(this));
    }

    public void reloadPluginConfigs() {

        HandlerList.unregisterAll(this);

        getServer().getPluginManager().registerEvents(new MachinePlaceListener(this, funkyMachineKey), this);
        getServer().getPluginManager().registerEvents(new MachineBreakListener(this, funkyMachineKey), this);
        getServer().getPluginManager().registerEvents(new MachineActivationListener(this, funkyMachineKey), this);
        getServer().getPluginManager().registerEvents(new MachineInteractionListener(this), this);

        this.machines.clear();

        this.machines.put("block_breaker", new BlockBreaker());

    }

    /**
     * Gets the machine with the given id
     * @param id The id of the machine to get
     * @return The machine with the given id, or null if there is no machine with the given id
     */
    public FunkyMachine getMachine(String id) {
        return machines.get(id);
    }

    /**
     * Gets the machine at the given block
     * @param block The block to get the machine at
     * @return The machine at the given block, or null if there is no machine at the given block
     */
    public FunkyMachine getMachine(Block block) {
        if (!(block.getState() instanceof PersistentDataHolder holder)) return null;
        String id = holder.getPersistentDataContainer().get(funkyMachineKey, PersistentDataType.STRING);
        if (id == null) return null;
        return getMachine(id);
    }

    public Map<String, FunkyMachine> getMachines() {
        return Collections.unmodifiableMap(machines);
    }

    public static FunkyMachines getInstance() {
        return instance;
    }

    public NamespacedKey getFunkyMachineKey() {
        return funkyMachineKey;
    }
}
