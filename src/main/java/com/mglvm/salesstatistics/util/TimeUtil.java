package com.mglvm.salesstatistics.util;

import java.time.Instant;
/**
 * Created by Vinoth Kumar on 21/12/2017.
 */
public class TimeUtil {
    public static long getCurrentTime() {
        return Instant.now().getEpochSecond() * 1000;
    }
}
