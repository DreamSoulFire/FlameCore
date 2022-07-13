package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

/**
 * AntiFireEntity模块文件加载
 */
public class AFELoader {

    public static boolean afeEnable;

    private static ModuleFileUtil afeFileUtil;

    public static ModuleFileUtil getAfeFileUtil() {
        return afeFileUtil;
    }

    public static void load() {
        afeFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "AntiFireEntity-Settings");
    }

    public static void loadData() {
        afeEnable = afeFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
