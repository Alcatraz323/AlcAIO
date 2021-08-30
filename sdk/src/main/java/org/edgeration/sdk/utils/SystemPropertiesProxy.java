package org.edgeration.sdk.utils;

import android.content.Context;

import org.edgeration.sdk.misc.Constants;

import java.lang.reflect.Method;

public class SystemPropertiesProxy {
    public static final String TAG = "SystemPropertiesProxy";

    /**
     * 根据给定的Key返回String类型的值
     *
     * @param context 上下文
     * @param key     获取指定信息所需的key
     * @return 返回一个String类型的值，如果不存在该key则返回空字符串
     */
    public static String getString(Context context, String key) {
        String result = "";
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method getString = SystemProperties.getMethod("get", paramTypes);
            //参数
            Object[] params = new Object[1];
            params[0] = new String(key);

            result = (String) getString.invoke(SystemProperties, params);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    /**
     * 根据给定的Key返回String类型的值
     *
     * @param context 上下文
     * @param key     获取指定信息所需的key
     * @param def     key不存在时的默认值
     * @return 返回一个String类型的值，如果key不存在, 并且如果def不为null则返回def,否则返回空字符串
     */
    public static String getString(Context context, String key, String def) {
        String result = def;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = String.class;
            Method getString = SystemProperties.getMethod("get", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new String(def);

            result = (String) getString.invoke(SystemProperties, params);
        } catch (Exception e) {
            result = def;
        }
        return result;
    }

    public static String getStringWithDefUnknown(Context context, String key) {
        return getString(context, key, Constants.Properties.EDGERATION_PROPERTIES_DEF_VAL_UNKNOWN);
    }

    /**
     * 根据给定的key返回int类型的值
     *
     * @param context 上下文
     * @param key     要查询的key
     * @param def     默认返回值
     * @return 返回一个int类型的值，如果没有发现则返回默认值 def
     */
    public static Integer getInt(Context context, String key, int def) {
        Integer result = def;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Integer(def);
            result = (Integer) getInt.invoke(SystemProperties, params);
        } catch (Exception e) {
            result = def;
        }
        return result;
    }

    /**
     * 根据给定的key返回long类型的值
     *
     * @param context 上下文
     * @param key     要查询的key
     * @param def     默认返回值
     * @return 返回一个long类型的值，如果没有发现则返回默认值def
     */
    public static Long getLong(Context context, String key, long def) {
        Long result = def;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = long.class;
            Method getLong = SystemProperties.getMethod("getLong", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Long(def);
            result = (Long) getLong.invoke(SystemProperties, params);
        } catch (Exception e) {
            result = def;
        }
        return result;
    }

    /**
     * 根据给定的key返回boolean类型的值
     * 如果值为'n','no','0','false' or 'off'返回false
     * 如果值为'y','yes','1','true' or 'on'返回true
     * 如果key不存在, 或者是其它的值, 则返回默认值
     *
     * @param context 上下文
     * @param key     要查询的key
     * @param def     默认返回值
     * @return 返回一个boolean类型的值，如果没有发现则返回默认值def
     */
    public static Boolean getBoolean(Context context, String key, boolean def) {
        Boolean result = def;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = boolean.class;
            Method getBoolean = SystemProperties.getMethod("getBoolean", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Boolean(def);
            result = (Boolean) getBoolean.invoke(SystemProperties, params);
        } catch (Exception e) {
            result = def;
        }
        return result;
    }

    /**
     * 根据给定的key和值设置属性, 该方法需要特定的权限才能操作.
     *
     * @param context 上下文
     * @param key     设置属性的key
     * @param val     设置属性的value
     */
    public static void set(Context context, String key, String val) {
        try {
            @SuppressWarnings("rawtypes")
            ClassLoader classLoader = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = String.class;
            Method set = SystemProperties.getMethod("set", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new String(val);
            set.invoke(SystemProperties, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
