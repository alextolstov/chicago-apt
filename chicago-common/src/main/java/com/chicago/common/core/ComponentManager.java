package com.chicago.common.core;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.chicago.dto.Config;
import com.google.protobuf.Message;

import java.util.function.Function;

public class ComponentManager
{
    private Config.ComponentsConfig _config;
    private static HashMap<String, Function> _factories = new HashMap<String, Function>();
    private HashMap<String, AbstractComponent> _componentsMap = new HashMap<String, AbstractComponent>();

    public boolean init(Config.ComponentsConfig config) throws Exception
    {
        _config = config;

        for (Config.Component component : _config.getComponentsList())
        {
            String className = component.getClassName();
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod("registerComponentFactories");
            method.invoke(null);
            Function creator = _factories.get(className);
            if(creator != null)
            {
                _componentsMap.put(className, (AbstractComponent)creator.apply(this));
            }
        }
        return true;
    }

    public static <T> void registerComponentFactory(String cName)
    {
        Function<ComponentManager, AbstractComponent> cr = (parm)->
        {
            try
            {
                Class<?> cl = Class.forName(cName);
                Constructor<?> cons = cl.getConstructor(ComponentManager.class);
                return (AbstractComponent)cons.newInstance(parm);
            }
            catch(NoSuchMethodException | ClassNotFoundException |
                    InstantiationException | IllegalAccessException | InvocationTargetException ex){
                ex.printStackTrace();
            }
            return null;
        };
        _factories.put(cName, cr);
    }

    public <T> T getResource(String cName) throws ClassNotFoundException
    {
        T res;
        if ((res = (T)_componentsMap.get(cName)) != null)
        {
            return res;
        }

        Class<?> cl = Class.forName(cName);
        for(AbstractComponent component : _componentsMap.values())
        {
            if (cl.isInstance(component))
            {
                res = (T) component;
                break;
            }
        }
        return res;
    }

    public void initComponents(Message config)
    {
        ConfigAccessor ca = new ConfigAccessor(config);
        for(AbstractComponent component : _componentsMap.values())
        {
            component.init(ca);
        }
    }

    public void shutdownComponents(){}
}
