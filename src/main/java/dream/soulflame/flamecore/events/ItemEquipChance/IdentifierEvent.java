package dream.soulflame.flamecore.events.ItemEquipChance;

import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

import static dream.soulflame.flamecore.fileloader.IECLoader.*;
import static dream.soulflame.flamecore.utils.ItemUtil.getLoreDouble;
import static dream.soulflame.flamecore.utils.SendUtil.stripColor;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;

public class IdentifierEvent implements Listener {

    private static final Random random = new Random();

    @EventHandler
    public void dragonEvent(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        String identifier = e.getIdentifier();
        ItemStack item = e.getItemStack();
        double randomChance = random.nextDouble();
        if (item == null) return;
        Material type = item.getType();
        if (Material.AIR.equals(type)) return;
        if (identifier == null) return;
        for (String identifiers : getIecFileUtil().getStringList("Identifier")) {
            if (!identifier.equalsIgnoreCase(identifiers)) continue;
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) continue;
            if (!itemMeta.hasLore()) continue;
            List<String> lore = itemMeta.getLore();
            for (String lores : lore) {
                if (!lores.contains(checkEquipChance)) continue;
                double pro = getLoreDouble(lore, checkEquipChance);
                pro += getLoreDouble(lore, checkAddChance);
                pro -= getLoreDouble(lore, checkReduceChance);
                PlayerInventory inventory = player.getInventory();
                if (getIecFileUtil().getBoolean("NameChance.Enable", false))
                    for (String nameChance : getIecFileUtil().getStringList("NameChance.List")) {
                        String[] split = nameChance.split(getIecFileUtil().getString("NameChance.Split", ""));
                        for (ItemStack _item : inventory) {
                            if (_item == null || Material.AIR.equals(_item.getType())) continue;
                            if (item != _item) continue;
                            ItemMeta _meta = _item.getItemMeta();
                            if (_meta == null) continue;
                            if (!_meta.hasDisplayName()) continue;
                            String displayName = _meta.getDisplayName();
                            String stripColorName = stripColor(displayName);
                            if (stripColorName.contains(split[0])) pro = Double.parseDouble(split[1]);
                        }
                    }
                for (ItemStack _item : inventory) {
                    if (_item == null || Material.AIR.equals(_item.getType())) continue;
                    ItemMeta _meta = _item.getItemMeta();
                    if (_meta == null) continue;
                    if (!_meta.hasLore()) continue;
                    List<String> _Lore = _meta.getLore();
                    for (String _loreList : _Lore) {
                        String stripColorLore = stripColor(_loreList);
                        boolean containsAddChance = stripColorLore.contains(checkAddChance);
                        boolean containsReduceChance = stripColorLore.contains(checkReduceChance);
                        if (!containsAddChance || !containsReduceChance) continue;
                        pro += getLoreDouble(_Lore, checkAddChance);
                        pro -= getLoreDouble(_Lore, checkReduceChance);
                        int _amount = _item.getAmount();
                        _item.setAmount(_amount - 1);
                    }
                }
                if ((pro / 100) >= randomChance) actions(player, equipSuccessMsg);
                else {
                    boolean saveItem = false;
                    actions(player, equipFailMsg);
                    for (ItemStack _item : inventory) {
                        if (_item == null || _item.getType().equals(Material.AIR)) continue;
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
                        inventory.addItem(item);
                    }
                    SlotAPI.setSlotItem(player, identifier, new ItemStack(Material.AIR), true);
                    item.setType(Material.AIR);
                }
            }
        }
    }

}
