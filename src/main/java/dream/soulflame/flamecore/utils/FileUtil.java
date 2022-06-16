package dream.soulflame.flamecore.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileUtil extends YamlConfiguration {

    public FileUtil(Plugin plugin, String filename) {
        try {
            File file = new File(plugin.getDataFolder(), filename + ".yml");
            writeOut(plugin, filename);
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void writeOut(Plugin plugin, String filename) {
        File file = new File(plugin.getDataFolder(),filename + ".yml");
        if (file.exists()) return;
        plugin.saveResource(filename + ".yml", false);
    }

}
