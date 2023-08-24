package me.youhavetrouble.funkymachines.events;

import org.bukkit.event.Event;

public abstract class FunkyMachineEvent extends Event {

    private final String machineId;

    public FunkyMachineEvent(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineId() {
        return machineId;
    }
}
