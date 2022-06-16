package dream.soulflame.flamecore.commands;

import dream.soulflame.flamecore.FlameCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.utils.SendUtil.reColor;
import static dream.soulflame.flamecore.utils.SendUtil.reName;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;

public class MainCommand implements TabExecutor {

    private void reload(CommandSender sender, Plugin plugin) {
        for (String reload : getPlugin().getConfig().getStringList("Reload")) actions(sender, reload);
        getPlugin().reloadConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("flamecore.help")) for (String help : getPlugin().getConfig().getStringList("Help"))
                sender.sendMessage(reColor(reName(sender, help)));
            else for (String noPer : getPlugin().getConfig().getStringList("NoPermission")) actions(sender, noPer);
            return true;
        }
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("flamecore.reload")) reload(player, FlameCore.getPlugin());
                else for (String noPer : getPlugin().getConfig().getStringList("NoPermission")) actions(sender, noPer);
            } else reload(sender, FlameCore.getPlugin());
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return TabList.returnList(args, args.length);
    }
}
