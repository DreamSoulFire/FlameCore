package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

public class RCGLoader {

    public static boolean rcgEnable;

    private static ModuleFileUtil rcgFileUtil;

    public static ModuleFileUtil getRcgFileUtil() {
        return rcgFileUtil;
    }

    public static void load() {
        rcgFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "RandomCommandGroup-Settings");
    }

    public static void loadData() {
        rcgEnable = rcgFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
