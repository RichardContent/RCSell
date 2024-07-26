package eu.rcdevelopment.rcsell.Command;

import eu.rcdevelopment.rcsell.RCSell;
import eu.rcdevelopment.rcsell.Utils.MessagesUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SellCommand implements CommandExecutor, TabCompleter {
    public static List<Player> cmd = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        Player p = (Player) sender;
        if (args.length == 0) {
            Inventory sell = Bukkit.createInventory(p, RCSell.getInstance().getConfig().getInt("rows") * 9, MessagesUtils.getS("title"));
            p.openInventory(sell);
            cmd.add(p);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("hand")) {
            ItemStack itemStack = p.getInventory().getItemInMainHand();

            if (itemStack == null || itemStack.getType().isAir()) {
                MessagesUtils.sendActionbarM(p, MessagesUtils.getS("error-no-sellable"));
                return true;
            }

            String name = itemStack.getType().toString();
            int amountA = itemStack.getAmount();
            double finalPrice = 0.0;
            int finalAmount = 0;

            if (RCSell.getInstance().getConfig().contains("items." + name)) {
                double amount = RCSell.getInstance().getConfig().getDouble("items." + name);
                finalPrice += amount * amountA;
            } else {
                finalPrice += RCSell.getInstance().getConfig().getDouble("items.not-sellable") * amountA;
            }
            finalAmount += amountA;
            if (finalPrice != 0.0) {
                RCSell.getEconomy().depositPlayer(p, finalPrice);
                p.getInventory().setItemInMainHand(null);
                MessagesUtils.sendActionbarM(p, RCSell.getInstance().getConfig().getString("sold").replace("%price%", String.valueOf(finalPrice)).replace("%amount%", String.valueOf(finalAmount)));
            } else {
                MessagesUtils.sendActionbarM(p, MessagesUtils.getS("error-no-sellable"));
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("inventory")) {
            double finalPrice = 0.0;
            int finalAmount = 0;

            for (ItemStack itemStack : p.getInventory().getContents()) {
                if (itemStack != null && !itemStack.getType().isAir()) {
                    String name = itemStack.getType().toString();
                    int amountA = itemStack.getAmount();

                    if (RCSell.getInstance().getConfig().contains("items." + name)) {
                        double amount = RCSell.getInstance().getConfig().getDouble("items." + name);
                        finalPrice += amount * amountA;
                    } else {
                        finalPrice += RCSell.getInstance().getConfig().getDouble("items.not-sellable") * amountA;
                    }
                    finalAmount += amountA;
                }
            }

            if (finalPrice != 0.0) {
                RCSell.getEconomy().depositPlayer(p, finalPrice);
                p.getInventory().clear();
                MessagesUtils.sendActionbarM(p, RCSell.getInstance().getConfig().getString("sold").replace("%price%", String.valueOf(finalPrice)).replace("%amount%", String.valueOf(finalAmount)));
            } else {
                MessagesUtils.sendActionbarM(p, MessagesUtils.getS("error-no-sellable"));
            }
        } else {
            MessagesUtils.sendActionbarM(p, MessagesUtils.getS("error-usage"));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();
        completions.add("hand");
        completions.add("inventory");
        return completions;
    }
}
