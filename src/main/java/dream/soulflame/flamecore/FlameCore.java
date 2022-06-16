package dream.soulflame.flamecore;

import dream.soulflame.flamecore.commands.MainCommand;
import dream.soulflame.flamecore.events.AntiFallingDamage;
import dream.soulflame.flamecore.events.AntiFire;
import dream.soulflame.flamecore.events.AntiSpeedJoin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static dream.soulflame.flamecore.utils.SendUtil.reColor;
import static org.bukkit.Bukkit.*;

public final class FlameCore extends JavaPlugin {

    public static long start;
    public static String prefixMsg = "&6Flame&eCore";
    public static String splitLine = "&b====================================";

    private static FlameCore plugin;

    public static FlameCore getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        start = System.currentTimeMillis();

        String[] startMsg = {
                splitLine,
                prefixMsg + " &7-> &6插件&b加载&a成功",
                "",
                "&6插件版本&f: &a" + getDescription().getVersion(),
                "&b开始&a加载&3配置文件",
                ""
        };
        for (String start : startMsg) getConsoleSender().sendMessage(reColor(start));
        plugin = this;
        getPluginCommand("flamecore").setExecutor(new MainCommand());
        getPluginCommand("flamecore").setTabCompleter(new MainCommand());

        saveDefaultConfig();
        String[] loadPlugin = {
                "&b配置文件&b加载&a成功",
                "",
        };
        for (String load : loadPlugin) getConsoleSender().sendMessage(reColor(load));

        if (getConfig().getBoolean("AntiFalling.Enable", false))
            getPluginManager().registerEvents(new AntiFallingDamage(), this);
        if (getConfig().getBoolean("AntiFire.Enable", false))
            getPluginManager().registerEvents(new AntiFire(), this);
        if (getConfig().getBoolean("AntiSpeedJoin.Enable", false))
            getPluginManager().registerEvents(new AntiSpeedJoin(), this);

        String[] finishMsg = {
                "&a加载&b完成 &6插件&e开始&3运行",
                splitLine
        };
        for (String finish : finishMsg) getConsoleSender().sendMessage(reColor(finish));
    }

    @Override
    public void onDisable() {
        String[] closeMsg = {
                splitLine,
                prefixMsg + " &7-> &6插件&c卸载&a成功",
                "",
                "&6插件版本&f: &a" + getDescription().getVersion(),
                "&e感谢使用本插件",
                splitLine
        };
        for (String close : closeMsg) getConsoleSender().sendMessage(reColor(close));
        if (!getConfig().getBoolean("KickPlayer.Enable" ,false)) return;
        String kickMsg = getConfig().getString("KickMsg", "");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) onlinePlayer.kickPlayer(reColor(kickMsg));
    }
}
