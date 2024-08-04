package net.pine.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.function.Predicate;

public class Setting {

    @Expose
    @SerializedName("name")
    public String name;
    private Predicate<Boolean> predicate;

    public Setting(String name)
    {
        this.name = name;
    }

    public Setting(String name, Predicate<Boolean> predicate)
    {
        this(name);

        this.predicate = predicate;
    }

    public boolean isHidden()
    {
        return this.predicate != null && this.predicate.test(true);
    }
}
