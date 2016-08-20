package com.kit.cn.library.download.download.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.kit.cn.library.download.download.DownloadInfo;
import com.kit.cn.library.network.cache.DataBaseDao;

import java.util.List;


/**
 * Created by zhouwen on 16/8/19.
 */
public class DownloadInfoDao extends DataBaseDao<DownloadInfo> {

    public DownloadInfoDao() {
        super(new DownloadInfoHelper());
    }

    /** 根据key获取缓存 */
    public DownloadInfo get(String key) {
        String selection = DownloadInfo.TASK_KEY + "=?";
        String[] selectionArgs = new String[]{key};
        List<DownloadInfo> infos = get(selection, selectionArgs);
        return infos.size() > 0 ? infos.get(0) : null;
    }

    public void delete(String taskKey) {
        delete(DownloadInfo.TASK_KEY + "=?", new String[]{taskKey});
    }

    public int update(DownloadInfo downloadInfo) {
        return update(downloadInfo, DownloadInfo.TASK_KEY + "=?", new String[]{downloadInfo.getTaskKey()});
    }

    @Override
    public List<DownloadInfo> getAll() {
        return get(null, null, null, null, null, DownloadInfo.ID + " ASC", null);
    }

    @Override
    public DownloadInfo parseCursorToBean(Cursor cursor) {
        return DownloadInfo.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(DownloadInfo downloadInfo) {
        return DownloadInfo.buildContentValues(downloadInfo);
    }

    @Override
    protected String getTableName() {
        return DownloadInfoHelper.TABLE_NAME;
    }
}