package io.agora.extension;

import android.content.Context;

import androidx.annotation.Keep;

import java.io.File;
import java.io.IOException;

@Keep
public class ExtensionManager {
    public static final String VENDOR_NAME = "FaceUnity";
    static {
        System.loadLibrary("AgoraWithFaceUnity");
    }

    public static void copyResource(Context context) {
        String path = "faceunityRes";
        File dstFile = context.getExternalFilesDir("assets");
        FileUtils.clearDir(new File(dstFile, path));

        try {
            FileUtils.copyAssets(context.getAssets(), path, dstFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static native long nativeGetExtensionProvider(Context context, String vendor);
    public static native int nativeSetParameters(String parameters);
    public static native String nativeGetParameters(String parameters);
}
