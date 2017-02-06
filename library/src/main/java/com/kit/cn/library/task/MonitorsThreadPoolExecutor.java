package com.kit.cn.library.task;

import com.kit.cn.library.utils.log.L;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>Description: 线程池监控</p>
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * @author zhouwen
 * @version 1.0
 * @since 2016/12/21
 */

public class MonitorsThreadPoolExecutor extends ThreadPoolExecutor {

    private final ThreadLocal<Long> mStartTime = new ThreadLocal<>();

    private final AtomicLong mNumTasks = new AtomicLong();

    private final AtomicLong mTotalTime = new AtomicLong();

    private ConcurrentHashMap<String, ITaskMonitor> mTaskMonitors =
            new ConcurrentHashMap<>();



    public MonitorsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MonitorsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public MonitorsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public MonitorsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * @see ThreadPoolExecutor beforeExecute()
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        for (ITaskMonitor taskMonitor : mTaskMonitors.values()) {
            if (taskMonitor.isOpenMonitor()) {
                taskMonitor.beforeExecute(t, r);
            }
        }
        L.e(String.format("Thread %s: start %s", t, r));
        mStartTime.set(System.nanoTime());
    }

    /**
     * @see ThreadPoolExecutor afterExecute()
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        for (ITaskMonitor taskMonitor : mTaskMonitors.values()) {
            if (taskMonitor.isOpenMonitor()) {
                taskMonitor.afterExecute(r, t);
            }
        }
        long endTime = System.nanoTime();
        long taskTime = endTime - mStartTime.get();
        mNumTasks.incrementAndGet();
        mTotalTime.addAndGet(taskTime);
        L.e(String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
    }

    /**
     * @see ThreadPoolExecutor terminated()
     */
    @Override
    protected void terminated() {
        super.terminated();
        for (ITaskMonitor taskMonitor : mTaskMonitors.values()) {
            if (taskMonitor.isOpenMonitor()) {
                taskMonitor.terminated(getLargestPoolSize(), getCompletedTaskCount());
            }
        }
        L.e(String.format("Terminated: avg time=%dns", mTotalTime.get() / mNumTasks.get()));

    }

    public ITaskMonitor addMonitorTask(String key, ITaskMonitor task, boolean onlyIfAbsent) {
        if (onlyIfAbsent) {
            synchronized (this) {
                return mTaskMonitors.put(key, task);
            }
        } else {
            synchronized (this) {
                return mTaskMonitors.putIfAbsent(key, task);
            }
        }
    }

    public ITaskMonitor addMonitorTask(String key, ITaskMonitor task) {
        return addMonitorTask(key, task, true);
    }

    public ITaskMonitor removeMonitorTask(ITaskMonitor task) {
        synchronized (this) {
            return mTaskMonitors.remove(task);
        }
    }

}
