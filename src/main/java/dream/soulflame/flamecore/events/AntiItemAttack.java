package dream.soulflame.flamecore.events;

import dream.soulflame.flamecore.fileloader.AIALoader;
import dream.soulflame.flamecore.utils.SendUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AntiItemAttack implements Listener {

    @EventHandler
    public static void itemAttack(EntityDamageByEntityEvent e) {
        if (!AIALoader.aiaEnable) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player)) return;
        Player player = (Player) damager;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand == null || itemInMainHand.getType().equals(Material.AIR)) return;
        ItemMeta itemMeta = itemInMainHand.getItemMeta();
        List<String> itemList = AIALoader.getAiaFileUtil().getStringList("CheckList");
        boolean cantAttack = false;
        for (String item : itemList) {
            String[] split = item.split("<->");
            if (split.length < 2) continue;
            if ("lore".equalsIgnoreCase(split[0])) {
                if (!itemMeta.hasLore()) continue;
                List<String> lores = itemMeta.getLore();
                for (String lore : lores) {
                    String line = SendUtil.stripColor(lore);
                    if (!line.contains(split[1])) continue;
                    cantAttack = true;
                }
            } else if ("name".equalsIgnoreCase(split[0])) {
                if (!itemMeta.hasDisplayName()) continue;
                String displayName = itemMeta.getDisplayName();
                String name = SendUtil.stripColor(displayName);
                if (!name.contains(split[1])) continue;
                cantAttack = true;
            } else if ("material".equalsIgnoreCase(split[0])) {
                String material = itemInMainHand.getType().toString();
                if (!material.contains(split[1])) continue;
                cantAttack = true;
            }
        }
        if (cantAttack) e.setCancelled(true);
    }

}
