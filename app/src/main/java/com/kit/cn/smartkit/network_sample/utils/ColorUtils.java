package com.kit.cn.smartkit.network_sample.utils;/**
 /**
 * Created by zhouwen on 16/8/18.
 */

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {

    public static int randomColor() {
        Random random = new Random();
        int red = random.nextInt(150) + 50;
        int green = random.nextInt(150) + 50;
        int blue = random.nextInt(150) + 50;
        return Color.rgb(red, green, blue);
    }
}
