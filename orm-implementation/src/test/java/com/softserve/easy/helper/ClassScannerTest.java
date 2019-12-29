package com.softserve.easy.helper;

import com.softserve.easy.annotation.Entity;
import com.softserve.easy.entity.UserTestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ClassScannerTest {


    @Test
    void getAnnotatedClassesByEntityClass() {
        Set<Class<?>> annotatedClasses = ClassScanner.getAnnotatedClasses(Entity.class);
        Assertions.assertEquals(1, annotatedClasses.size());
        Assertions.assertArrayEquals(new Class<?>[]{UserTestEntity.class}, annotatedClasses.toArray());
    }
}