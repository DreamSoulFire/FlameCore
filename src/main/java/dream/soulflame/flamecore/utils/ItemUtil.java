package dream.soulflame.flamecore.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dream.soulflame.flamecore.utils.SendUtil.reColor;
import static dream.soulflame.flamecore.utils.SendUtil.stripColor;

public class ItemUtil {
    public static void splitUtil(List<String> slotList, Set<Integer> slotSet) {
        for ( String slots : slotList ) if (slots.contains("-")) {
            String[] split = slots.split("-");
            int min = Integer.parseInt(split[0]);
            int max = Integer.parseInt(split[1]);
            for (int i = min; i <= max; i++) slotSet.add(i);
        } else slotSet.add(Integer.parseInt(slots));
    }

    public static ItemStack spawnItem(String material, List<String> loreList, String itemName) {

        String[] split = material.split(":");
        int data = 0;
        if (split.length > 1) data = Integer.parseInt(split[1]);
        Material _material = Material.matchMaterial(split[0]);
        if(_material == null) _material = Material.STONE;
        ItemStack item = new ItemStack(_material);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(reColor(itemName));
        meta.setLore(reColor(loreList));
        item.setItemMeta(meta);
        return item;

    }

    public static void putItem(Inventory inventory, Set<Integer> stringSet, ItemStack item) {
        for (Integer slots : stringSet) inventory.setItem(slots, item);
    }
    public static boolean isEquip(ItemStack itemStack) {
        return isEquip(itemStack.getType());
    }

    private static boolean isEquip(Material material) {
        return isEquip(material.name());
    }

    private static boolean isEquip(String name) {
        return (name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS"));
    }

    public static double getLoreDouble(List<String> lore, String regex) {
        double max = 0.0D;
        if (lore.isEmpty() || regex == null) return max;
        String line = stripColor(lore.toString());
        Pattern compile = Pattern.compile(regex + ":? *([\\d.]+)");
        Matcher matcher = compile.matcher(line);
        while (matcher.find()) {
            String group = matcher.group(1);
            max += Double.parseDouble(group);
        }
        return max;
    }
}
