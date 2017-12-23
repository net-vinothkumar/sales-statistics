package com.mglvm.salesstatistics.util;

import java.time.Instant;

public class TimeUtil {
    public static long getCurrentTime() {
        return Instant.now().getEpochSecond() * 1000;
    }
}
