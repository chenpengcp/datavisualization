package com.snh48.datavisualization.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScalaUtils {
    public static String scala(Float f) {
        String a = "";
        BigDecimal bg = new BigDecimal(f).setScale(2, RoundingMode.UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            a = String.valueOf((long) num);
        } else {
            a = String.valueOf(num);
        }
        return a;
    }

    public static Float scalaFloat(Float f) {
        String a = "";
        BigDecimal bg = new BigDecimal(f).setScale(2, RoundingMode.UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            a = String.valueOf((long) num);
        } else {
            a = String.valueOf(num);
        }
        return Float.parseFloat(a);
    }
}
