package dream.soulflame.flamecore.events;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.fileloader.SICLoader.*;
import static dream.soulflame.flamecore.utils.ItemUtil.getLoreDouble;
import static dream.soulflame.flamecore.utils.SendUtil.stripColor;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static org.bukkit.Bukkit.getScheduler;

public class SwapItemCooldown implements Listener {

    private static final List<Player> cdList = new ArrayList<>();

    @EventHandler
    public void swapEvent(PlayerItemHeldEvent e) {
        if (!sicEnable) return;
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        int heldItemSlot = e.getNewSlot();
        ItemStack item = inventory.getItem(heldItemSlot);
        if (item == null || Material.AIR.equals(item.getType())) {
            if (!perResolveEnable) return;
            for (String perResolve : perResolveList)
                if (player.hasPermission(perResolve)) cdList.remove(player);
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            if (!perResolveEnable) return;
            for (String perResolve : perResolveList)
                if (player.hasPermission(perResolve)) cdList.remove(player);
            return;
        }
        double cooldown = 0;
        String[] splits;
        if (checkLoreEnable) {
            if (!itemMeta.hasLore()) return;
            List<String> itemLore = itemMeta.getLore();
            for (String lores : itemLore) {
                String stripColorLore = stripColor(lores);
                if (!stripColorLore.contains(lore)) continue;
                cdList.add(player);
            }
            cooldown += getLoreDouble(itemLore, lore);
        }
        if (checkNameEnable) {
            if (!itemMeta.hasDisplayName()) return;
            String displayName = itemMeta.getDisplayName();
            String stripColorName = stripColor(displayName);
            for (String name : getSicFileUtil().getStringList("Setting.Name.CheckList")) {
                splits = name.split(splitChar);
                if (!stripColorName.contains(splits[0])) continue;
                cdList.add(player);
                cooldown += Double.parseDouble(splits[1]);
            }
        }
        if (getSicFileUtil().getBoolean("Permission.Enable", false)) for (String permission : getSicFileUtil().getStringList("Permission.List")) {
            splits = permission.split(splitChar);
            if (!player.hasPermission(splits[0])) continue;
            cooldown -= Double.parseDouble(splits[1]);
        }
        getScheduler().runTaskLaterAsynchronously(getPlugin(),
                ()-> cdList.remove(player),
                (long) (cooldown * 20));
    }

    @EventHandler
    public void attackEvent(EntityDamageByEntityEvent e) {
        if (!sicEnable) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player)) return;
        if (!cdList.contains(damager)) return;
        for (String cdMsg : inCooldown) actions(damager, cdMsg);
        e.setCancelled(true);
    }

}
