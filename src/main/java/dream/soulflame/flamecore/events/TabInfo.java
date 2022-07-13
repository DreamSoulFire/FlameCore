package dream.soulflame.flamecore.events;

import eos.moe.dragoncore.api.event.EntityJoinWorldEvent;
import eos.moe.dragoncore.api.event.EntityLeaveWorldEvent;
import eos.moe.dragoncore.api.worldtexture.WorldTexture;
import eos.moe.dragoncore.network.PacketSender;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.fileloader.TILoader.getTiFileUtil;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getScheduler;

public class TabInfo implements Listener {
    public static final Map<Player, WorldTexture[]> playerWorldTextureMap = new HashMap<>();

    public TabInfo() {
        getScheduler().runTaskTimerAsynchronously(getPlugin(), this::run, 20, 20);
    }

    public static void reload() {
        playerWorldTextureMap.clear();
        Bukkit.getOnlinePlayers().forEach(TabInfo::build);
    }

    private static void build(Player player) {
        List<WorldTexture> worldTextures = new ArrayList<>();
        ConfigurationSection playerInfoSection = getTiFileUtil().getConfigurationSection("PlayerInfo");
        Set<String> keys = playerInfoSection.getKeys(false);
        for (String key : keys) {
            ConfigurationSection section = playerInfoSection.getConfigurationSection(key);
            double x = section.getDouble("X", 0);
            double y = section.getDouble("Y", 0);
            double z = section.getDouble("Z", 0);
            float pitch = (float) section.getDouble("Pitch", 0);
            float yaw = (float) section.getDouble("Yaw", 0);
            float size = (float) section.getDouble("Size", 0);
            String text = section.getString("Text", "");
            worldTextures.add(buildWorldTexture(player, text, x, y, z, pitch, yaw, size));
        }
        WorldTexture[] textures = new WorldTexture[worldTextures.size()];
        worldTextures.toArray(textures);
        playerWorldTextureMap.put(player, textures);
    }

    private static WorldTexture buildWorldTexture(Player player, String info, double x, double y, double z, float rotateX, float rotateY, float size) {
        WorldTexture worldTexture = new WorldTexture();
        worldTexture.world = player.getWorld().getName();
        worldTexture.translateX = x;
        worldTexture.translateY = y;
        worldTexture.translateZ = z;
        worldTexture.path = "[text]" + PlaceholderAPI.setPlaceholders(player, info);
        worldTexture.rotateX = rotateX;
        worldTexture.rotateY = rotateY;
        worldTexture.rotateZ = 0;
        worldTexture.width = size;
        worldTexture.height = size;
        worldTexture.alpha = 0;
        worldTexture.followPlayerEyes = true;
        worldTexture.followEntityDirection = true;
        worldTexture.glow = true;
        worldTexture.entity = player.getUniqueId();
        return worldTexture;
    }

    @EventHandler
    public void join(EntityJoinWorldEvent event) {
        Player player = event.getPlayer();
        build(player);
    }

    @EventHandler
    public void quit(EntityLeaveWorldEvent event) {
        if (playerWorldTextureMap.get(event.getPlayer()) == null) return;
        playerWorldTextureMap.remove(event.getPlayer());
    }

    private void run() {
        for (Player player : getOnlinePlayers())
            for (Map.Entry<Player, WorldTexture[]> entry : playerWorldTextureMap.entrySet()) {
                Player k = entry.getKey();
                WorldTexture[] v = entry.getValue();
                if (!getTiFileUtil().getBoolean("ShowSelf", false) && player == k) continue;
                int index = 0;
                for (WorldTexture worldTexture : v) {
                    PacketSender.setPlayerWorldTexture(player, k.getUniqueId().toString() + index, worldTexture);
                    index++;
                }
            }
    }
}
