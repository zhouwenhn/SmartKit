package com.kit.cn.library.download.download;

import android.os.Handler;
import android.os.Message;

import com.kit.cn.library.download.listener.DownloadListener;
import com.kit.cn.library.network.utils.OkLogger;


/**
 * Created by zhouwen on 16/8/19.
 */
public class DownloadUIHandler extends Handler {

    private DownloadListener mGlobalDownloadListener;

    @Override
    public void handleMessage(Message msg) {
        MessageBean messageBean = (MessageBean) msg.obj;
        if (messageBean != null) {
            DownloadInfo info = messageBean.downloadInfo;
            String errorMsg = messageBean.errorMsg;
            Exception e = messageBean.e;
            if (mGlobalDownloadListener != null) {
                executeListener(mGlobalDownloadListener, info, errorMsg, e);
            }
            DownloadListener listener = info.getListener();
            if (listener != null) executeListener(listener, info, errorMsg, e);
        } else {
            OkLogger.e("DownloadUIHandler DownloadInfo null");
        }
    }

    private void executeListener(DownloadListener listener, DownloadInfo info, String errorMsg, Exception e) {
        int state = info.getState();
        switch (state) {
            case DownloadManager.NONE:
            case DownloadManager.WAITING:
            case DownloadManager.DOWNLOADING:
            case DownloadManager.PAUSE:
                listener.onProgress(info);
                break;
            case DownloadManager.FINISH:
                listener.onProgress(info);   //结束前再次回调进度，避免最后一点数据没有刷新
                listener.onFinish(info);
                break;
            case DownloadManager.ERROR:
                listener.onProgress(info);   //结束前再次回调进度，避免最后一点数据没有刷新
                listener.onError(info, errorMsg, e);
                break;
        }
    }

    public void setGlobalDownloadListener(DownloadListener downloadListener) {
        this.mGlobalDownloadListener = downloadListener;
    }

    public static class MessageBean {
        public DownloadInfo downloadInfo;
        public String errorMsg;
        public Exception e;
    }
}
