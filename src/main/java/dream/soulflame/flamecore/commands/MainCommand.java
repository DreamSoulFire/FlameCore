package dream.soulflame.flamecore.commands;

import dream.soulflame.flamecore.utils.SendUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;

public class MainCommand implements TabExecutor {

    private void reload(CommandSender sender) {
        actions(sender, getPlugin().getConfig().getStringList("Reload"));
        getPlugin().reloadConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("flamecore.help"))
                SendUtil.message(sender, getPlugin().getConfig().getStringList("Help"));
            else actions(sender, getPlugin().getConfig().getStringList("NoPermission"));
            return true;
        }
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("flamecore.reload")) reload(player);
                else actions(sender, getPlugin().getConfig().getStringList("NoPermission"));
            } else reload(sender);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return TabList.returnList(args, args.length);
    }
}
