package dream.soulflame.flamecore.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.ModuleFileUtil;

import java.util.List;

public class SICLoader {

    public static boolean sicEnable;
    public static String splitChar;
    public static boolean checkNameEnable;
    public static boolean checkLoreEnable;
    public static String lore;
    public static boolean perResolveEnable;
    public static List<String> perResolveList;
    public static List<String> inCooldown;

    private static ModuleFileUtil sicFileUtil;

    public static ModuleFileUtil getSicFileUtil() {
        return sicFileUtil;
    }

    public static void load() {
        sicFileUtil = new ModuleFileUtil(FlameCore.getPlugin(), "RandomCommandGroup-Settings");
    }

    public static void loadData() {
        sicEnable = sicFileUtil.getBoolean("Module.Enable", false);
        splitChar = sicFileUtil.getString("SplitChar", "");
        checkNameEnable = sicFileUtil.getBoolean("Setting.Name.Enable", false);
        checkLoreEnable = sicFileUtil.getBoolean("Setting.Lore.Enable", false);
        lore = sicFileUtil.getString("Setting.Lore.Check", "");
        perResolveEnable = sicFileUtil.getBoolean("Resolve.Permission.Enable", false);
        perResolveList = sicFileUtil.getStringList("Resolve.Permission.List");
        inCooldown = sicFileUtil.getStringList("Message.InCooldown");
    }

    public static void reload() {
        load();
        loadData();
    }

}
