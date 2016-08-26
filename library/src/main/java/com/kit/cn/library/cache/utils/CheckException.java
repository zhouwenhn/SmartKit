package com.kit.cn.library.cache.utils;

import com.kit.cn.library.utils.log.Logger;

/**
 * @author zhouwen
 * @version 0.1
 * @since 2016/4/8
 */
public class CheckException {

    private final static String TAG = CheckException.class.getSimpleName();

    /**
     * check NPE
     * @param params params
     */
    public static void checkNPE(Object params) {
        if (params == null) {
            Logger.e(TAG, "params can not null");
            throw new NullPointerException("params is not be null");
        }
    }
}
