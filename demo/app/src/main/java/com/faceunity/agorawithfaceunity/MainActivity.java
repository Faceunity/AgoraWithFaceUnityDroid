package com.faceunity.agorawithfaceunity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.faceunity.agorawithfaceunity.view.BottomNavigationView;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

import io.agora.extension.ExtensionManager;
import io.agora.extension.ResourceHelper;
import io.agora.extension.UtilsAsyncTask;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IMediaExtensionObserver;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class MainActivity extends AppCompatActivity implements IMediaExtensionObserver, UtilsAsyncTask.OnUtilsAsyncTaskEvents, SensorEventListener {
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
    };
    private static final String appId = "aab8b8f5a8cd4469a63042fcfafe7063";
    private final static String TAG = "Agora_FaceU:";
    private static final int PERMISSION_REQ_ID = 22;
    private final FuJsonHelper mFuJsonHelper = FuJsonHelper.getInstance();

    private FrameLayout localVideoContainer;
    private FrameLayout remoteVideoContainer;
    private SurfaceView mRemoteView;
    private BottomNavigationView mBottomNavigator;
    private TextView tv_face_info, tv_face_info2, tv_body_info;
    private ImageView iv_switch, iv_debug;
    private SwitchButton switch_btn_body;

    private boolean mCameraFacingFont = true;
    private RtcEngine mRtcEngine;
    private SensorManager mSensorManager;
    private int mRotationMode = -1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        checkPermission();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (!ResourceHelper.isResourceReady(this, 1)) {
            new UtilsAsyncTask(this, this).execute();
        } else {
            onPostExecute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ResourceHelper.isResourceReady(this, 1)) {
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mRotationMode = -1;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initAgoraEngine();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initAgoraEngine();
        }
    }

    private void initUI() {
        localVideoContainer = findViewById(R.id.view_container);
        remoteVideoContainer = findViewById(R.id.remote_video_view_container);
        mBottomNavigator = findViewById(R.id.bottom_navigator);
        tv_face_info = findViewById(R.id.tv_face_info);
        tv_face_info2 = findViewById(R.id.tv_face_info2);
        tv_body_info = findViewById(R.id.tv_body_info);
        iv_switch = findViewById(R.id.iv_switch_camera);
        iv_switch.setOnClickListener(v -> {
            mCameraFacingFont = !mCameraFacingFont;
            mRotationMode = -1;
            mRtcEngine.switchCamera();
            mFuJsonHelper.setParams("setParam"
                    , new Object[]{"is_front_camera", mCameraFacingFont ? 1 : 0}
                    , new String[]{"param", "value"});
        });
        iv_debug = findViewById(R.id.iv_debug);
        iv_debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean gone = tv_face_info.getVisibility() == View.GONE;
                tv_face_info.setVisibility(gone ? View.VISIBLE : View.GONE);
            }
        });
        switch_btn_body = findViewById(R.id.switch_btn_body);
        switch_btn_body.setOnCheckedChangeListener(mBottomNavigator);
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    private void initAgoraEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = this;
            config.mAppId = appId;
            long provider = ExtensionManager.nativeGetExtensionProvider(this, ExtensionManager.VENDOR_NAME);
            Log.d(TAG, "Extension provider: " + provider);
            config.addExtension(ExtensionManager.VENDOR_NAME, provider);
            config.mExtensionObserver = this;
            config.mEventHandler = new IRtcEngineEventHandler() {
                @Override
                public void onJoinChannelSuccess(String s, int i, int i1) {
                    Log.d(TAG, "onJoinChannelSuccess");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRtcEngine.startPreview();
                        }
                    });
                }

                @Override
                public void onFirstRemoteVideoDecoded(final int i, int i1, int i2, int i3) {
                    super.onFirstRemoteVideoDecoded(i, i1, i2, i3);
                    Log.d(TAG, "onFirstRemoteVideoDecoded  uid = " + i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupRemoteVideo(i);
                        }
                    });
                }

                @Override
                public void onUserJoined(int i, int i1) {
                    super.onUserJoined(i, i1);
                    Log.d(TAG, "onUserJoined  uid = " + i);
                }

                @Override
                public void onUserOffline(final int i, int i1) {
                    super.onUserOffline(i, i1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRemoteUserLeft();
                        }
                    });
                }
            };
            mRtcEngine = RtcEngine.create(config);
//            mRtcEngine.enableExtension(ExtensionManager.VENDOR_NAME, true);
            setupLocalVideo();

            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            mRtcEngine.enableLocalVideo(true);
            mRtcEngine.enableVideo();
            mRtcEngine.enableAudio();
            Log.d(TAG, "api call join channel");
            mRtcEngine.joinChannel("", "agora_test", "", 0);
            mRtcEngine.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLocalVideo() {
        SurfaceView view = RtcEngine.CreateRendererView(this);
        view.setZOrderMediaOverlay(true);
        localVideoContainer.addView(view);
        mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        mRtcEngine.setLocalRenderMode(Constants.RENDER_MODE_HIDDEN);
    }

    private void setupRemoteVideo(int uid) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        int count = remoteVideoContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = remoteVideoContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }

        Log.d(TAG, " setupRemoteVideo uid = " + uid);
        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        remoteVideoContainer.addView(mRemoteView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRtcEngine.setRemoteRenderMode(uid, Constants.RENDER_MODE_HIDDEN);
        mRemoteView.setTag(uid);
    }

    private void onRemoteUserLeft() {
        removeRemoteVideo();
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            remoteVideoContainer.removeView(mRemoteView);
        }
        mRemoteView = null;
    }

    @Override
    public void onEvent(String vendor, String key, String value) {
        try {
            JSONObject json = new JSONObject(value);
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            if (json.has("frame")) {
                sb.append("resolution:\n").append(json.getJSONObject("resolution").getInt("width"))
                  .append("*").append(json.getJSONObject("resolution").getInt("height")).append("\n")
                  .append("fps: ").append(new BigDecimal(json.getDouble("frame")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()).append("\n");
            }
            if (json.has("expression_type")) {
                sb2.append("表情: ");
                int expression = json.getInt("expression_type");
                for (int i : FuKeyWord.EXPRESSION.keySet()) {
                    if ((i & expression) != 0) {
                        sb2.append(FuKeyWord.EXPRESSION.get(i)).append(" ");
                    }
                }
                if (expression == 0) {
                    sb2.append(FuKeyWord.EXPRESSION.get(0));
                }
                sb2.append("\n");
            }
            if (json.has("gesture_type")) {
                int gesture = json.getInt("gesture_type");
                sb2.append("手势: ").append(FuKeyWord.GESTURE.get(gesture)).append("\n");
            }
            if (json.has("yaw")) {
                sb.append("yaw: ").append(new BigDecimal(json.getDouble("yaw")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).append("°\n")
                  .append("pitch: ").append(new BigDecimal(json.getDouble("pitch")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).append("°\n")
                  .append("roll: ").append(new BigDecimal(json.getDouble("roll")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).append("°\n");
            }
            boolean noBody = json.has("body_num") && json.getInt("body_num") == 0;
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            if (sb2.length() > 0) {
                sb2.deleteCharAt(sb2.length() - 1);
            }
            runOnUiThread(() -> {
                if (noBody) {
                    tv_body_info.setVisibility(View.VISIBLE);
                } else {
                    tv_body_info.setVisibility(View.GONE);
                }
                tv_face_info.setText(sb);
                tv_face_info2.setText(sb2);
                tv_face_info2.setVisibility(sb2.length() > 0 ? View.VISIBLE : View.GONE);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreExecute() {}

    @Override
    public void onPostExecute() {
        ResourceHelper.setResourceReady(this, true, 1);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        File assetsDir = new File(getExternalFilesDir("assets").getAbsolutePath());
        if (!new File(assetsDir.getAbsolutePath() + "/faceunityRes/authpack.json").exists()) {
            throw new AndroidRuntimeException("需要相芯鉴权证书");
        }
        mFuJsonHelper.putAndSet(assetsDir.getAbsolutePath() + "/faceunityRes/", "path");
        mFuJsonHelper.setParams("screen"
                , new Object[]{localVideoContainer.getMeasuredWidth(), localVideoContainer.getMeasuredHeight()}
                , new String[]{"width", "height"});
        mFuJsonHelper.setParams("setParam"
                , new Object[]{"is_front_camera", mCameraFacingFont ? 1 : 0}
                , new String[]{"param", "value"});
        mBottomNavigator.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, "资源准备完毕", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int rotationMode = 1;
            float x = event.values[0];
            float y = event.values[1];
            if (Math.abs(x) > 1 || Math.abs(y) > 1) {
                boolean horizontal = Math.abs(x) > Math.abs(y);
                if (mCameraFacingFont) {
                    if (horizontal) {
                        rotationMode = x > 0 ? 0 : 2;
                    } else {
                        rotationMode = y > 0 ? 1 : 3;
                    }
                } else {
                    if (horizontal) {
                        rotationMode = x > 0 ? 0 : 2;
                    } else {
                        rotationMode = y > 0 ? 3 : 1;
                    }
                }
            }
            if (mRotationMode != rotationMode) {
                Log.d("FaceUnity", "onSensorChanged:" + rotationMode + " x:" + x + " y:" + y);
                mFuJsonHelper.setParams("setParam"
                        , new Object[]{"rotationMode", rotationMode}
                        , new String[]{"param", "value"});
                mRotationMode = rotationMode;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}