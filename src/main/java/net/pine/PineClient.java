package net.pine;

import net.minecraft.client.Minecraft;
import net.pine.managers.impl.ConfigManager;
import net.pine.managers.impl.ModuleManager;

public class PineClient {
    public final String VERSION_STREAM = "pre-release";
    public final int VERSION_ID = 1;

    //Managers
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    //variables
    public static Minecraft mc;

    public static void start()
    {
        PineClient.mc = Minecraft.getMinecraft();

        moduleManager = new ModuleManager();
        configManager = new ConfigManager();

        //Init in PROPER REQUIRED ORDER

        if (!moduleManager.init() || !configManager.init())
        {
            //close game, a manager failed to load
        }
    }
}
