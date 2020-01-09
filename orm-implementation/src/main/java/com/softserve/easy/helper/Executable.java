package com.softserve.easy.helper;

import com.softserve.easy.exception.OrmException;

public interface Executable {
    void execute() throws OrmException;
}
