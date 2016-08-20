package com.kit.cn.library.download.task;

/**
 * Created by zhouwen on 16/8/19.
 */
public class PriorityObject<E> {

    public final Priority priority;
    public final E obj;

    public PriorityObject(Priority priority, E obj) {
        this.priority = priority == null ? Priority.DEFAULT : priority;
        this.obj = obj;
    }
}
