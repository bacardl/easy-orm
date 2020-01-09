package com.softserve.easy.action;

import com.softserve.easy.helper.Executable;
import com.softserve.easy.meta.MetaData;

public abstract class EntityAction implements Executable {

    protected Object instance;
    protected Class<?> clazz;
    protected MetaData metaData;

}
