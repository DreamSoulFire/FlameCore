package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

public class SFLoader {

    public static boolean sfEnable;

    private static ModuleFileUtil sfFileUtil;

    public static void load() {
        sfFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "SlotFunctions-Settings");
    }

    public static void loadData() {
        sfEnable = sfFileUtil.getBoolean("Module.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
