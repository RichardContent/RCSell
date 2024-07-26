package eu.rcdevelopment.rcsell;

import eu.rcdevelopment.rcsell.Command.SellCommand;
import eu.rcdevelopment.rcsell.Command.WorthCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class RCSell extends JavaPlugin {
    private static Economy econ = null;
    private static RCSell instance;
    public static List<String> materials = null;

    @Override
    public void onEnable() {
        long now = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("sell").setExecutor(new SellCommand());
        getCommand("sell").setTabCompleter(new SellCommand());

        getCommand("worth").setExecutor(new WorthCommand());
        getCommand("worth").setTabCompleter(new WorthCommand());

        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);

        long now2 = System.currentTimeMillis();
        materials = setCompletions();
        long time2 = System.currentTimeMillis() - now2;
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Material Task took: " + ChatColor.YELLOW + time2 + ChatColor.GRAY + " ms");

        long time = System.currentTimeMillis() - now;
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Config Task took: " + ChatColor.YELLOW + time + ChatColor.GRAY + " ms");
    }
    public static RCSell getInstance() {
        return instance;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }
    private List<String> setCompletions() {
        List<String> materials = new ArrayList<>();

        for (Material st : Material.values()) {
            String str = st.toString();

            String[] words = str.split("_");
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append("_");
            }
            sb.setLength(sb.length() - 1);
            materials.add(sb.toString().trim());
        }

        return materials;
    }
}
