package eu.rcdevelopment.rcsell.Utils;

import com.cryptomorin.xseries.messages.ActionBar;
import eu.rcdevelopment.rcsell.RCSell;
import org.bukkit.entity.Player;

public class MessagesUtils {

    public static void sendActionbarM(Player p, String message) {
        p.sendMessage(ColorUtils.format(message));
        boolean actionbar = RCSell.getInstance().getConfig().getBoolean("action-bar");
        if (actionbar) {
            ActionBar.sendActionBar(p, ColorUtils.format(message));
        }
    }

    public static String getS(String path) {
        String s = RCSell.getInstance().getConfig().getString(path);

        return s;
    }
    public static double getD(String path) {
        Double d = RCSell.getInstance().getConfig().getDouble(path);

        return d;
    }
    public static int getI(String path) {
        int i = RCSell.getInstance().getConfig().getInt(path);

        return i;
    }
}
