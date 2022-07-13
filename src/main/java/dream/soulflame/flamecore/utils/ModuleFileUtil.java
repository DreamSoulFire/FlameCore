package dream.soulflame.flamecore.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ModuleFileUtil extends YamlConfiguration {

    private final File file;

    public ModuleFileUtil(Plugin plugin, String filename) {
        file = new File(plugin.getDataFolder() + "/modules", filename + ".yml");
        writeOut(plugin, filename);
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void writeOut(Plugin plugin, String filename) {
        if (file.exists()) return;
        plugin.saveResource("modules/" + filename + ".yml", false);
    }
}
