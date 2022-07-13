package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

public class TILoader {

    public static boolean tiEnable;

    private static ModuleFileUtil tiFileUtil;

    public static ModuleFileUtil getTiFileUtil() {
        return tiFileUtil;
    }

    public static void load() {
        tiFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "TabInfo-Settings");
    }

    public static void loadData() {
        tiEnable = tiFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
