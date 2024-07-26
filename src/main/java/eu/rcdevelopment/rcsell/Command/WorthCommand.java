package eu.rcdevelopment.rcsell.Command;

import eu.rcdevelopment.rcsell.RCSell;
import eu.rcdevelopment.rcsell.Utils.MessagesUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        Player p = (Player) sender;
        FileConfiguration c = RCSell.getInstance().getConfig();

        if (args.length == 0) {
            Material hand = p.getInventory().getItemInMainHand().getType();
            int amount = p.getInventory().getItemInMainHand().getAmount();

            if (!hand.isAir()) {
                if (c.contains("items." + hand)) {
                    double price = c.getDouble("items." + hand) * amount;
                    MessagesUtils.sendActionbarM(p, c.getString("worth").replace("%amount%", String.valueOf(amount)).replace("%item%", capitalizeWords(hand.toString().toLowerCase().replace("_", " "))).replace("%price%", String.valueOf(price)));
                    return true;
                } else {
                    MessagesUtils.sendActionbarM(p, c.getString("worth-error"));
                    return true;
                }
            } else {
                MessagesUtils.sendActionbarM(p, c.getString("worth-air"));
                return true;
            }
        }

        if (args.length == 1) {
            try {
                Material material = Material.valueOf(args[0].toUpperCase());
                int amount = 1;

                if (!material.isAir()) {
                    if (c.contains("items." + material)) {
                        double price = c.getDouble("items." + material) * amount;
                        MessagesUtils.sendActionbarM(p, c.getString("worth").replace("%amount%", String.valueOf(amount)).replace("%item%", capitalizeWords(material.toString().toLowerCase().replace("_", " "))).replace("%price%", String.valueOf(price)));
                        return true;
                    } else {
                        MessagesUtils.sendActionbarM(p, c.getString("worth-error"));
                        return true;
                    }
                } else {
                    MessagesUtils.sendActionbarM(p, c.getString("worth-air"));
                    return true;
                }
            } catch (IllegalArgumentException e) {
                MessagesUtils.sendActionbarM(p, c.getString("worth-error"));
                return true;
            }
        }

        if (args.length == 2) {
            try {
                Material material = Material.valueOf(args[0].toUpperCase());
                int amount = Integer.parseInt(args[1]);

                if (!material.isAir()) {
                    if (c.contains("items." + material)) {
                        double price = c.getDouble("items." + material) * amount;
                        MessagesUtils.sendActionbarM(p, c.getString("worth").replace("%amount%", String.valueOf(amount)).replace("%item%", capitalizeWords(material.toString().toLowerCase().replace("_", " "))).replace("%price%", String.valueOf(price)));
                        return true;
                    } else {
                        MessagesUtils.sendActionbarM(p, c.getString("worth-error"));
                        return true;
                    }
                } else {
                    MessagesUtils.sendActionbarM(p, c.getString("worth-air"));
                    return true;
                }
            } catch (IllegalArgumentException e) {
                MessagesUtils.sendActionbarM(p, c.getString("worth-error"));
                return true;
            }
        }

        return false;
    }

    private String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(RCSell.materials);
        }
        if (args.length == 2) {
            completions.add("16");
            completions.add("32");
            completions.add("64");
        }
        return completions;
    }
}
