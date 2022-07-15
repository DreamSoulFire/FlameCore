package dream.soulflame.flamecore.events;

import dream.soulflame.flamecore.fileloader.IDLoader;
import dream.soulflame.flamecore.utils.ItemUtil;
import dream.soulflame.flamecore.utils.SendUtil;
import dream.soulflame.flamecore.utils.SpecialUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDurability extends ItemUtil implements Listener {

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent e) {
        if (!IDLoader.idEnable) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player)) return;
        Player player = (Player) damager;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().equals(Material.AIR)) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return;
        List<String> lores = meta.getLore();
        boolean cantUse = false;
        boolean clearItem = false;
        boolean soonCantUse = false;
        int durability = 0;
        for (int i = 0; i < lores.size(); i++) {
            String lore = lores.get(i);
            String lore_noColor = SendUtil.stripColor(lore);
            if (!lore_noColor.contains(IDLoader.checkLore) || !lore_noColor.startsWith(IDLoader.checkLore)) continue;
            String str_noColor = lore_noColor.substring(IDLoader.checkLore.length());
            String[] split_noColor = str_noColor.split(IDLoader.getIdFileUtil().getString("Split", ""));
            if (!split_noColor[0].startsWith(" ")) {
                cantUse = true;
                continue;
            }
            double now = Double.parseDouble(split_noColor[0]);
            now -= 1;
            if (now <= IDLoader.getIdFileUtil().getDouble("Notice", 0)) {
                soonCantUse = true;
                durability = (int) now;
            }
            if (now == 0) {
                clearItem = true;
                continue;
            }
            String newDurability = String.valueOf(now);
            String[] nowSplit = newDurability.split("\\.");
            lores.set(i, lore.replace(split_noColor[0], " " + nowSplit[0]));
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        if (soonCantUse) {
            for (String soon : IDLoader.getIdFileUtil().getStringList("Message.SoonCantUse"))
                SpecialUtil.actions(player, soon.replace("<value>", String.valueOf(durability)));
        }
        if (cantUse) {
            SpecialUtil.actions(player, IDLoader.getIdFileUtil().getStringList("Message.LoreError"));
            e.setCancelled(true);
        }
        if (clearItem) {
            item.setAmount(0);
            item.setType(Material.AIR);
            SpecialUtil.actions(player, IDLoader.getIdFileUtil().getStringList("Message.ClearItem"));
        }
    }
}
