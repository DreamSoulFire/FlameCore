package dream.soulflame.flamecore.events;

import dream.soulflame.flamecore.fileloader.ASJLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static dream.soulflame.flamecore.FlameCore.start;
import static dream.soulflame.flamecore.utils.SendUtil.reColor;

public class AntiSpeedJoin implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        if (!ASJLoader.asjEnable) return;
        Player player = e.getPlayer();
        long join = System.currentTimeMillis();
        int antiTime = ASJLoader.getAsjFileUtil().getInt("AntiTime", 0);
        String antiTimeMsg = ASJLoader.getAsjFileUtil().getString("Message.AntiTime", "");
        long pre = join - start;
        long anti = antiTime * 1000L;
        if (pre >= anti) return;
        long now = (anti - pre) / 1000L;
        player.kickPlayer(reColor(antiTimeMsg.replace("<time>", String.valueOf(now))));
    }

}
