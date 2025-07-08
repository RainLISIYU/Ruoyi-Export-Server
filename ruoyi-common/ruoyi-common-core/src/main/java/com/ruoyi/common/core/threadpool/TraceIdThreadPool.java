package com.ruoyi.common.core.threadpool;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.util.annotation.NonNull;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author lsy
 * @description 带链路追踪的线程池
 * @date 2025/7/8
 */
public class TraceIdThreadPool extends ThreadPoolTaskExecutor {

    @Serial
    private static final long serialVersionUID = 1L;
    private boolean useFixedContext = false;
    private Map<String, String> fixedContext;

    public TraceIdThreadPool() {
        super();
    }

    public TraceIdThreadPool(Map<String, String> fixedContext) {
        super();
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
    }

    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        super.execute(wrapExecute(command, getContextForTask()));
    }

    @Override
    @NonNull
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return super.submit(wrapSubmit(task, getContextForTask()));
    }

    private <T> Callable<T> wrapSubmit(Callable<T> task, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return task.call();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    private Runnable wrapExecute(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

}
