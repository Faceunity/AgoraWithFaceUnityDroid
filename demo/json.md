//setParameters 输入json string
{
    //开始调用nativeGetExtensionProvider获取native provider句柄后  需调用nativeSetParameters或者mRtcEngine.setExtensionProperty传入屏幕长宽和根目录路径参数
    "screen":{ //屏幕长宽  
        "width":640,
        "height":320
    },
    "path":"...",// 根目录路径
    "setParam":{ //输入bundle参数
        "param":"is_front_camera",
        "value":1
    }
    "beautyface":{  //美肤
       "skin":70,//磨皮
       "white":30,//美白
       "red":30,//红润
       "lighteye":0,//亮眼
       "sharpen":70,//锐化
       "teeth":0,//美牙
       "blackeye":0,//去黑眼圈
       "ade":0 //去法令纹
    },
    "beautyshape":{  //美型
        "thinface":40,//瘦脸
        "bigeye":40,//大眼
        "circleeye":0,//圆眼
        "chin":30,//下巴
        "forehead":30,//额头
        "thinnose":50,//瘦鼻
        "mouth":40,//嘴型
        "vface":50,//V脸
        "naface":0,//窄脸
        "smallface":0,//小脸
        "thincheekbone":0,//瘦颧骨
        "thinmandible":0,//瘦下颚骨
        "openeye":0,//开眼角
        "eyedis":50,//眼距
        "eyerid":50,//眼睛角度
        "longnose":50,//长鼻
        "shrinking":50,//缩人中
        "smile":0//微笑嘴角
    },
    "beautybody":{   //美体
        "thinbody":0,//瘦身
        "longleg":0,//长腿
        "thinwaist":0,//细腰
        "beashou":0,//美肩
        "beaass":0,//美臀
        "smallhead":0,//小头
        "thinleg":0,//瘦腿
    },
    "filter":{    //滤镜
        "type":0~5,//0:原图 1:白亮 2:粉嫩 3:小清新 4:冷色调 5:暖色调
        "level":0~100//强度
    },
    "stickers":-1~4,//贴纸 0~4 5张不同的贴纸 -1取消
    "makeup":-1~4,//美妆 0:减龄 1:暖冬 2:红枫 3:Rose 4:少女 -1取消
    "ExpressionRecognition":-1~0,//表情识别 0:开启 -1:关闭
    "EmotionRecognition":-1~0,//情绪识别 0:开启 -1:关闭 目前使用7.3.3版本不支持情绪识别 无需调用 默认关闭的
    "GestureRecognition":-1~0,//手势识别 0:开启 -1:关闭
    "PortraitSegmentation":-1~7,//人像分割 0:男友1 1:男友2 2:男友3 3:古风 4:音乐 5:西瓜 6:海边 7:现代 -1:取消
    "FaceDetection":-1~0,//人脸检测 0:开启 -1:关闭
    "BodyDetection":-1~1//人体检测 0:半身关键点 1:全身关键点 -1:关闭
}

//每次渲染都会回调json
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

//getParameters 输入FaceInformation  获取json string
{
    "expression_type":0,//表情
    "emotion_type":0,//情绪
    "rect":[x0,y0,x1,y1],//人脸方框  (x0,y0)为左上角 (x1,y1)为右下角
    "roll":...,
    "pitch":...,
    "yaw":...

    //expression_type.FUAIEXPRESSION_UNKNOWN = 0;
    //expression_type.FUAIEXPRESSION_BROW_UP = 1 << 1; //抬眉毛
    //expression_type.FUAIEXPRESSION_BROW_FROWN = 1 << 2;//皱眉
    //expression_type.FUAIEXPRESSION_LEFT_EYE_CLOSE = 1 << 3;//闭左眼
    //expression_type.FUAIEXPRESSION_RIGHT_EYE_CLOSE = 1 << 4;//闭右眼
    //expression_type.FUAIEXPRESSION_EYE_WIDE = 1 << 5;//睁大眼睛
    //expression_type.FUAIEXPRESSION_MOUTH_SMILE_LEFT = 1 << 6;//抬左边嘴角
    //expression_type.FUAIEXPRESSION_MOUTH_SMILE_RIGHT = 1 << 7;//抬右边嘴角
    //expression_type.FUAIEXPRESSION_MOUTH_FUNNEL = 1 << 8;//嘴型O
    //expression_type.FUAIEXPRESSION_MOUTH_OPEN = 1 << 9;//嘴型'啊'
    //expression_type.FUAIEXPRESSION_MOUTH_PUCKER = 1 << 10;//嘟嘴
    //expression_type.FUAIEXPRESSION_MOUTH_ROLL = 1 << 11;//抿嘴
    //expression_type.FUAIEXPRESSION_MOUTH_PUFF = 1 << 12;//鼓脸
    //expression_type.FUAIEXPRESSION_MOUTH_SMILE = 1 << 13;//微笑
    //expression_type.FUAIEXPRESSION_MOUTH_FROWN = 1 << 14;//撇嘴
    //expression_type.FUAIEXPRESSION_HEAD_LEFT = 1 << 15;//左转头
    //expression_type.FUAIEXPRESSION_HEAD_RIGHT = 1 << 16;//右转头
    //expression_type.FUAIEXPRESSION_HEAD_NOD = 1 << 17;//点头

    //emotion_type.FUAIEMOTION_UNKNOWN = 0,
    //emotion_type.FUAIEMOTION_HAPPY = 1 << 1,
    //emotion_type.FUAIEMOTION_SAD = 1 << 2,
    //emotion_type.FUAIEMOTION_ANGRY = 1 << 3,
    //emotion_type.FUAIEMOTION_SURPRISE = 1 << 4,
    //emotion_type.FUAIEMOTION_FEAR = 1 << 5,
    //emotion_type.FUAIEMOTION_DISGUST = 1 << 6,
    //emotion_type.FUAIEMOTION_NEUTRAL = 1 << 7,
    //emotion_type.FUAIEMOTION_CONFUSE = 1 << 8,
}

//getParameters 输入GestureInformation  获取json string
{
    "gesture_type":0,
    "rect":[x0,y0,x1,y1]//手势方框  (x0,y0)为左上角 (x1,y1)为右下角

    //gesture_type.FUAIGESTURE_NO_HAND = -1,
    //gesture_type.FUAIGESTURE_UNKNOWN = 0,
    //gesture_type.FUAIGESTURE_THUMB = 1,
    //gesture_type.FUAIGESTURE_KORHEART = 2,
    //gesture_type.FUAIGESTURE_SIX = 3,
    //gesture_type.FUAIGESTURE_FIST = 4,
    //gesture_type.FUAIGESTURE_PALM = 5,
    //gesture_type.FUAIGESTURE_ONE = 6,
    //gesture_type.FUAIGESTURE_TWO = 7,
    //gesture_type.FUAIGESTURE_OK = 8,
    //gesture_type.FUAIGESTURE_ROCK = 9,
    //gesture_type.FUAIGESTURE_CROSS = 10,
    //gesture_type.FUAIGESTURE_HOLD = 11,
    //gesture_type.FUAIGESTURE_GREET = 12,
    //gesture_type.FUAIGESTURE_PHOTO = 13,
    //gesture_type.FUAIGESTURE_HEART = 14,
    //gesture_type.FUAIGESTURE_MERGE = 15,
    //gesture_type.FUAIGESTURE_EIGHT = 16,
    //gesture_type.FUAIGESTURE_HALFFIST = 17,
    //gesture_type.FUAIGESTURE_GUN = 18,
}