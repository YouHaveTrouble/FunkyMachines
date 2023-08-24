package me.youhavetrouble.funkymachines.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when funky machine breaks a block
 */
public class FunkyMachineBlockBreakEvent extends FunkyMachineEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    private final Block block;

    /**
     * @param machineId The id of the type of the machine that broke the block
     * @param block The block that is about to be broken
     */
    public FunkyMachineBlockBreakEvent(String machineId, Block block) {
        super(machineId);
        this.cancelled = false;
        this.block = block;
    }

    /**
     * Gets the block that is about to be broken
     * @return The block that is about to be broken
     */
    public Block getBlock() {
        return block;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
