package org.edgeration.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public final class SharedPreferenceUtil {
    private static final String FILE_NAME = "edgeration_tester_preferences";
    private static SharedPreferenceUtil mInstance;

    public static boolean put(Context context, String key, Object value) {
        String type = value.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case "Integer":
                editor.putInt(key, (Integer) value);
                break;
            case "Boolean":
                editor.putBoolean(key, (Boolean) value);
                break;
            case "Float":
                editor.putFloat(key, (Float) value);
                break;
            case "Long":
                editor.putLong(key, (Long) value);
                break;
            case "String":
                editor.putString(key, (String) value);
                break;
        }
        editor.apply();
        return false;
    }

    @Nullable
    public static Object get(Context context, String key, Object defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String type = defValue.getClass().getSimpleName();
        switch (type) {
            case "Integer":
                return sharedPreferences.getInt(key, (Integer) defValue);
            case "Boolean":
                return sharedPreferences.getBoolean(key, (Boolean) defValue);
            case "Float":
                return sharedPreferences.getFloat(key, (Float) defValue);
            case "Long":
                return sharedPreferences.getLong(key, (Long) defValue);
            case "String":
                return sharedPreferences.getString(key, (String) defValue);
        }
        return null;
    }
}
