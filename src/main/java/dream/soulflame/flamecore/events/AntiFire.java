package dream.soulflame.flamecore.events;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.SendUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AntiFire implements Listener {

    @EventHandler
    public static void entityFire(EntityCombustEvent e) {
        for (String antis : FlameCore.getPlugin().getConfig().getStringList("AntiFire.List")) {
            String[] split = antis.split("<->");
            if ("name".equalsIgnoreCase(split[0])) {
                if (split.length < 2) continue;
                String name = e.getEntity().getName();
                if (!name.contains(split[1])) continue;
                e.setCancelled(true);
            } else if ("entity".equalsIgnoreCase(split[0])) {
                if (split.length < 2) continue;
                EntityType type = e.getEntityType();
                if (!type.equals(EntityType.valueOf(split[1]))) continue;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void entityDamage(EntityDamageByEntityEvent e) {
        if (!FlameCore.getPlugin().getConfig().getBoolean("AntiFire.Debug", false)) return;
        Entity damager = e.getDamager();
        EntityType type = damager.getType();
        if (!type.equals(EntityType.PLAYER)) return;
        Player player = (Player) damager;
        SendUtil.message(player, "你攻击的实体是: " + e.getEntityType().toString(), 0);
    }

}
