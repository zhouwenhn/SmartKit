package com.kit.cn.library.task;

import android.os.Handler;
import android.os.Looper;

import com.kit.cn.library.utils.log.L;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: 统一的线程池</p>
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * @author zhouwen
 * @version 1.0
 * @since 2016/10/31 11:28
 */
public class TaskExecutor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    private static final int KEEP_ALIVE = 1;

    private static final int CAPACITY = 128;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "TaskExecutor #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(CAPACITY);

    private static final MonitorsThreadPoolExecutor sThreadPoolExecutor = new MonitorsThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    private static ScheduledThreadPoolExecutor sScheduledThreadPoolExecutor = null;

    private static Handler mMainHandler;

    /**
     * 线程池任务
     * @param r Runnable
     */
    public static void execute(Runnable r) {
        sThreadPoolExecutor.execute(r);
    }

    /**
     * 线程池任务
     * @param r Runnable
     */
    public static void execute(String threadName, Runnable r) {
        execute(r);
        sThreadPoolExecutor.addMonitorTask(threadName, new ITaskMonitor() {
            @Override
            public void beforeExecute(Thread t, Runnable r) {
                L.e("monitor>>>>beforeExecute>>>>thread_name>"+t.getName());
            }

            @Override
            public void afterExecute(Runnable r, Throwable t) {
                L.e("monitor>>>>afterExecute>>>>>"+r);
            }

            @Override
            public void terminated(int largestPoolSize, long completedTaskCount) {
                L.e("monitor>>>>terminated>>>>largestPoolSize>"+largestPoolSize+
                        ">completedTaskCount>"+completedTaskCount);
            }

            @Override
            public boolean isOpenMonitor() {
                return true;
            }
        });
    }

    /**
     * schedule execute 任务
     * @param r Runnable
     * @param delayMillis delay 时间 单位：毫秒
     * @return
     */
    public static ScheduledFuture<?> scheduleExecuteTask(Runnable r, long delayMillis) {
        checkScheduledThreadPoolExecutor();
        return sScheduledThreadPoolExecutor.schedule(r, delayMillis,TimeUnit.MILLISECONDS);
    }

    /**
     * 按频率执行
     * @param r Runnable
     * @param initialDelay 初始 delay
     * @param delayMillis delay 时间 单位：毫秒
     * @return ScheduledFuture
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initialDelay, long delayMillis){
        checkScheduledThreadPoolExecutor();
        return sScheduledThreadPoolExecutor.scheduleAtFixedRate(r, initialDelay,
                delayMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 在UI线程执行
     * @param r Runnable
     * @return see android.os.Handler#post(java.lang.Runnable)
     */
    public static boolean executeRunOnUIExecutorTask(Runnable r) {
        checkHandler();
        return mMainHandler.post(r);
    }

    /**
     * 在UI线程按设置delay时间执行
     * @param r Runnable
     * @param delayMillis delay 时间  单位：毫秒
     * @return see android.os.Handler#postDelayed(java.lang.Runnable, long)
     */
    public static boolean executeScheduleRunOnUIExecutorTask(Runnable r, long delayMillis) {
        checkHandler();
        return mMainHandler.postDelayed(r, delayMillis);
    }

    /**
     * remove task
     * @param r Runnable
     */
    public static void removeUITask(Runnable r) {
        checkHandler();
        mMainHandler.removeCallbacks(r);
    }

    /**
     * 获取线程池实例
     * @return thread pool
     */
    public static Executor getTaskExecutor(){
        return sThreadPoolExecutor;
    }

    /**
     * shutdown threadPool
     */
    public static void shutdown() {
        if (sScheduledThreadPoolExecutor != null)
            sScheduledThreadPoolExecutor.shutdown();
    }

    /**
     * shutdown threadPool-->now
     * see java.util.concurrent.ScheduledThreadPoolExecutor#shutdownNow()
     */
    public static void shutdownNow() {
        if (sScheduledThreadPoolExecutor != null)
            sScheduledThreadPoolExecutor.shutdownNow();
    }

    private synchronized static void checkHandler() {
        if (mMainHandler == null)
            mMainHandler = new Handler(Looper.getMainLooper());
    }

    private static void checkScheduledThreadPoolExecutor() {
        if (sScheduledThreadPoolExecutor == null)
            sScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}
