package eu.rcdevelopment.rcsell;

import eu.rcdevelopment.rcsell.Command.SellCommand;
import eu.rcdevelopment.rcsell.Utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (SellCommand.cmd.contains( (Player) e.getPlayer())) {
            Player p = (Player) e.getPlayer();

            Double finalPrice = 0.0;
            int finalAmount = 0;

            for (ItemStack itemStack : e.getInventory().getContents()) {
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
                MessagesUtils.sendActionbarM(p, RCSell.getInstance().getConfig().getString("sold").replace("%price%", String.valueOf(finalPrice)).replace("%amount%", String.valueOf(finalAmount)));
            }
        }
    }
}
