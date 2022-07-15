package dream.soulflame.flamecore;

import dream.soulflame.flamecore.commands.MainCommand;
import dream.soulflame.flamecore.events.*;
import dream.soulflame.flamecore.events.ItemEquipChance.EquipEvent;
import dream.soulflame.flamecore.events.ItemEquipChance.IdentifierEvent;
import dream.soulflame.flamecore.fileloader.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static dream.soulflame.flamecore.utils.SendUtil.message;
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
        new Metrics(plugin, 15780);

        start = System.currentTimeMillis();

        String[] startMsg = {
                splitLine,
                prefixMsg + " &7-> &6插件&b加载&a成功",
                "",
                "&6插件版本&f: &a" + getDescription().getVersion(),
                "&b开始&a加载&3配置文件",
                ""
        };
        message(startMsg);

        plugin = this;
        getPluginCommand("flamecore").setExecutor(new MainCommand());
        getPluginCommand("flamecore").setTabCompleter(new MainCommand());

        saveDefaultConfig();
        ADLoader.reload();
        AFDLoader.reload();
        AFELoader.reload();
        AIALoader.reload();
        ASJLoader.reload();
        IDLoader.reload();
        IECLoader.reload();
        RCGLoader.reload();
        SFLoader.reload();
        SICLoader.reload();
        TILoader.reload();
        String[] loadPlugin = {
                "&b配置文件&b加载&a成功",
                "",
        };
        message(loadPlugin);

        if (ADLoader.adEnable)
            getPluginManager().registerEvents(new AntiDrop(), this);
        if (AFDLoader.afdEnable)
            getPluginManager().registerEvents(new AntiFallingDamage(), this);
        if (AFELoader.afeEnable)
            getPluginManager().registerEvents(new AntiFireEntity(), this);
        if (AIALoader.aiaEnable)
            getPluginManager().registerEvents(new AntiItemAttack(), this);
        if (ASJLoader.asjEnable)
            getPluginManager().registerEvents(new AntiSpeedJoin(), this);
        if (IDLoader.idEnable)
            getPluginManager().registerEvents(new ItemDurability(), this);
        if (IECLoader.iecEnable) {
            getPluginManager().registerEvents(new EquipEvent(), this);
            if (!getPluginManager().isPluginEnabled("DragonCore")) return;
            getPluginManager().registerEvents(new IdentifierEvent(), this);
        }
        if (SICLoader.sicEnable)
            getPluginManager().registerEvents(new SwapItemCooldown(), this);
        if (TILoader.tiEnable)
            getPluginManager().registerEvents(new TabInfo(), this);

        String[] finishMsg = {
                "&a加载&b完成 &6插件&e开始&3运行",
                splitLine
        };
        message(finishMsg);
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
        message(closeMsg);
        if (!getConfig().getBoolean("KickPlayer.Enable" ,false)) return;
        String kickMsg = getConfig().getString("KickMsg", "");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) onlinePlayer.kickPlayer(reColor(kickMsg));
    }
}
