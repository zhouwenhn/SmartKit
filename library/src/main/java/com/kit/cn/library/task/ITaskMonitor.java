package com.kit.cn.library.task;

/**
 * <p>Description: 监控interface</p>
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * @author zhouwen
 * @version 1.0
 * @since 2016/12/21
 */
public interface ITaskMonitor {
    /**
     * 执行前
     * @param t
     * @param r
     */
    void beforeExecute(Thread t, Runnable r);

    /**
     * 执行后
     * @param r
     * @param t
     */
    void afterExecute(Runnable r, Throwable t);

    /**
     * 执行结束
     * @param largestPoolSize
     * @param completedTaskCount
     */
    void terminated(int largestPoolSize, long completedTaskCount);

    /**
     * 是否开启线程监控
     * @return
     */
    boolean isOpenMonitor();

}
