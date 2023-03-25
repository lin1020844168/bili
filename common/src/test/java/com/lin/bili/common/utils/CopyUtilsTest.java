package com.lin.bili.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CopyUtilsTest {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        A a = new A("1", "2");
        B b = new B();
        Map<String, String> map = new HashMap<>();
        map.put("a", "aa");
        map.put("b", "bb");
        CopyUtils.copyObject(a, b, map);
        System.out.println(b);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class A {
    private String a;
    private String b;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class B {
    private String aa;
    private String bb;
}