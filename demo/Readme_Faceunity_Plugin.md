# 相芯 plugin 的使用方法
### 1. 将所需相关依赖文件放到指定目录
|依赖文件|存放路径|
|----|----|
|AgoraWithFaceU.aar|AgoraWithFaceUnity\app\libs|
|agora-rtc-sdk.jar|AgoraWithFaceUnity\app\libs|
|64位libagora-rtc-sdk-jni.so|AgoraWithFaceUnity\app\src\main\jniLibs\arm64-v8a|
|32位libagora-rtc-sdk-jni.so|AgoraWithFaceUnity\app\src\main\jniLibs\armeabi-v7a|

### 2. 使用 RtcEngine create(RtcEngineConfig config) 初始化方法
```
//更换为开发者自己的appID
private static final String appId = "#YOUR APP ID#";
......
RtcEngineConfig config = new RtcEngineConfig();
config.mContext = this;
config.mAppId = appId;
//通过插件提供的接口获取native provider句柄
long provider = ExtensionManager.nativeGetExtensionProvider(this); 
//注册native provider句柄，其中：vender用于区分不同的插件，observer用于监听该插件的消息
config.addExtensionProvider(ExtensionManager.VENDOR_NAME, provider, this); 
......
mRtcEngine = RtcEngine.create(config);
//enable插件
mRtcEngine.enableExtension(ExtensionManager.VENDOR_NAME, true);
```

2.1 addExtensionProvider可多次调用，以注册多个插件（需使用不同的VENDOR_NAME）

2.2 注册插件的消息回调需要实现 io.agora.rtc2.IMediaExtensionObserver 的 onEvent 接口
```
@Override
public void onEvent(String vendor, String key, String value) {
//vendor即为上述注册插件时的VENDOR_NAME，key/value是插件消息的键值对
......
}
```

### 3. 设置相芯插件参数

#3.1初始化相芯
```
// Android
{"path":"根目录路径"}
因demo特殊情况,需将assets中
方法 ExtensionManager.nativeSetParameters(jsonString)

#3.2重要参数
#3.2.1为保证效果正确建议传屏幕分辨率, json格式如下
{
    "screen":{ //屏幕长宽
         "width":640,
         "height":320
     }
}
#3.2.2为保证能识别人脸方向正确需传 is_front_camera, json如下
{
    "setParam":{ //输入bundle参数
        "param":"is_front_camera",
        "value":1
    }
}
value 前置摄像头为1 后置为0
#3.2.3为保证能识别人脸需传 rotationMode, json如下
{
    "setParam":{ //输入bundle参数
        "param":"rotationMode",
        "value":1
    }
}
value 参考 MainActivity onSensorChanged方法
```

#3.3设置美颜,美型,美体,贴纸,滤镜,人像分割参数,参数用 json 的方式传输

```
// Android
ExtensionManager.nativeSetParameters(jsonString)
```

参数解释参考json.md

```

### 4. 识别结果将以 json 的方式在onEvent方法中返回(每帧都会返回)
```
#每次渲染都会回调json
{
    "resolution":{ //分辨率
        "width":1920,
        "height":1080
    }
    "frame":30, //帧率

    "hand_num":1,//屏幕中手的数量 如果开启手势识别会返回
    "gesture_type":0,//如果开启手势识别会返回

    "face_num":1,//屏幕中人脸数量 如果开启表情识别或者表情识别或者人脸检测会返回
    "expression_type":0,//表情 如果开启表情识别会返回
    "emotion_type":0,//情绪 如果开启情绪识别会返回
    "roll":...,//如果开启人脸检测别会返回
    "pitch":...,//如果开启人脸检测别会返回
    "yaw":...//如果开启人脸检测别会返回

    "body_num":1,//屏幕中人体数量 如果开启人体检测会返回
}
```
#也可以手动调用getParameters获取
//Android
ExtensionManager.nativeGetParameters(jsonString)

返回 json 格式参考 json.md
```

### 5. 已知问题
```
# 进入demo初始化耗时会造成可见的黑屏时间

```