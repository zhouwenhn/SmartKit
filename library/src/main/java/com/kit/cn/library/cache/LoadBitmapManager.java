package com.kit.cn.library.cache;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.kit.cn.library.cache.biz.FileCacheLoader;
import com.kit.cn.library.cache.biz.MemoryCacheLoader;
import com.kit.cn.library.utils.BitmapUtils;
import com.kit.cn.library.utils.log.L;


/**
 * @author zhouwen
 * @version 0.1
 * @since 2015/11/15
 */
public class LoadBitmapManager implements ICacheAble<String, Bitmap> {

    private static LoadBitmapManager sLoadCacheData;

    public LoadBitmapManager() {
    }

    public static LoadBitmapManager getInstance() {
        if (sLoadCacheData == null) {
            synchronized (LoadBitmapManager.class) {
                if (sLoadCacheData == null) {
                    sLoadCacheData = new LoadBitmapManager();
                }
            }
        }
        return sLoadCacheData;
    }

    /**
     * load from network
     * @param key key
     * @return from network data
     */
    private Bitmap loadFromNetwork(String key) {
        //from network
        return null;
    }

    /**
     * set cache dir
     * @param pathDir path dir
     */
    public void setFileCacheDir(String pathDir){
        FileCacheLoader.setFileCacheDir(pathDir);
    }

    /**
     * put cache to file
     * @param key key
     * @param value value
     */
    @Override
    public void put(String key, Bitmap value) {
        MemoryCacheLoader.getInstance().addCache(key, value);
        FileCacheLoader.getInstance().addCache(key, value);
    }

    public void put(String key, Drawable value) {
        MemoryCacheLoader.getInstance().addCache(key, BitmapUtils.drawableToBitmap(value));
        FileCacheLoader.getInstance().addCache(key, BitmapUtils.drawableToBitmap(value));
    }

    /**
     * get data from cache
     * @param key
     * @return
     */
    @Override
    public Bitmap getCache(String key) {
        Bitmap value = (Bitmap) MemoryCacheLoader.getInstance().getCache(key);
        if (value == null) {
            value = (Bitmap) FileCacheLoader.getInstance().getCache(key, ValueType.BITMAP.getValue());
            if (value != null) {
                L.d("cache>>>>getCache from file cache>>");
                MemoryCacheLoader.getInstance().addCache(key, value);
            } else {
                value = loadFromNetwork(key);
                if (value != null) {
                    MemoryCacheLoader.getInstance().addCache(key, value);
                    FileCacheLoader.getInstance().addCache(key, value);
                }
            }
        } else {
            L.d("cache>>>>getCache from memory cache>>");
        }
        return value;
    }

    /**
     * remove cache
     * @param key key
     */
    @Override
    public void remove(String key) {
        MemoryCacheLoader.getInstance().remove(key);
        FileCacheLoader.getInstance().remove(key);
    }

    /**
     * update cache
     * @param key key
     * @param value value
     */
    @Override
    public void update(String key, Bitmap value) {
        MemoryCacheLoader.getInstance().update(key, value);
        FileCacheLoader.getInstance().update(key, value);
    }
}
