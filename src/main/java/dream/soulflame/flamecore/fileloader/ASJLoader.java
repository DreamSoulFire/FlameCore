package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

/**
 * AntiSpeedJoin模块文件加载
 */
public class ASJLoader {

    public static boolean asjEnable;

    private static ModuleFileUtil asjFileUtil;

    public static ModuleFileUtil getAsjFileUtil() {
        return asjFileUtil;
    }

    public static void load() {
        asjFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "AntiSpeedJoin-Settings");
    }

    public static void loadData() {
        asjEnable = asjFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
