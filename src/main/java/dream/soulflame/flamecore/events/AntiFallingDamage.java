package dream.soulflame.flamecore.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiFallingDamage implements Listener {

    @EventHandler
    public void entityFallingDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (!entity.getType().equals(EntityType.PLAYER)) return;
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        e.setCancelled(true);
    }

}
