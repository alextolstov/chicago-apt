package com.chicago.common.core;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

        for (Config.Component c : _config.getComponentsList())
        {
            String cn = c.getClassName();
            Function creator = _factories.get(cn);
            if(creator != null)
            {
                _componentsMap.put(cn, (AbstractComponent)creator.apply(this));
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
        for(AbstractComponent v : _componentsMap.values())
        {
            if (cl.isInstance(v))
            {
                res = (T) v;
                break;
            }
        }
        return res;
    }

    public void initComponents(Message config)
    {
        ConfigAccessor ca = new ConfigAccessor(config);
        for(AbstractComponent v : _componentsMap.values())
        {
            v.init(ca);
        }
    }

    public void shutdownComponents(){}
}
