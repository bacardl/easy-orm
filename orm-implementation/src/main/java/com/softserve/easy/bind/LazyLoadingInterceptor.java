package com.softserve.easy.bind;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class LazyLoadingInterceptor {
    private boolean isCalled;
    public void intercept(@Origin Method m) throws Exception {
        if (!isCalled) {
            isCalled = true;
            System.out.println("Init/");
            return;
        }
        System.out.println("Initialized/");
    }
}
