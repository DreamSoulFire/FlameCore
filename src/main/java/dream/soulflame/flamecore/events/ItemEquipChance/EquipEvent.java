package dream.soulflame.flamecore.events.ItemEquipChance;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

import static dream.soulflame.flamecore.fileloader.IECLoader.*;
import static dream.soulflame.flamecore.utils.ItemUtil.getLoreDouble;
import static dream.soulflame.flamecore.utils.ItemUtil.isEquip;
import static dream.soulflame.flamecore.utils.SendUtil.stripColor;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static org.bukkit.event.inventory.ClickType.LEFT;

public class EquipEvent implements Listener {

    private static final Random random = new Random();

    @EventHandler
    public void playerClickInv(InventoryClickEvent e) {
        HumanEntity whoClicked = e.getWhoClicked();
        if (!(whoClicked instanceof Player)) return;
        Player player = (Player) whoClicked;
        int slot = e.getSlot();
        ClickType click = e.getClick();
        double randomChance = random.nextDouble();
        if (click != LEFT) return;
        if (slot < 0 || slot > 35) return;
        Inventory top = e.getView().getTopInventory();
        if (!top.getType().equals(InventoryType.CRAFTING)) return;
        Inventory inventory = e.getView().getBottomInventory();
        ItemStack clickItem = inventory.getItem(slot);
        if (clickItem == null || Material.AIR.equals(clickItem.getType())) return;
        if (!isEquip(clickItem)) return;
        String name = clickItem.getType().name();
        ItemMeta itemMeta = clickItem.getItemMeta();
        if (itemMeta == null) return;
        if (!itemMeta.hasLore()) return;
        List<String> lore = itemMeta.getLore();
        for (String lores : lore) {
            if (!lores.contains(checkEquipChance)) continue;
            double pro = getLoreDouble(lore, checkEquipChance);
            pro += getLoreDouble(lore, checkAddChance);
            pro -= getLoreDouble(lore, checkReduceChance);
            if (getIecFileUtil().getBoolean("NameChance.Enable", false))
                for (String nameChance : getIecFileUtil().getStringList("NameChance.List")) {
                    String[] split = nameChance.split(getIecFileUtil().getString("NameChance.Split", ""));
                    for (ItemStack _item : inventory) {
                        if (_item == null || Material.AIR.equals(_item.getType())) continue;
                        if (clickItem != _item) continue;
                        ItemMeta _meta = _item.getItemMeta();
                        if (_meta == null) continue;
                        if (!_meta.hasDisplayName()) continue;
                        String displayName = _meta.getDisplayName();
                        displayName = stripColor(displayName);
                        if (displayName.contains(split[0])) pro = Double.parseDouble(split[1]);
                    }
                }
            for (ItemStack _item : inventory) {
                if (_item == null || Material.AIR.equals(_item.getType())) continue;
                if (_item.equals(clickItem)) continue;
                if (isEquip(_item)) continue;
                ItemMeta _itemMeta = _item.getItemMeta();
                if (_itemMeta == null) continue;
                if (!_itemMeta.hasLore()) continue;
                List<String> _lores = _itemMeta.getLore();
                for (String _lore : _lores) {
                    _lore = stripColor(_lore);
                    boolean containsAddChance = _lore.contains(checkAddChance);
                    boolean containsReduceChance = _lore.contains(checkReduceChance);
                    if (!containsAddChance && !containsReduceChance) continue;
                    pro += getLoreDouble(_lores, checkAddChance);
                    pro -= getLoreDouble(_lores, checkReduceChance);
                    int _amount = _item.getAmount();
                    _item.setAmount(_amount - 1);
                }
            }
            ItemStack headSlot = inventory.getItem(39);
            ItemStack chestSlot = inventory.getItem(38);
            ItemStack legSlot = inventory.getItem(37);
            ItemStack bootSlot = inventory.getItem(36);
            if (headSlot != null || chestSlot != null || legSlot != null || bootSlot != null) return;
            inventory.setItem(slot, new ItemStack(Material.AIR));
            if ((pro / 100) >= randomChance) {
                if (name.contains("HELMET")) inventory.setItem(39, clickItem);
                else if (name.contains("CHESTPLATE")) inventory.setItem(38, clickItem);
                else if (name.contains("LEGGINGS")) inventory.setItem(37, clickItem);
                else if (name.contains("BOOTS")) inventory.setItem(36, clickItem);
                else return;
                actions(player, equipSuccessMsg);
            } else {
                boolean saveItem = false;
                actions(player, equipFailMsg);
                for (ItemStack _item : inventory) {
                    if (_item == null || _item.getType().equals(Material.AIR)) continue;
                    if (isEquip(_item)) continue;
                    List<String> _lore = _item.getItemMeta().getLore();
                    if (_lore == null) continue;
                    for (String loreList : _lore) if (stripColor(loreList).contains(checkSaveItem)) {
                        saveItem = true;
                        int _amount = _item.getAmount();
                        _item.setAmount(_amount - 1);
                    }
                }
                if (saveItem) {
                    actions(player, equipSaveMsg);
                    inventory.addItem(clickItem);
                }
            }
        }

    }

}
