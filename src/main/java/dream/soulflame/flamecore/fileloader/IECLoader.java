package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

import java.util.List;

public class IECLoader {

    public static boolean iecEnable;
    public static String checkEquipChance;
    public static String checkAddChance;
    public static String checkReduceChance;
    public static String checkSaveItem;

    public static List<String> equipSuccessMsg;
    public static List<String> equipFailMsg;
    public static List<String> equipSaveMsg;

    private static ModuleFileUtil iecFileUtil;

    public static ModuleFileUtil getIecFileUtil() {
        return iecFileUtil;
    }
    public static void load() {
        iecFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "ItemEquipChance-Settings");
    }

    public static void loadData() {
        iecEnable = iecFileUtil.getBoolean("Module.Enable", false);

        checkEquipChance = iecFileUtil.getString("CheckLore.EquipChance", "");
        checkAddChance = iecFileUtil.getString("CheckLore.AddChance", "");
        checkReduceChance = iecFileUtil.getString("CheckLore.ReduceChance", "");
        checkSaveItem = iecFileUtil.getString("CheckLore.SaveItem", "");

        equipSuccessMsg = iecFileUtil.getStringList("Message.EquipSuccess");
        equipFailMsg = iecFileUtil.getStringList("Message.EquipFail");
        equipSaveMsg = iecFileUtil.getStringList("Message.EquipSave");
    }

    public static void reload() {
        load();
        loadData();
    }

}
