package com.kit.cn.library.download.download.db;

import com.kit.cn.library.download.download.DownloadInfo;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhouwen on 16/8/19.
 */
public enum DownloadDBManager {

    INSTANCE;

    private Lock mLock;
    private DownloadInfoDao infoDao;

    DownloadDBManager() {
        mLock = new ReentrantLock();
        infoDao = new DownloadInfoDao();
    }

    /** 获取下载任务 */
    public DownloadInfo get(String key) {
        mLock.lock();
        try {
            return infoDao.get(key);
        } finally {
            mLock.unlock();
        }
    }

    /** 获取所有下载信息 */
    public List<DownloadInfo> getAll() {
        mLock.lock();
        try {
            return infoDao.getAll();
        } finally {
            mLock.unlock();
        }
    }

    /** 更新下载任务，没有就创建，有就替换 */
    public DownloadInfo replace(DownloadInfo entity) {
        mLock.lock();
        try {
            infoDao.replace(entity);
            return entity;
        } finally {
            mLock.unlock();
        }
    }

    /** 移除下载任务 */
    public void delete(String key) {
        mLock.lock();
        try {
            infoDao.delete(key);
        } finally {
            mLock.unlock();
        }
    }

    /** 创建下载任务 */
    public void create(DownloadInfo entity) {
        mLock.lock();
        try {
            infoDao.create(entity);
        } finally {
            mLock.unlock();
        }
    }

    /** 更新下载任务 */
    public void update(DownloadInfo entity) {
        mLock.lock();
        try {
            infoDao.update(entity);
        } finally {
            mLock.unlock();
        }
    }

    /** 清空下载任务 */
    public boolean clear() {
        mLock.lock();
        try {
            return infoDao.deleteAll() > 0;
        } finally {
            mLock.unlock();
        }
    }
}