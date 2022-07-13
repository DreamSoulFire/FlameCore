package dream.soulflame.flamecore.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.List;

import static dream.soulflame.flamecore.commands.MainCommand.toggleMode;
import static dream.soulflame.flamecore.fileloader.ADLoader.*;
import static dream.soulflame.flamecore.utils.SendUtil.stripColor;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;

public class AntiDrop implements Listener {

    @EventHandler
    public static void antiDropItemEvent(PlayerDropItemEvent e) {
        if (!adEnable || !getAdFileUtil().getBoolean("Lore.Enable", false)) return;
        Player player = e.getPlayer();
        if (player.isOp()) return;
        if (skipPerEnable && player.hasPermission(getAdFileUtil().getString("Skip.Permission.Lore", ""))) return;
        List<String> itemLore = e.getItemDrop().getItemStack().getItemMeta().getLore();
        if (itemLore == null) return;
        a: for (String loreLists : getAdFileUtil().getStringList("Lore.List")) {
            List<String> stripColorLore = stripColor(itemLore);
            for (String ignoreLore : stripColorLore) {
                if (!ignoreLore.contains(loreLists)) continue a;
                actions(player, normalMsg);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void antiDropWorldEvent(PlayerDropItemEvent e) {
        if (!adEnable || !getAdFileUtil().getBoolean("World.Enable", false)) return;
        Player player = e.getPlayer();
        String worldName = player.getWorld().getName();
        if (player.isOp()) return;
        if (skipPerEnable && player.hasPermission(getAdFileUtil().getString("Skip.Permission.World", ""))) return;
        if (!getAdFileUtil().getStringList("World.List").contains(worldName)) return;
        actions(player, getAdFileUtil().getStringList("Message.World"));
        e.setCancelled(true);
    }

    @EventHandler
    public static void antiDropNameEvent(PlayerDropItemEvent e) {
        if (!adEnable || !getAdFileUtil().getBoolean("Name.Enable", false)) return;
        Player player = e.getPlayer();
        String itemName = e.getItemDrop().getItemStack().getItemMeta().getDisplayName();
        if (player.isOp()) return;
        if (skipPerEnable && player.hasPermission(getAdFileUtil().getString("Skip.Permission.Name", ""))) return;
        if (itemName == null) return;
        for (String nameList : getAdFileUtil().getStringList("Name.List")) {
            String stripColorName = stripColor(itemName);
            if (!stripColorName.contains(nameList)) return;
            actions(player, normalMsg);
            e.setCancelled(true);
        }

    }

    @EventHandler
    public static void antiDropMaterialEvent(PlayerDropItemEvent e) {
        if (!adEnable || !getAdFileUtil().getBoolean("Material.Enable", false)) return;
        Player player = e.getPlayer();
        Material itemMaterial = e.getItemDrop().getItemStack().getType();
        int itemId = e.getItemDrop().getItemStack().getTypeId();
        if (player.isOp()) return;
        if (skipPerEnable && player.hasPermission(getAdFileUtil().getString("Skip.Permission.Material", ""))) return;
        if (itemMaterial.equals(Material.AIR) || itemId < 0) return;
        if (getAdFileUtil().getStringList("Material.String.List").contains(itemMaterial.name())) {
            actions(player, normalMsg);
            e.setCancelled(true);
        } else if (getAdFileUtil().getIntegerList("Material.Integer.List").contains(itemId)) {
            actions(player, normalMsg);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void toggleDropMode(PlayerDropItemEvent e) {
        if (!adEnable || !toggleModeEnable) return;
        Player player = e.getPlayer();
        if (toggleMode.contains(player)) return;
        actions(player, getAdFileUtil().getStringList("Message.ToggleMode"));
        e.setCancelled(true);
    }

}
