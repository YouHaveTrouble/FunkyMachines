package me.youhavetrouble.funkymachines.commands;

import me.youhavetrouble.funkymachines.FunkyMachines;
import me.youhavetrouble.funkymachines.machines.FunkyMachine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FunkyMachinesCommand extends Command {

    private final FunkyMachines plugin;

    public FunkyMachinesCommand(FunkyMachines plugin) {
        super("funkymachines", "FunkyMachines command", "/funkymachines", List.of("fm"));
        this.plugin = plugin;
        this.setPermission("funkymachines.admin");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (args.length == 0) {
            sender.sendMessage("FunkyMachines");
            sender.sendMessage("By YouHaveTrouble");
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (!sender.hasPermission("funkymachines.admin")) {
                    sender.sendMessage("You do not have permission to use this command");
                    return true;
                }
                plugin.reloadPluginConfigs();
                sender.sendMessage("Reloading FunkyMachines...");
                return true;
            }
            case "give" -> {
                switch (args.length) {
                    case 2 -> {
                        if (!(sender instanceof Player player)) {
                            sender.sendMessage("Usage: /funkymachines give <machine> <player>");
                            return true;
                        }
                        String machineId = args[1];
                        giveMachine(player, machineId);
                        return true;
                    }
                    case 3 -> {
                        String machineId = args[1];
                        String playerName = args[2];
                        Player player = plugin.getServer().getPlayer(playerName);
                        if (player == null) {
                            sender.sendMessage("Unknown player: " + playerName);
                            return true;
                        }
                        giveMachine(player, machineId);
                        return true;
                    }
                    default -> sender.sendMessage("Usage: /funkymachines give <machine> <player>");
                }
            }
            default -> sender.sendMessage("Unknown subcommand");
        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1 -> {
                return StringUtil.copyPartialMatches(args[0], List.of("reload", "give"), new ArrayList<>());
            }
            case 2 -> {
                if (args[0].equals("give")) {
                    return StringUtil.copyPartialMatches(args[1], plugin.getMachines().keySet(), new ArrayList<>());
                }
            }
            case 3 -> {
                if (args[0].equals("give")) {
                    return StringUtil.copyPartialMatches(args[2], plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList(), new ArrayList<>());
                }
            }
        }
        return new ArrayList<>();
    }

    private void giveMachine(Player player, String machineId) {
        FunkyMachine machine = plugin.getMachine(machineId);
        if (machine == null) {
            player.sendMessage("Unknown machine: " + machineId);
            return;
        }
        player.sendMessage("Giving " + player.getName() + " " + machineId);
        player.getInventory().addItem(machine.getItem());
    }
}
