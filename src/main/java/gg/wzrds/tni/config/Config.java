
package gg.wzrds.tni.config;

import gg.wzrds.tni.ToolsAndItems;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("tni.yaml");
    private static Config instance = new Config();

    public String resource_pack_namespace = "tni";
    public Map<String, PermissionTeam> permissions = new HashMap<>();

    public static Config get() {
        return instance;
    }

    public static void load() {
        if (!Files.exists(PATH)) {
            save();
        }
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(Config.class, loaderOptions);
        Yaml yaml = new Yaml( constructor );
        try (InputStream input = Files.newInputStream(PATH)) {
            Config loaded = yaml.loadAs(input, Config.class);
            if (loaded != null) {
                instance = loaded;
            }
        } catch (IOException e) {
            ToolsAndItems.LOGGER.error(e.getMessage());
        }
    }

    public static void save() {
        if (!Files.exists(PATH)) {
            try {
                Files.createFile(PATH);
            } catch (IOException e) {
                ToolsAndItems.LOGGER.error(e.getMessage());
                return;
            }
        }
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer(dumperOptions);
        representer.addClassTag(Config.class, Tag.MAP);
        Yaml yaml = new Yaml(representer, dumperOptions);
        try (BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            yaml.dump(instance, writer);
        } catch (IOException e) {
            ToolsAndItems.LOGGER.error(e.getMessage());
        }
    }
}
