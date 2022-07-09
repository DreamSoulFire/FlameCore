package dream.soulflame.flamecore.utils;

import org.bukkit.command.CommandSender;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.utils.SendUtil.reName;
import static org.bukkit.Bukkit.*;
import static org.bukkit.Bukkit.dispatchCommand;

public class CommandUtil {
    public static void commands(CommandSender sender, String commands) {

        int delay = 0;
        String[] split;
        String eventPrefix;
        eventPrefix = "[op]";
        if (commands.startsWith(eventPrefix)) {
            String substring = commands.substring(eventPrefix.length());
            if (!substring.contains(":")) {
                opCommand(sender, reName(sender, substring), delay);
                return;
            }
            split = substring.split(":");
            if (split.length < 2) return;
            delay = Integer.parseInt(split[1]);
            opCommand(sender, reName(sender, split[0]), delay);
        }
        eventPrefix = "[console]";
        if (commands.startsWith(eventPrefix)) {
            String substring = commands.substring(eventPrefix.length());
            if (!substring.contains(":")) {
                dispatchCommand(getConsoleSender(), reName(sender, substring));
                return;
            }
            split = substring.split(":");
            if (split.length < 2) return;
            delay = Integer.parseInt(split[1]);
            String[] finalSplit = split;
            getScheduler().runTaskLaterAsynchronously(getPlugin(),
                    () -> dispatchCommand(getConsoleSender(), reName(sender, finalSplit[0])),
                    delay);
        }
        eventPrefix = "[player]";
        if (commands.startsWith(eventPrefix)) {
            String substring = commands.substring(eventPrefix.length());
            if (!substring.contains(":")) {
                dispatchCommand(sender, reName(sender, substring));
                return;
            }
            split = substring.split(":");
            if (split.length < 2) return;
            delay = Integer.parseInt(split[1]);
            String[] finalSplit = split;
            getScheduler().runTaskLaterAsynchronously(getPlugin(),
                    () -> dispatchCommand(sender, reName(sender, finalSplit[0])),
                    delay);
        }

    }

    private static void opCommand(CommandSender sender, String substring, int delay) {
        if (sender.isOp()) getScheduler().runTaskLaterAsynchronously(
                getPlugin(),
                () -> dispatchCommand(sender, substring),
                delay);
        else getScheduler().runTaskLaterAsynchronously(
                getPlugin(),
                () -> {
                    sender.setOp(true);
                    dispatchCommand(sender, substring);
                    sender.setOp(false);
                },
                delay);
    }
}
