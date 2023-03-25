package com.lin.bili.common.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class CopyUtils {
    public static void copyObject(Object objectSource, Object objectTarget, Map<String, String> fieldMapping) throws NoSuchFieldException, IllegalAccessException {
        Class<?> t1 = objectSource.getClass();
        Class<?> t2 = objectTarget.getClass();
        Set<String> keySet = fieldMapping.keySet();
        for (String k : keySet) {
            Field fieldSource = t1.getDeclaredField(k);
            fieldSource.setAccessible(true);
            Field fieldTarget = t2.getDeclaredField(fieldMapping.get(k));
            fieldTarget.setAccessible(true);
            fieldTarget.set(objectTarget, fieldSource.get(objectSource));
        }
    }
}
