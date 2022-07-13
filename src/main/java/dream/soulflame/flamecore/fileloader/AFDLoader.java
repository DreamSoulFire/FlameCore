package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

/**
 * AntiFallingDamage模块文件加载
 */
public class AFDLoader {

    public static boolean afdEnable;

    private static ModuleFileUtil afdFileUtil;

    public static void load() {
        afdFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "AntiFallingDamage-Settings");
    }

    public static void loadData() {
        afdEnable = afdFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
