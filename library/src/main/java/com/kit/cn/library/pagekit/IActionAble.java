package com.kit.cn.library.pagekit;

/**
 * Created by zhouwen on 16/8/3.
 */
public interface IActionAble {
    /**
     * close the page
     * @param fragment page's instance
     */
    void close(FragmentWrapper fragment);

    /**
     * openPage the page
     * @param fragment fragment page's instance
     */
    void open(FragmentWrapper fragment);
}
