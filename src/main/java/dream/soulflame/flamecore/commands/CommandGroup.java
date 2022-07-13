package dream.soulflame.flamecore.commands;

import dream.soulflame.flamecore.fileloader.RCGLoader;
import dream.soulflame.flamecore.utils.CommandUtil;
import dream.soulflame.flamecore.utils.ModuleFileUtil;
import dream.soulflame.flamecore.utils.SpecialUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

import static dream.soulflame.flamecore.utils.SendUtil.stripColor;

public class CommandGroup {
    private static final Random random = new Random();

    public static void build(String args, Player player) {
        if (!RCGLoader.rcgEnable) return;
        ModuleFileUtil rcgFileUtil = RCGLoader.getRcgFileUtil();
        ConfigurationSection group = rcgFileUtil.getConfigurationSection("CommandGroup");
        for (String key : group.getKeys(false)) {
            if (!key.equalsIgnoreCase(args)) continue;
            ConfigurationSection section = group.getConfigurationSection(key);
            double chance = section.getDouble("Chance", 0.0);
            String permission = section.getString("Permission", "");
            List<String> commands = section.getStringList("Commands");
            List<String> successMsg = section.getStringList("Success");
            List<String> failMsg = section.getStringList("Fail");
            if (!player.hasPermission(permission)) continue;
            if (rcgFileUtil.getBoolean("Special.Item.Enable", false)) {
                List<String> items = rcgFileUtil.getStringList("Special.Item.List");
                for (String item : items) {
                    String[] split = item.split("<->");
                    if (split[0].equalsIgnoreCase("permission")) {
                        if (!player.hasPermission(split[1])) continue;
                        chance += Double.parseDouble(split[2]);
                    }
                    for (ItemStack itemStack : player.getInventory()) {
                        if (itemStack == null || Material.AIR.equals(itemStack.getType())) continue;
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (split[0].equalsIgnoreCase("name")) {
                            if (!itemMeta.hasDisplayName()) continue;
                            String name = itemMeta.getDisplayName();
                            if (!stripColor(name).contains(split[1])) continue;
                            chance += Double.parseDouble(split[2]);
                        }
                        if (split[0].equalsIgnoreCase("lore")) {
                            if (!itemMeta.hasLore()) continue;
                            List<String> lore = itemMeta.getLore();
                            for (String line : lore) {
                                if (!stripColor(line).contains(split[1])) continue;
                                chance += Double.parseDouble(split[2]);
                            }
                        }
                    }
                }
            }
            double randomChance = random.nextDouble();
            if (chance >= randomChance * 100) {
                if (commands != null) CommandUtil.commands(player, commands);
                if (successMsg != null) SpecialUtil.actions(player, successMsg);
            } else {
                if (failMsg == null) continue;
                SpecialUtil.actions(player, failMsg);
            }
        }
    }
}
