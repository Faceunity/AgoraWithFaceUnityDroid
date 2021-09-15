package com.faceunity.agorawithfaceunity;

import java.util.ArrayList;

/**
 * @author Qinyu on 2021-09-08
 * @description
 */
public class Filter {
    private static final String[] FILTER_NAMES = {"原图"
            , "自然1", "自然2", "自然3", "自然4", "自然5", "自然6", "自然7", "自然8"
            , "质感灰1", "质感灰2", "质感灰3", "质感灰4", "质感灰5", "质感灰6", "质感灰7", "质感灰8"
            , "蜜桃1", "蜜桃2", "蜜桃3", "蜜桃4", "蜜桃5", "蜜桃6", "蜜桃7", "蜜桃8"
            , "白亮1", "白亮2", "白亮3", "白亮4", "白亮5", "白亮6", "白亮7"
            , "粉嫩1", "粉嫩2", "粉嫩3", "粉嫩4", "粉嫩5", "粉嫩6", "粉嫩7", "粉嫩8"
            , "冷色调1", "冷色调2", "冷色调3", "冷色调4", "冷色调5", "冷色调6", "冷色调7", "冷色调8", "冷色调9", "冷色调10", "冷色调11"
            , "暖色调1", "暖色调2", "暖色调3"
            , "个性1", "个性2", "个性3", "个性4", "个性5", "个性6", "个性7", "个性8", "个性9", "个性10", "个性11"//todo
            , "小清新1", "小清新2", "小清新3", "小清新4", "小清新5", "小清新6"
            , "黑白1", "黑白2", "黑白3", "黑白4", "黑白5"};
    private static final int[] FILTER_RES = {R.drawable.demo_icon_cancel
            , R.drawable.icon_beauty_filter_natural_1, R.drawable.icon_beauty_filter_natural_2, R.drawable.icon_beauty_filter_natural_3, R.drawable.icon_beauty_filter_natural_4, R.drawable.icon_beauty_filter_natural_5, R.drawable.icon_beauty_filter_natural_6, R.drawable.icon_beauty_filter_natural_7, R.drawable.icon_beauty_filter_natural_8
            , R.drawable.icon_beauty_filter_texture_gray_1, R.drawable.icon_beauty_filter_texture_gray_2, R.drawable.icon_beauty_filter_texture_gray_3, R.drawable.icon_beauty_filter_texture_gray_4, R.drawable.icon_beauty_filter_texture_gray_5, R.drawable.icon_beauty_filter_texture_gray_6, R.drawable.icon_beauty_filter_texture_gray_7, R.drawable.icon_beauty_filter_texture_gray_8
            , R.drawable.icon_beauty_filter_peach_1, R.drawable.icon_beauty_filter_peach_2, R.drawable.icon_beauty_filter_peach_3, R.drawable.icon_beauty_filter_peach_4, R.drawable.icon_beauty_filter_peach_5, R.drawable.icon_beauty_filter_peach_6, R.drawable.icon_beauty_filter_peach_7, R.drawable.icon_beauty_filter_peach_8
            , R.drawable.icon_beauty_filter_bailiang_1, R.drawable.icon_beauty_filter_bailiang_2, R.drawable.icon_beauty_filter_bailiang_3, R.drawable.icon_beauty_filter_bailiang_4, R.drawable.icon_beauty_filter_bailiang_5, R.drawable.icon_beauty_filter_bailiang_6, R.drawable.icon_beauty_filter_bailiang_7
            , R.drawable.icon_beauty_filter_fennen_1, R.drawable.icon_beauty_filter_fennen_2, R.drawable.icon_beauty_filter_fennen_3, 666666, R.drawable.icon_beauty_filter_fennen_5, R.drawable.icon_beauty_filter_fennen_6, R.drawable.icon_beauty_filter_fennen_7, R.drawable.icon_beauty_filter_fennen_8
            , R.drawable.icon_beauty_filter_lengsediao_1, R.drawable.icon_beauty_filter_lengsediao_2, R.drawable.icon_beauty_filter_lengsediao_3, R.drawable.icon_beauty_filter_lengsediao_4, 666666, 666666, R.drawable.icon_beauty_filter_lengsediao_7, R.drawable.icon_beauty_filter_lengsediao_8, 666666, 666666, R.drawable.icon_beauty_filter_lengsediao_11
            , R.drawable.icon_beauty_filter_nuansediao_1, R.drawable.icon_beauty_filter_nuansediao_2, 666666
            , R.drawable.icon_beauty_filter_gexing_1, R.drawable.icon_beauty_filter_gexing_2, R.drawable.icon_beauty_filter_gexing_3, R.drawable.icon_beauty_filter_gexing_4, R.drawable.icon_beauty_filter_gexing_5, 666666, R.drawable.icon_beauty_filter_gexing_7, 666666, 666666, R.drawable.icon_beauty_filter_gexing_10, R.drawable.icon_beauty_filter_gexing_11
            , R.drawable.icon_beauty_filter_xiaoqingxin_1, 666666, R.drawable.icon_beauty_filter_xiaoqingxin_3, R.drawable.icon_beauty_filter_xiaoqingxin_4, 666666, R.drawable.icon_beauty_filter_xiaoqingxin_6
            , R.drawable.icon_beauty_filter_heibai_1, R.drawable.icon_beauty_filter_heibai_2, R.drawable.icon_beauty_filter_heibai_3, R.drawable.icon_beauty_filter_heibai_4, 666666};
    private static final int[] FILTER_KEYS = {0
            , 51, 52, 53, 54, 55, 56, 57, 58 /*自然*/
            , 60, 61, 62, 63, 64, 65, 66, 67 /*质感灰*/
            , 68, 69, 70, 71, 72, 73, 74, 75 /*蜜桃*/
            , 1, 2, 3, 4, 5, 6, 7 /*白亮*/
            , 8, 9, 10, 11, 12, 13, 14, 15 /*粉嫩*/
            , 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41 /*冷色调*/
            , 42, 43, 44 /*暖色调*/
            , 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 /*个性*/, 59 /*个性11*/
            , 45, 46, 47, 48, 49, 50 /*小清新*/
            , 26, 27, 28, 29, 30 /*黑白*/
            };

    public static ArrayList<Effect> getFilers() {
        ArrayList<Effect> effects = new ArrayList<>(FILTER_NAMES.length);
        for (int i = 0; i < FILTER_NAMES.length; i++) {
            /** 有些在nama线上版本是没有的, 通过magic number 去除 */
            if (FILTER_RES[i] != 666666) {
                effects.add(new Effect(FILTER_NAMES[i], FILTER_RES[i], "filter", FILTER_KEYS[i]));
            }
        }
        return effects;
    }
}
