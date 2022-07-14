package dream.soulflame.flamecore.events;

import dream.soulflame.flamecore.fileloader.AFELoader;
import dream.soulflame.flamecore.utils.SpecialUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AntiFireEntity implements Listener {

    @EventHandler
    public static void entityFire(EntityCombustEvent e) {
        if (!AFELoader.afeEnable) return;
        for (String antis : AFELoader.getAfeFileUtil().getStringList("Entities")) {
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
        if (!AFELoader.afeEnable || !AFELoader.getAfeFileUtil().getBoolean("Debug", false)) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player)) return;
        Player player = (Player) damager;
        for (String info : AFELoader.getAfeFileUtil().getStringList("Message.DebugInfo"))
            SpecialUtil.actions(player, info.replace("<entity>", e.getEntityType().toString()));
    }

}
