package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

import java.util.List;

/**
 * AntiDrop模块文件加载
 */
public class ADLoader {

    public static boolean adEnable;
    public static boolean toggleModeEnable;
    public static boolean skipPerEnable;
    public static List<String> normalMsg;

    private static ModuleFileUtil adFileUtil;

    public static ModuleFileUtil getAdFileUtil() {
        return adFileUtil;
    }

    public static void load() {
        adFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "AntiDrop-Settings");
    }

    public static void loadData() {
        adEnable = adFileUtil.getBoolean("Module.Enable", false);
        toggleModeEnable = adFileUtil.getBoolean("ToggleDropMode.Enable", false);
        skipPerEnable = adFileUtil.getBoolean("Skip.Permission.Enable", false);
        normalMsg = adFileUtil.getStringList("Message.Normal");
    }

    public static void reload() {
        load();
        loadData();
    }

}
