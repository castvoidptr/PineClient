package net.pine.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.pine.PineClient;
import net.pine.managers.IManager;
import net.pine.modules.Module;
import net.pine.settings.Setting;
import net.pine.settings.impl.BooleanSetting;
import net.pine.settings.impl.ModeSetting;
import net.pine.settings.impl.NumberSetting;
import net.pine.settings.impl.StringSetting;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager implements IManager {

    public String configPath;

    @Override
    public boolean init() {
        new File(PineClient.mc.mcDataDir + "/config/PineClient").mkdir();
        configPath = PineClient.mc.mcDataDir.getPath() + "/config/PineClient/";



        return true;
    }

    public boolean loadConfig()
    {
        return this.loadConfig(configPath + "Config.json");
    }

    public boolean loadConfig(String path)
    {
        try
        {
            String configString = new String(Files.readAllBytes(new File(path).toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Module[] modules = gson.fromJson(configString, Module[].class);

            for (Module cModule : modules)
            {
                Module module = PineClient.moduleManager.findModuleByName(cModule.name);

                if (module == null)
                {
                    continue;
                }

                for (Setting setting : module.getSettings())
                {
                    for (ConfigSetting configSetting : cModule.cfgSettings)
                    {
                        if (setting == null) continue;

                        if (!setting.name.equals(configSetting.name)) continue;

                        if (setting instanceof BooleanSetting)
                        {
                            ((BooleanSetting) setting).setEnabled((boolean) configSetting.value);
                        }
                        else if (setting instanceof NumberSetting)
                        {
                            ((NumberSetting) setting).set((double) configSetting.value);
                        }
                        else if (setting instanceof StringSetting)
                        {
                            ((StringSetting) setting).setValue((String) configSetting.value);
                        }
                        else if (setting instanceof ModeSetting)
                        {
                            ((ModeSetting) setting).setSelected((String) configSetting.value);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public boolean saveConfig()
    {
        return this.saveConfig(this.configPath + "Config.json");
    }

    public boolean saveConfig(String path)
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        for (Module module : PineClient.moduleManager.modules)
        {
            module.save();

            List<ConfigSetting> cfgSettings = new ArrayList<>();
            for (Setting setting1 : module.getSettings())
            {
                if (setting1 == null) continue;

                ConfigSetting configSetting = new ConfigSetting(setting1.name, null);

                if (setting1 instanceof BooleanSetting setting)
                {
                    configSetting.value = setting.isEnabled();
                }
                else if (setting1 instanceof NumberSetting setting)
                {
                    configSetting.value = setting.getValue();
                }
                else if (setting1 instanceof StringSetting setting)
                {
                    configSetting.value = setting.getValue();
                }
                else if (setting1 instanceof ModeSetting setting)
                {
                    configSetting.value = setting.getSelected();
                }

                cfgSettings.add(configSetting);
            }

            module.cfgSettings = cfgSettings.toArray(new ConfigSetting[0]);
        }
        try
        {
            File file = new File(path);
            Files.write(file.toPath(), gson.toJson(PineClient.moduleManager.modules).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static class ConfigSetting
    {
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("value")
        public Object value;

        public ConfigSetting(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
