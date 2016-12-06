package com.kit.cn.library.notify;

/**
 * Created by zhouwen on 2016/11/15.
 * Description: msg callback  @link Pair
 */
public class MessageData<T1, T2> {
    public T1 o1;
    public T2 o2;

    public MessageData(T1 o1, T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
    }
}
