package github.com.mars_jun.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectUtils {
    public static Object notNull(Object obj, Object obj1) {
        return (obj == null) || ("".equals(obj)) ? obj1 : obj;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isInt(Object s) {
        try {
            Integer.valueOf(s.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(Object s) {
        try {
            Float.valueOf(s.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isLong(Object s) {
        try {
            Long.valueOf(s.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isBoolean(Object s) {
        try {
            Boolean.valueOf(s.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void merge(Object merge_object, Object object)
            throws Exception {
        Class classType = object.getClass();
        Field[] fields = classType.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getMethodName = "get" + firstLetter + fieldName.substring(1);
            String setMethodName = "set" + firstLetter + fieldName.substring(1);
            Method getMethod = classType.getMethod(getMethodName, new Class[0]);
            Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
            Object value = getMethod.invoke(object, new Object[0]);
            if ((value == null) || (value.toString().length() <= 0)) continue;
            setMethod.invoke(merge_object, new Object[]{value});
        }
    }

    public static Class<?> getObject(String Path)
            throws ClassNotFoundException {
        return Class.forName(Path);
    }
}
