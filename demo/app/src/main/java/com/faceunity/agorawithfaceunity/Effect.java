package com.faceunity.agorawithfaceunity;

import java.util.ArrayList;

/**
 * @author Qinyu on 2021-04-01
 * @description
 */
public class Effect {
    private String name;
    private int iconId;
    private String jsonKey;
    private int jsonValue;

    public Effect(String name, int iconId, String jsonKey, int jsonValue) {
        this.name = name;
        this.iconId = iconId;
        this.jsonKey = jsonKey;
        this.jsonValue = jsonValue;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public int getJsonValue() {
        return jsonValue;
    }

    public enum EffectEnum {
        /**
         * 滤镜 0:原图 1:白亮 2:粉嫩 3:小清新 4:冷色调 5:暖色调
         */
//        FilterNone("原图", R.drawable.demo_icon_cancel, "filter", 0),
//        Filter1("白亮", R.drawable.bailiang1, "filter", 1),
//        Filter2("粉嫩", R.drawable.fennen1, "filter", 2),
//        Filter3("小清新", R.drawable.xiaoqingxin1, "filter", 3),
//        Filter4("冷色调", R.drawable.lengsediao1, "filter", 4),
//        Filter5("暖色调", R.drawable.nuansediao1, "filter", 5),

        /**
         * 贴纸 0~4 5张不同的贴纸 -1取消
         */
        StickerNone("无", R.drawable.ic_delete_all, "stickers", -1),
        Sticker0("0", R.drawable.nihongdeng, "stickers", 0),
        Sticker1("1", R.drawable.cat_sparks, "stickers", 1),
        Sticker2("2", R.drawable.redribbt, "stickers", 2),
        Sticker3("3", R.drawable.daisypig, "stickers", 3),
        Sticker4("4", R.drawable.sdlu, "stickers", 4),

        /**
         * 人像分割 0:男友1 1:男友3 2:男友2 3:古风 4:音乐 5:西瓜 6:海边 7:现代 -1:取消
         */
        SegmentNone("无", R.drawable.ic_delete_all, "PortraitSegmentation", -1),
        Segment0("男友1", R.drawable.demo_icon_boyfriend_01, "PortraitSegmentation", 0),
        Segment1("男友3", R.drawable.demo_icon_boyfriend_03, "PortraitSegmentation", 1),
        Segment2("男友2", R.drawable.demo_icon_boyfriend_02, "PortraitSegmentation", 2),
        Segment3("古风", R.drawable.gufeng_zh_fu, "PortraitSegmentation", 3),
        Segment4("音乐", R.drawable.hez_ztt_fu, "PortraitSegmentation", 4),
        Segment5("西瓜", R.drawable.ice_lm_fu, "PortraitSegmentation", 5),
        Segment6("海边", R.drawable.sea_lm_fu, "PortraitSegmentation", 6),
        Segment7("现代", R.drawable.xiandai_ztt_fu, "PortraitSegmentation", 7);

        private String name;
        private int iconId;
        private String jsonKey;
        private int jsonValue;

        EffectEnum(String name, int iconId, String jsonKey, int jsonValue) {
            this.name = name;
            this.iconId = iconId;
            this.jsonKey = jsonKey;
            this.jsonValue = jsonValue;
        }

        public Effect effect() {
            return new Effect(name, iconId, jsonKey, jsonValue);
        }

        public static ArrayList<Effect> getEffectsByKey(String key) {
            EffectEnum[] values = EffectEnum.values();
            ArrayList<Effect> effects = new ArrayList<>();
            for (EffectEnum e : values) {
                if (e.jsonKey.equals(key)) {
                    effects.add(e.effect());
                }
            }
            return effects;
        }
    }
}
