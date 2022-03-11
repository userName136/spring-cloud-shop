package com.leyou.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 16:35
 */
public class ThreadUtils {

        private static final ExecutorService ES = Executors.newFixedThreadPool(10);

        public static void execute(Runnable runnable) {
            ES.submit(runnable);
        }

}
