package com.carbarn.common.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @Description 简化使用StopWatch
 * 1. StopWatcher stopWatch = new StopWatcher();
 * 2. process
 * 3. long latency = stopWatch.getTime()
 * @Author lxzou
 * @Date 2025/1/11 13:13
 */
public class StopWatcher {
    private final StopWatch stopWatch;
    private final long startMillis;
    public StopWatcher() {
        startMillis = System.currentTimeMillis();
        this.stopWatch = new StopWatch();
        this.stopWatch.start();
    }

    public long getStartMillis() {
        return this.startMillis;
    }

    public long getMillis() {
        return stopWatch.getTime();
    }

    public long getMicros() {
        return stopWatch.getTime(TimeUnit.MICROSECONDS);
    }

    public long getSeconds() {
        return stopWatch.getTime(TimeUnit.SECONDS);
    }

    public long getSecondsAndStop() {
        return getTimeAndStop(TimeUnit.SECONDS);
    }

    public long getTime(TimeUnit timeUnit) {
        return stopWatch.getTime(timeUnit);
    }

    public long getMillsAndStop() {
        stopWatch.stop();
        return stopWatch.getTime();
    }

    public long getTimeAndStop(TimeUnit timeUnit) {
        stopWatch.stop();
        return stopWatch.getTime(timeUnit);
    }


}
