package net.pine.forge;

import lombok.SneakyThrows;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;

public class ForgeSpoofer {
    public static ModContainer spoofedContainer = Loader.instance().getMinecraftModContainer();
    private static Method method = null;

    @SneakyThrows
    public static void register(Object target)
    {
        //This is essentially a recode of the EventBus register function, but allowing us
        //to register a class containing events without having our own "ModContainer"

        //Get a random container on boot-up
        //for now ill just use the MC one
        //Also, we need a method access we don't have

        if (method == null)
        {
            method = EventBus.class.getMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
        }

        for (Method method : target.getClass().getMethods())
        {
            try
            {
                if (!method.isAnnotationPresent(SubscribeEvent.class)) continue;

                Class<?>[] param = method.getParameterTypes();

                if (param.length != 1)
                {
                    throw new IllegalArgumentException("Too many arguments");
                }

                Class<?> event = param[0];

                if (!Event.class.isAssignableFrom(event))
                {
                    throw new IllegalArgumentException("Incorrect class type");
                }

                method.invoke(MinecraftForge.EVENT_BUS, event, target, method, spoofedContainer);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
