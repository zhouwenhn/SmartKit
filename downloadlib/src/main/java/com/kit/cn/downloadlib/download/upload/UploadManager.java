package com.kit.cn.downloadlib.download.upload;

import android.support.annotation.NonNull;

import com.kit.cn.downloadlib.download.listener.UploadListener;
import com.kit.cn.library.network.OkHttpTask;
import com.kit.cn.library.network.request.BaseBodyRequest;
import com.kit.cn.library.network.request.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhouwen on 16/8/19.
 */
public class UploadManager {
    //定义上传状态常量
    public static final int NONE = 0;         //无状态  --> 等待
    public static final int WAITING = 1;      //等待    --> 下载
    public static final int UPLOADING = 2;    //下载中  --> 完成，错误
    public static final int FINISH = 3;       //完成    --> 重新上传
    public static final int ERROR = 4;        //错误    --> 等待

    private List<UploadInfo> mUploadInfoList;       //维护了所有下载任务的集合
    private UploadUIHandler mUploadUIHandler;       //主线程执行的handler
    private static UploadManager mInstance;         //使用单例模式
    private UploadThreadPool threadPool;            //上传的线程池

    public static UploadManager getInstance() {
        if (null == mInstance) {
            synchronized (UploadManager.class) {
                if (null == mInstance) {
                    mInstance = new UploadManager();
                }
            }
        }
        return mInstance;
    }

    private UploadManager() {
        mUploadInfoList = Collections.synchronizedList(new ArrayList<UploadInfo>());
        mUploadUIHandler = new UploadUIHandler();
        threadPool = new UploadThreadPool();
    }

    /** 添加一个上传任务,默认使用post请求 */
    @Deprecated
    public <T> void addTask(String url, @NonNull File resource, @NonNull String key, UploadListener<T> listener) {
        PostRequest request = OkHttpTask.post(url).params(key, resource);
        addTask(url, request, listener);
    }

    /** 添加一个上传任务 */
    public <T> void addTask(String taskKey, @NonNull BaseBodyRequest request, UploadListener<T> listener) {
        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setTaskKey(taskKey);
        uploadInfo.setState(UploadManager.NONE);
        uploadInfo.setRequest(request);
        mUploadInfoList.add(uploadInfo);
        //构造即开始执行
        UploadTask uploadTask = new UploadTask<T>(uploadInfo, listener);
        uploadInfo.setTask(uploadTask);
    }

    public UploadThreadPool getThreadPool() {
        return threadPool;
    }

    public UploadUIHandler getHandler() {
        return mUploadUIHandler;
    }

    public List<UploadInfo> getAllTask() {
        return mUploadInfoList;
    }
}