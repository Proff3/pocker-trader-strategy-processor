package org.surfer.strategyprocessor.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StrategyUtils {

    public static boolean isFirstBiggerWithCoefficient(BigDecimal v1, BigDecimal v2, double coefficient) {
        return isFirstBigger(v1, v2) && v1.divide(v2, 5, RoundingMode.HALF_UP).doubleValue() > coefficient;
    }

    public static boolean isFirstBigger(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) > 0;
    }
}
