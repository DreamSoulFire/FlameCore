package dream.soulflame.flamecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static org.bukkit.Bukkit.createBossBar;
import static org.bukkit.Bukkit.getScheduler;

public class SendUtil {
    public static void message(CommandSender sender, List<String> texts) {
        for (String text : texts) sender.sendMessage(reColor(reName(sender, text)));
    }
    public static void message(CommandSender sender, String text) {
        sender.sendMessage(reColor(reName(sender, text)));
    }
    public static void message(String text) {
        Bukkit.getConsoleSender().sendMessage(reColor(text));
    }
    public static void message(CommandSender sender, String text, int delay) {
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> sender.sendMessage(reColor(reName(sender, text))), delay);
    }
    public static void title(CommandSender sender, String main, String sub, int fadeIn, int stay, int fadeOut, int delay) {
        Player player = (Player) sender;
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> player.sendTitle(
                        reColor(reName(sender, main)),
                        reColor(reName(sender, sub)),
                        fadeIn,
                        stay,
                        fadeOut
                ),
                delay);
    }
    public static void bossbar(CommandSender sender, String title, String color, String style, int delay) {
        Player player = (Player) sender;
        BossBar bossBar = createBossBar(reColor(reName(player, title)), BarColor.valueOf(color), BarStyle.valueOf(style), BarFlag.PLAY_BOSS_MUSIC);
        bossBar.addPlayer(player);
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> bossBar.removePlayer(player), delay);
    }

    public static void actionBar(CommandSender sender, String text,int delay) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        String version = SpecialUtil.version();
        Class<?> aClass_Packet = Class.forName("net.minecraft.server." + version + ".Packet");
        Class<?> aClass_PlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
        Class<?> aClass_ChatMessageType = Class.forName("net.minecraft.server." + version + ".ChatMessageType");
        Class<?> aClass_EntityPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
        Class<?> aClass_IChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
        Class<?> aClass_IChatBaseComponent$ChatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
        Class<?> aClass_PacketPlayOutChat = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
        Class<?> aClass_CraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        Method aMethod_IChatBaseComponent$ChatSerializer_a = aClass_IChatBaseComponent$ChatSerializer.getDeclaredMethod("a", String.class);
        Object object_msg = aMethod_IChatBaseComponent$ChatSerializer_a.invoke(null, "{\"text\":\"" + reColor(text) + "\"}");
        Constructor<?> constructor = aClass_PacketPlayOutChat.getConstructor(aClass_IChatBaseComponent, aClass_ChatMessageType);
        Object enum_GAME_INFO = aClass_ChatMessageType.getEnumConstants()[2];
        Object packet = constructor.newInstance(object_msg, enum_GAME_INFO);
        Method aMethod_getHandle = aClass_CraftPlayer.getDeclaredMethod("getHandle");
        Object object_EntityPlayer = aMethod_getHandle.invoke(sender);
        Field field_playerConnection = aClass_EntityPlayer.getField("playerConnection");
        Method aMethod_sendPacket = aClass_PlayerConnection.getDeclaredMethod("sendPacket", aClass_Packet);
        Object object_playerConnection = field_playerConnection.get(object_EntityPlayer);
        getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> {
            try {
                aMethod_sendPacket.invoke(object_playerConnection,packet);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }, delay);
    }

    public static String reColor(String text) {
        return ChatColor.translateAlternateColorCodes('&',text);
    }

    public static List<String> reColor(List<String> text) {
        List<String> list = new ArrayList<>();
        for (String texts : text)
            list.add(ChatColor.translateAlternateColorCodes('&', texts));
        return list;
    }
    public static String reName(CommandSender sender, String text) {
        return text.replace("<player>", sender.getName());
    }
    public static List<String> stripColor(List<String> text) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.stripColor(text.toString()));
        return list;
    }
    public static String stripColor(String text) {
        return ChatColor.stripColor(text);
    }
}
