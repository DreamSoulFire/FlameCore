package dream.soulflame.flamecore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.FlameCore.start;
import static dream.soulflame.flamecore.utils.SendUtil.reColor;

public class AntiSpeedJoin implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        long join = System.currentTimeMillis();
        int antiTime = getPlugin().getConfig().getInt("AntiSpeedJoin.AntiTime", 0);
        String antiTimeMsg = getPlugin().getConfig().getString("AntiSpeedJoin.AntiTimeMsg", "");
        long pre = join - start;
        long anti = antiTime * 1000L;
        if (pre >= anti) return;
        long now = (anti - pre) / 1000L;
        player.kickPlayer(reColor(antiTimeMsg.replace("<time>", String.valueOf(now))));
    }

}
