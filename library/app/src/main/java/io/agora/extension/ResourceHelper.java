package io.agora.extension;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.File;

public class ResourceHelper {

    public static boolean isResourceReady(@NonNull final Context context, int versionCode) {

        SharedPreferences preferences=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean resourceReady = preferences.getBoolean("resource", false);
        int preVersioncode = preferences.getInt("versionCode", 0);

        // 如果上次已经拷贝过 继续检查版本号
        // Continue to check the version number if it was copied last time
        if (resourceReady && versionCode == preVersioncode){
            return true;
        }
        return false;
    }

    public static void setResourceReady(@NonNull final Context context, boolean isReady, int versionCode){
        SharedPreferences preferences=context.getSharedPreferences("user", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("resource", isReady);
        editor.putInt("versionCode", versionCode);
        editor.commit();
    }

}
