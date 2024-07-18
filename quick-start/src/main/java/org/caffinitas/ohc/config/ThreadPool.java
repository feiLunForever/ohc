package org.caffinitas.ohc.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static ThreadPoolExecutor Cache = new ThreadPoolExecutor(
                3,
                8,
                200,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                new ThreadFactoryBuilder().setNameFormat("cache-pool-").build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
}