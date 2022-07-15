package dream.soulflame.flamecore.commands;

import dream.soulflame.flamecore.events.*;
import dream.soulflame.flamecore.events.ItemEquipChance.EquipEvent;
import dream.soulflame.flamecore.events.ItemEquipChance.IdentifierEvent;
import dream.soulflame.flamecore.fileloader.*;
import dream.soulflame.flamecore.utils.SendUtil;
import dream.soulflame.flamecore.utils.SlotUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.commands.CommandGroup.build;
import static dream.soulflame.flamecore.fileloader.ADLoader.*;
import static dream.soulflame.flamecore.fileloader.AFDLoader.afdEnable;
import static dream.soulflame.flamecore.fileloader.AFELoader.afeEnable;
import static dream.soulflame.flamecore.fileloader.AIALoader.aiaEnable;
import static dream.soulflame.flamecore.fileloader.ASJLoader.asjEnable;
import static dream.soulflame.flamecore.fileloader.IECLoader.iecEnable;
import static dream.soulflame.flamecore.fileloader.RCGLoader.rcgEnable;
import static dream.soulflame.flamecore.fileloader.SFLoader.sfEnable;
import static dream.soulflame.flamecore.fileloader.SICLoader.sicEnable;
import static dream.soulflame.flamecore.fileloader.TILoader.tiEnable;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static org.bukkit.Bukkit.getPluginManager;

public class MainCommand implements TabExecutor {

    public static List<Player> toggleMode = new ArrayList<>();

    private void reload(CommandSender sender) {
        actions(sender, getPlugin().getConfig().getStringList("Command.Reload"));
        getPlugin().reloadConfig();
        if (adEnable) {
            getPluginManager().registerEvents(new AntiDrop(), getPlugin());
            ADLoader.reload();
        }
        if (afdEnable) {
            getPluginManager().registerEvents(new AntiFallingDamage(), getPlugin());
            AFDLoader.reload();
        }
        if (afeEnable) {
            getPluginManager().registerEvents(new AntiFireEntity(), getPlugin());
            AFELoader.reload();
        }
        if (aiaEnable) {
            getPluginManager().registerEvents(new AntiItemAttack(), getPlugin());
            AIALoader.reload();
        }
        if (asjEnable) {
            getPluginManager().registerEvents(new AntiSpeedJoin(), getPlugin());
            ASJLoader.reload();
        }
        if (IDLoader.idEnable) {
            getPluginManager().registerEvents(new ItemDurability(), getPlugin());
            IDLoader.reload();
        }
        if (iecEnable) {
            getPluginManager().registerEvents(new EquipEvent(), getPlugin());
            if (getPluginManager().isPluginEnabled("DragonCore"))
                getPluginManager().registerEvents(new IdentifierEvent(), getPlugin());
            IECLoader.reload();
        }
        if (rcgEnable) RCGLoader.reload();
        if (sfEnable) SFLoader.reload();
        if (sicEnable) {
            getPluginManager().registerEvents(new SwapItemCooldown(), getPlugin());
            SICLoader.reload();
        }
        if (tiEnable) {
            getPluginManager().registerEvents(new TabInfo(), getPlugin());
            TabInfo.reload();
            TILoader.reload();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("flamecore.help")) {
                SendUtil.message(sender, getPlugin().getConfig().getStringList("Command.Help.Main"));
                if (adEnable) SendUtil.message(sender, getPlugin().getConfig().getStringList("Command.Help.AntiDrop"));
                if (sfEnable) SendUtil.message(sender, getPlugin().getConfig().getStringList("Command.Help.SlotFunctions"));
                if (rcgEnable) SendUtil.message(sender, getPlugin().getConfig().getStringList("Command.Help.RandomCommandGroup"));
                SendUtil.message(sender, getPlugin().getConfig().getStringList("Command.Help.Tips"));
            }
            else actions(sender, getPlugin().getConfig().getStringList("Command.NoPermission"));
            return true;
        }

        if ("reload".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("flamecore.reload")) reload(player);
                else actions(sender, getPlugin().getConfig().getStringList("Command.NoPermission"));
            } else reload(sender);
            return true;
        }

        if (args.length == 1) {
            if ("check".equalsIgnoreCase(args[0]) || "run".equalsIgnoreCase(args[0]) ||
                    "get".equalsIgnoreCase(args[0]) || "set".equalsIgnoreCase(args[0]) ||
                    "change".equalsIgnoreCase(args[0])) {
                actions(sender, getPlugin().getConfig().getStringList("Command.ArgsNoEnough"));
                return true;
            }

            if ("toggle".equalsIgnoreCase(args[0])) {
                if (!toggleModeEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (!player.hasPermission(getAdFileUtil().getString("ToggleDropMode.Permission", ""))) {
                        actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                        return true;
                    }
                    if (toggleMode.contains(player)) {
                        toggleMode.remove(player);
                        actions(player, getAdFileUtil().getStringList("Message.CloseToggle"));
                        return true;
                    }
                    toggleMode.add(player);
                    if (getAdFileUtil().getBoolean("ToggleDropMode.Delay.Enable", false))  {
                        Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(),
                                () -> toggleMode.remove(player), getAdFileUtil().getInt("ToggleDropMode.Delay.Time", 0));
                        if (!getAdFileUtil().getBoolean("ToggleDropMode.Delay.Notice.Enable", false)) return true;
                        actions(player, getAdFileUtil().getStringList("Message.AutoClose"));
                    }
                    actions(player, getAdFileUtil().getStringList("Message.OpenToggle"));
                    return true;
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
                return true;
            }
        }

        if (args.length == 2) {
            if ("check".equalsIgnoreCase(args[0])) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    ItemStack handItem = player.getInventory().getItemInMainHand();
                    MaterialData materialData = handItem.getData();
                    Material itemType = materialData.getItemType();
                    if (player.hasPermission("flamecore.check")) switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "material":
                            if (itemType != Material.AIR)
                                for (String materialMsg : getPlugin().getConfig().getStringList("CheckItem.CorrectTexture"))
                                    actions(player, materialMsg
                                            .replace("<texture>", itemType.name())
                                            .replace("<mode>", "英文材质") + ":" + materialData.getData());
                            else actions(player, getPlugin().getConfig().getStringList("Command.CheckItem.NoItem"));
                            break;
                        case "id":
                            if (itemType != Material.AIR)
                                for (String idMsg : getPlugin().getConfig().getStringList("CheckItem.CorrectTexture"))
                                    actions(player, idMsg
                                            .replace("<id>", String.valueOf(itemType.getId()))
                                            .replace("<mode>", "数字ID") + ":" + materialData.getData());
                            else actions(player, getPlugin().getConfig().getStringList("Command.CheckItem.NoItem"));
                            break;
                        default:
                            actions(player, getPlugin().getConfig().getStringList("Command.ArgsNoEnough"));
                            break;
                    }
                    else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                    return true;
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
                return true;
            }

            if ("run".equalsIgnoreCase(args[0])) {
                if (!rcgEnable) return true;
                if (sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    if (player.hasPermission("flamecore.run")) build(args[1], player);
                    else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
                return true;
            }

            if ("toggle".equalsIgnoreCase(args[0])) {
                if (!toggleModeEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    boolean online = false;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        String name = onlinePlayer.getName();
                        if (!args[1].equalsIgnoreCase(name)) continue;
                        Player target = Bukkit.getPlayer(args[1]);
                        if (!player.hasPermission(getAdFileUtil().getString("ToggleDropMode.Permission", ""))) {
                            actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                            continue;
                        }
                        if (toggleMode.contains(target)) {
                            toggleMode.remove(target);
                            actions(target, getAdFileUtil().getStringList("Message.CloseToggle"));
                            continue;
                        }
                        toggleMode.add(target);
                        if (getAdFileUtil().getBoolean("ToggleDropMode.Delay.Enable", false))  {
                            Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(),
                                    () -> toggleMode.remove(target), getAdFileUtil().getInt("ToggleDropMode.Delay.Time", 0));
                            if (!getAdFileUtil().getBoolean("ToggleDropMode.Delay.Notice.Enable", false)) continue;
                            actions(target, getAdFileUtil().getStringList("Message.AutoClose"));
                        }
                        actions(target, getAdFileUtil().getStringList("Message.OpenToggle"));
                        online = true;
                    }
                    if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
                return true;
            }

            if ("get".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameslotfunction.get"))
                        SlotUtil.getSlot(player, player, args[1]);
                    else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
            }

            if ("set".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameslotfunction.set"))
                        SlotUtil.setSlot(player, player, args[1]);
                    else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
            }

            if ("change".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                actions(sender, getPlugin().getConfig().getStringList("Command.ArgsNoEnough"));
            }
        }

        if (args.length == 3) {
            if ("run".equalsIgnoreCase(args[0])) {
                if (!rcgEnable) return true;
                if (sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    if (player.hasPermission("randomcommandgroup.run")) {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[1]);
                            build(args[1], target);
                            online = true;
                        }
                        if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    } else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
                return true;
            }

            if ("change".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameslotfunction.change"))
                        SlotUtil.changeSlot(player, player, args[1], args[2]);
                    else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
            }

            if ("get".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameslotfunction.get")) {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[1]);
                            SlotUtil.getSlot(player, target, args[2]);
                            online = true;
                        }
                        if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    } else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
            }

            if ("set".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameslotfunction.set")) {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[1]);
                            SlotUtil.setSlot(player, target, args[2]);
                            online = true;
                        }
                        if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    } else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
            }

        }

        if (args.length == 4) {
            if ("change".equalsIgnoreCase(args[0])) {
                if (!sfEnable) return true;
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameslotfunction.change")) {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[1]);
                            SlotUtil.changeSlot(player, target, args[2], args[3]);
                            online = true;
                        }
                        if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    } else actions(player, getPlugin().getConfig().getStringList("Command.NoPermission"));
                } else actions(sender, getPlugin().getConfig().getStringList("Command.CantConsole"));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return TabList.returnList(args, args.length);
    }
}
