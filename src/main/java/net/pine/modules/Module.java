package net.pine.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.pine.managers.impl.ConfigManager;
import net.pine.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Module {

    @Getter
    @Expose @SerializedName("enabled")
    boolean enabled;
    @Expose @SerializedName("name")
    @Getter
    public String name;
    @Expose @SerializedName("keyCode")
    @Getter @Setter
    private int keyCode;

    @Expose
    @SerializedName("settings")
    public ConfigManager.ConfigSetting[] cfgSettings;

    @Getter
    public ModuleType moduleType;
    @Getter
    public ModuleVisibility moduleVisibility;
    @Getter
    public Category category;

    @Getter
    private final List<Setting> settings = new ArrayList<>();




    public Module(String name, Category category)
    {
        this.category = category;
        this.name = name;
        this.keyCode = 0;
    }

    public Module(String name, int keyCode, Category category)
    {
        this.category = category;
        this.name = name;
        this.keyCode = keyCode;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void assign();
    public abstract String getSuffix();

    public void save()
    {

    }

    public void addSetting(Setting setting)
    {
        this.settings.add(setting);
    }

    public void addSettings(Setting... settings)
    {
        this.settings.addAll(Arrays.stream(settings).collect(Collectors.toList()));
    }

    public boolean isPressed()
    {
        return this.keyCode != 0 && Keyboard.isKeyDown(keyCode);
    }

    public void setToggled(boolean state)
    {
        if (this.enabled == state) return;

        this.enabled = state;

        if (state)
        {
            this.onEnable();
        }
        else
        {
            this.onDisable();
        }
    }
}
