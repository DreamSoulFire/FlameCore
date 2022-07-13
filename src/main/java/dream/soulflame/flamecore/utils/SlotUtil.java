package dream.soulflame.flamecore.utils;

import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;

public class SlotUtil {
    public static void getSlot(Player player, Player target, String slot) {
        final ItemStack[] item = {null};
        SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                    actions(player, getPlugin().getConfig().getStringList("ItemAir"));
                    return;
                }
                item[0] = itemStack;
                player.getInventory().addItem(itemStack);
            }
            @Override
            public void onFail() {
                actions(player, getPlugin().getConfig().getStringList("Error"));
            }
        });
        SlotAPI.setSlotItem(target, slot, new ItemStack(Material.AIR), true);
        ItemMeta meta = item[0].getItemMeta();
        String name = meta.getDisplayName();
        int amount = item[0].getAmount();
        for (String getSlotItem : getPlugin().getConfig().getStringList("GetSlotItem")) {
            actions(player, getSlotItem
                    .replace("<item>", name)
                    .replace("<amount>", String.valueOf(amount))
                    .replace("<target>", target.getName()));
        }
    }

    public static void setSlot(Player player,Player target, String slot) {
        ItemStack itemInMainHand = target.getInventory().getItemInMainHand();
        if (itemInMainHand == null || Material.AIR.equals(itemInMainHand.getType())) {
            actions(player, getPlugin().getConfig().getStringList("NoItemInHand"));
            return;
        }
        int amount = itemInMainHand.getAmount();
        ItemStack item = new ItemStack(itemInMainHand);
        item.setAmount(1);
        SlotAPI.setSlotItem(target, slot, item, true);
        itemInMainHand.setAmount(amount - 1);
        for (String setSlotItem : getPlugin().getConfig().getStringList("SetSlotItem")) {
            actions(player, setSlotItem
                    .replace("<slot>", slot)
                    .replace("<target>", target.getName()));
        }
    }

    public static void changeSlot(Player player, Player target, String preSlot, String newSlot) {
        SlotAPI.getSlotItem(target, preSlot, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                    actions(player, getPlugin().getConfig().getStringList("ItemAir"));
                    return;
                }
                SlotAPI.getSlotItem(target, newSlot, new IDataBase.Callback<ItemStack>() {
                    @Override
                    public void onResult(ItemStack itemStack) {
                        if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                            actions(player, getPlugin().getConfig().getStringList("ItemAir"));
                            return;
                        }
                        SlotAPI.setSlotItem(target, preSlot, itemStack, true);
                    }

                    @Override
                    public void onFail() {
                        actions(player, getPlugin().getConfig().getStringList("Error"));
                    }
                });
                SlotAPI.setSlotItem(target, newSlot, itemStack, true);
            }

            @Override
            public void onFail() {
                actions(player, getPlugin().getConfig().getStringList("Error"));
            }
        });
        for (String changeSlotItem : getPlugin().getConfig().getStringList("ChangeSlotItem")) {
            actions(player, changeSlotItem
                    .replace("<preSlot>", preSlot)
                    .replace("<newSlot>", newSlot)
                    .replace("<target>", target.getName()));
        }
    }
}
