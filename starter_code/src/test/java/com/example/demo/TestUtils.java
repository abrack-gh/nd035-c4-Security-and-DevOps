package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if (wasPrivate) {
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
