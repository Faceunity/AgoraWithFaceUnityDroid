package com.faceunity.agorawithfaceunity;

import androidx.collection.ArrayMap;

import java.util.Map;

/**
 * @author Qinyu on 2021-04-02
 * @description
 */
public final class FuKeyWord {
    public static final Map<Integer, String> GESTURE = new ArrayMap<>(20);
    public static final Map<Integer, String> EXPRESSION = new ArrayMap<>(18);

    static {
        GESTURE.put(-1, "无");
        GESTURE.put(0, "未知");
        GESTURE.put(1, "点赞");
        GESTURE.put(2, "单手比心");
        GESTURE.put(3, "666");
        GESTURE.put(4, "拳头");
        GESTURE.put(5, "推掌");
        GESTURE.put(6, "一");
        GESTURE.put(7, "二");
        GESTURE.put(8, "OK");
        GESTURE.put(9, "摇滚");
        GESTURE.put(10, "");
        GESTURE.put(11, "水平手掌");
        GESTURE.put(12, "拜年");
        GESTURE.put(13, "拍照");
        GESTURE.put(14, "双手爱心");
        GESTURE.put(15, "双手合十");
        GESTURE.put(16, "八");
        GESTURE.put(17, "");
        GESTURE.put(18, "枪");

        EXPRESSION.put(0, "无");
        EXPRESSION.put(1 << 1, "抬眉毛");
        EXPRESSION.put(1 << 2, "皱眉");
        EXPRESSION.put(1 << 3, "闭左眼");
        EXPRESSION.put(1 << 4, "闭右眼");
        EXPRESSION.put(1 << 5, "睁大眼睛");
        EXPRESSION.put(1 << 6, "抬左边嘴角");
        EXPRESSION.put(1 << 7, "抬右边嘴角");
        EXPRESSION.put(1 << 8, "嘴型O");
        EXPRESSION.put(1 << 9, "嘴型'啊'");
        EXPRESSION.put(1 << 10, "嘟嘴");
        EXPRESSION.put(1 << 11, "抿嘴");
        EXPRESSION.put(1 << 12, "鼓脸");
        EXPRESSION.put(1 << 13, "微笑");
        EXPRESSION.put(1 << 14, "撇嘴");
        EXPRESSION.put(1 << 15, "左转头");
        EXPRESSION.put(1 << 16, "右转头");
        EXPRESSION.put(1 << 17, "点头");
    }
}
