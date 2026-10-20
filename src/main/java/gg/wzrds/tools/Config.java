
package gg.wzrds.tools;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("tools.yaml");

    public static Settings settings = new Settings();
    public static class Settings {
        public String resource_pack_namespace = "tools";

        public Permissions permissions = new Permissions();
        public static class Permissions {
            public Map<String, User> users = new HashMap<>();
            public static class User {
                public List<String> allow = new ArrayList<>();
                public List<String> deny = new ArrayList<>();
            }
            public Map<String, Group> groups = new HashMap<>();
            public static class Group {
                public List<String> uuids = new ArrayList<>();
                public List<String> allow = new ArrayList<>();
                public List<String> deny = new ArrayList<>();
            }
        }

    }


    public static void load() {
        if (!Files.exists(PATH)) {
            save();
        }
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(Settings.class, loaderOptions);
        Yaml yaml = new Yaml( constructor );
        try (InputStream input = Files.newInputStream(PATH)) {
            Settings loaded = yaml.loadAs(input, Settings.class);
            if (loaded != null) {
                settings = loaded;
            }
        } catch (IOException e) {
            Tools.LOGGER.error(e.getMessage());
        }
    }

    public static void save() {
        if (!Files.exists(PATH)) {
            try {
                Files.createFile(PATH);
            } catch (IOException e) {
                Tools.LOGGER.error(e.getMessage());
                return;
            }
        }
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer(dumperOptions);
        representer.addClassTag(Settings.class, Tag.MAP);
        Yaml yaml = new Yaml(representer, dumperOptions);
        try (BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            yaml.dump(settings, writer);
        } catch (IOException e) {
            Tools.LOGGER.error(e.getMessage());
        }
    }
}
