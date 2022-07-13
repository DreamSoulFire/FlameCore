package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

/**
 * AntiItemAttack模块文件加载
 */
public class AIALoader {

    public static boolean aiaEnable;

    private static ModuleFileUtil aiaFileUtil;

    public static ModuleFileUtil getAiaFileUtil() {
        return aiaFileUtil;
    }

    public static void load() {
        aiaFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "AntiItemAttack-Settings");
    }

    public static void loadData() {
        aiaEnable = aiaFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
