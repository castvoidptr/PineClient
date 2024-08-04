package net.pine.managers.impl;

import net.pine.managers.IManager;
import net.pine.modules.Module;
import org.reflections.Reflections;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager implements IManager {

    public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();

    @Override
    public boolean init() {

        Reflections reflections = new Reflections("net.pine.modules");

        try {
            for (Class<? extends Module> module : reflections.getSubTypesOf(Module.class)) {
                Module mod = module.newInstance();

                modules.add(mod);
            }
        } catch (Exception e)
        {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public Module findModuleByName(String name)
    {
        return modules.stream().filter(c -> c.name.equals(name)).findFirst().get();
    }
}
