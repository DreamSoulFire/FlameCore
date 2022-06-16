package dream.soulflame.flamecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static org.bukkit.Bukkit.createBossBar;
import static org.bukkit.Bukkit.getScheduler;

public class SendUtil {
    public static void message(String text) {
        Bukkit.getConsoleSender().sendMessage(reColor(text));
    }
    public static void message(CommandSender sender, String text, int delay) {
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> sender.sendMessage(reColor(reName(sender, text))), delay);
    }
    public static void title(CommandSender sender, String main, String sub, int fadeIn, int stay, int fadeOut, int delay) {
        Player player = (Player) sender;
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> player.sendTitle(
                        reColor(reName(sender, main)),
                        reColor(reName(sender, sub)),
                        fadeIn,
                        stay,
                        fadeOut
                ),
                delay);
    }
    public static void bossbar(CommandSender sender, String title, String color, String style, int delay) {
        Player player = (Player) sender;
        BossBar bossBar = createBossBar(reColor(reName(player, title)), BarColor.valueOf(color), BarStyle.valueOf(style), BarFlag.PLAY_BOSS_MUSIC);
        bossBar.addPlayer(player);
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> bossBar.removePlayer(player), delay);
    }
    public static String reColor(String text) {
        return ChatColor.translateAlternateColorCodes('&',text);
    }
    public static List<String> reColor(List<String> text) {
        List<String> list = new ArrayList<>();
        for (String texts : text)
            list.add(ChatColor.translateAlternateColorCodes('&', texts));
        return list;
    }
    public static String reName(CommandSender sender, String text) {
        return text.replace("<player>", sender.getName());
    }
    public static List<String> stripColor(List<String> text) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.stripColor(text.toString()));
        return list;
    }
    public static String stripColor(String text) {
        return ChatColor.stripColor(text);
    }
}
