package dream.soulflame.flamecore.utils;

import dream.soulflame.flamecore.enums.Conditions;
import dream.soulflame.flamecore.enums.Currencies;
import dream.soulflame.flamecore.enums.Equations;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.utils.SendUtil.*;

public class SpecialUtil {

    public static boolean takeCurrency(Player player, String take, String splitChar, Plugin plugin, List<String> configError) {
        String money = Currencies.MONEY.getArgs();
        String points = Currencies.POINTS.getArgs();
        String[] split = take.split(splitChar);
        Economy provider = plugin.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        if (take.equalsIgnoreCase("")) return true;
        if (take.contains(splitChar)) {
            double amount = Double.parseDouble(split[1]);
            if (take.startsWith(money)) {
                if (provider.has(player, amount)) {
                    provider.withdrawPlayer(player, amount);
                    return true;
                } else return false;
            } else if (take.startsWith(points)) {
                PlayerPointsAPI playerPointsAPI = new PlayerPointsAPI(PlayerPoints.getInstance());
                double look = playerPointsAPI.look(player.getUniqueId());
                System.out.println(look);
                if (look >= amount) {
                    playerPointsAPI.take(player.getUniqueId(), Integer.parseInt(split[1]));
                    return true;
                } else return false;
            } else {
                for (String error : configError) actions(player, error);
                return false;
            }
        } else {
            for (String error : configError) actions(player, error);
            return false;
        }
    }

    public static boolean checkCondition(Player player, List<String> conditions, String splitChar, List<String> configError) {
        String papi = Conditions.PAPI.getArgs();
        String perm = Conditions.PERM.getArgs();
        //小于
        String lt = Equations.LT.getArgs();
        //小于等于
        String le = Equations.LE.getArgs();
        //等于
        String eq = Equations.EQ.getArgs();
        //不等于
        String ne = Equations.NE.getArgs();
        //大于等于
        String ge = Equations.GE.getArgs();
        //大于
        String gt = Equations.GT.getArgs();
        String[] split;
        if (conditions == null) return true;
        for (String condition : conditions) {
            if (condition.startsWith(papi)) {
                String[] papiStr = condition.split(splitChar);
                String prePapi;
                if (papiStr[1].contains(gt) && !papiStr[1].contains(eq)) {
                    split = papiStr[1].split(gt);
                    prePapi = PlaceholderAPI.setPlaceholders(player, split[0]);
                    return Double.parseDouble(prePapi) > Double.parseDouble(split[1]);
                } else if (papiStr[1].contains(lt) && !papiStr[1].contains(eq)) {
                    split = papiStr[1].split(lt);
                    prePapi = PlaceholderAPI.setPlaceholders(player, split[0]);
                    return Double.parseDouble(prePapi) < Double.parseDouble(split[1]);
                } else if (papiStr[1].contains(eq) && !papiStr[1].contains(eq) && !papiStr[1].contains(eq)) {
                    split = papiStr[1].split(eq);
                    prePapi = PlaceholderAPI.setPlaceholders(player, split[0]);
                    return Double.parseDouble(prePapi) == Double.parseDouble(split[1]);
                } else if (papiStr[1].contains(ge)) {
                    split = papiStr[1].split(ge);
                    prePapi = PlaceholderAPI.setPlaceholders(player, split[0]);
                    return Double.parseDouble(prePapi) >= Double.parseDouble(split[1]);
                } else if (papiStr[1].contains(le)) {
                    split = papiStr[1].split(le);
                    prePapi = PlaceholderAPI.setPlaceholders(player, split[0]);
                    return Double.parseDouble(prePapi) <= Double.parseDouble(split[1]);
                } else if (papiStr[1].contains(ne)) {
                    split = papiStr[1].split(ne);
                    prePapi = PlaceholderAPI.setPlaceholders(player, split[0]);
                    return Double.parseDouble(prePapi) != Double.parseDouble(split[1]);
                } else {
                    for (String error : configError) actions(player, error);
                    return false;
                }
            } else if (condition.startsWith(perm)) {
                split = condition.split(splitChar);
                return player.hasPermission(split[1]);
            } else {
                for (String error : configError) actions(player, error);
                return false;
            }
        }
        return true;
    }

    public static void actions(CommandSender sender, String events) {
        int delay = 0;
        String substring;
        String[] split;
        String[] delaySplit;
        String eventPrefix;
        String prefix = getPlugin().getConfig().getString("Prefix", "");
        eventPrefix = "[message]";
        if (events.startsWith(eventPrefix)) {
            substring = events.substring(eventPrefix.length());
            if (!substring.contains(":")) {
                message(sender, prefix + substring);
                return;
            }
            delaySplit = substring.split(":");
            if (delaySplit[1] != null) delay = Integer.parseInt(delaySplit[1]);
            message(sender, prefix + delaySplit[0], delay);
        }
        eventPrefix = "[title]";
        if (events.startsWith(eventPrefix)) {
            substring = events.substring(eventPrefix.length());
            split = substring.split("-");
            if (!substring.contains(":")) {
                title(
                        sender,
                        split[0],
                        split[1],
                        Integer.parseInt(split[2]),
                        Integer.parseInt(split[3]),
                        Integer.parseInt(split[4]),
                        delay
                );
                return;
            }
            delaySplit = substring.split(":");
            if (delaySplit[1] != null) delay = Integer.parseInt(delaySplit[1]);
            title(
                    sender,
                    split[0],
                    split[1],
                    Integer.parseInt(split[2]),
                    Integer.parseInt(split[3]),
                    Integer.parseInt(delaySplit[0]),
                    delay
            );
        }
        eventPrefix = "[bossbar]";
        if (events.startsWith(eventPrefix)) {
            substring = events.substring(eventPrefix.length());
            split = substring.split("-");
            if (!substring.contains(":")) {
                bossbar(sender, split[0], split[1], split[2], delay);
                return;
            }
            delaySplit = substring.split(":");
            if (delaySplit[1] != null) delay = Integer.parseInt(delaySplit[1]);
            bossbar(sender, split[0], split[1], delaySplit[0], delay);
        }
    }
}
