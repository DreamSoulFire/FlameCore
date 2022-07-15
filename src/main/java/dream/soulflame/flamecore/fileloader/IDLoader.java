package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

public class IDLoader {

    public static boolean idEnable;
    public static String checkLore;

    private static ModuleFileUtil idFileUtil;

    public static ModuleFileUtil getIdFileUtil() {
        return idFileUtil;
    }

    public static void load() {
        idFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "ItemDurability-Settings");
    }

    public static void loadData() {
        idEnable = idFileUtil.getBoolean("Module.Enable", false);
        checkLore = idFileUtil.getString("CheckLore", "");
    }

    public static void reload() {
        load();
        loadData();
    }

}
