package com.kit.cn.library.cache;


import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.kit.cn.library.cache.biz.FileCacheLoader;
import com.kit.cn.library.cache.biz.MemoryCacheLoader;
import com.kit.cn.library.utils.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author zhouwen
 * @version 0.1
 * @since 2015/11/15
 */
public class LoadDataManager implements ICacheAble<String, String> {

    private static LoadDataManager sLoadCacheData;

    public LoadDataManager() {
    }

    public static LoadDataManager getInstance() {
        if (sLoadCacheData == null) {
            synchronized (LoadDataManager.class) {
                if (sLoadCacheData == null) {
                    sLoadCacheData = new LoadDataManager();
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
    private String loadFromNetwork(String key) {
        //from network
        return null;
    }

    /**
     * set cache dir
     * @param pathDir path dir
     */
    public void setFileCacheDir(@NonNull String pathDir){
        FileCacheLoader.setFileCacheDir(pathDir);
    }

    /**
     * put cache to file
     * @param key key
     * @param value value
     */
    @Override
    public void put(@NonNull String key, String value) {
        MemoryCacheLoader.getInstance().addCache(key, value);
        FileCacheLoader.getInstance().addCache(key, value);
    }

    public void put(@NonNull String key, JSONObject value) {
        MemoryCacheLoader.getInstance().addCache(key, value.toString());
        FileCacheLoader.getInstance().addCache(key, value.toString());
    }

    public void put(@NonNull String key, JSONArray value) {
        MemoryCacheLoader.getInstance().addCache(key, value.toString());
        FileCacheLoader.getInstance().addCache(key, value.toString());
    }

    public void put(@NonNull String key, Float value) {
        MemoryCacheLoader.getInstance().addCache(key, String.valueOf(value));
        FileCacheLoader.getInstance().addCache(key, String.valueOf(value));
    }

    public void put(@NonNull String key, Integer value) {
        MemoryCacheLoader.getInstance().addCache(key, String.valueOf(value));
        FileCacheLoader.getInstance().addCache(key, String.valueOf(value));
    }

    public void put(@NonNull String key, Boolean value) {
        MemoryCacheLoader.getInstance().addCache(key, String.valueOf(value));
        FileCacheLoader.getInstance().addCache(key, String.valueOf(value));
    }

    public void put(@NonNull String key, byte[] value) {
        try {
            String srt =new String(value,"UTF-8");
            MemoryCacheLoader.getInstance().addCache(key, srt);
            FileCacheLoader.getInstance().addCache(key, srt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void put(@NonNull String key, Serializable value) {
        MemoryCacheLoader.getInstance().addCache(key, value.toString());
        FileCacheLoader.getInstance().addCache(key, value.toString());
    }

    /**
     * get data from cache
     * @param key
     * @return
     */
    @Override
    public String getCache(String key) {
        String value = (String) MemoryCacheLoader.getInstance().getCache(key);
        if (value == null) {
            value = (String) FileCacheLoader.getInstance().getCache(key, ValueType.STRING.getValue());
            if (value != null) {
                Logger.d("cache>>>>getCache from file cache>>");
                MemoryCacheLoader.getInstance().addCache(key, value);
            } else {
                value = loadFromNetwork(key);
                if (value != null) {
                    MemoryCacheLoader.getInstance().addCache(key, value);
                    FileCacheLoader.getInstance().addCache(key, value);
                }
            }
        } else {
            Logger.d("cache>>>>getCache from memory cache>>");
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
    public void update(String key, String value) {
        MemoryCacheLoader.getInstance().update(key, value);
        FileCacheLoader.getInstance().update(key, value);
    }

//    public static final int BITMAP = 1;
//    public static final int STRING = 2;
//
//    @IntDef({BITMAP, STRING})
//    @Retention(RetentionPolicy.SOURCE)
//    @Target(ElementType.PARAMETER)
//    public  @interface Type{
//
//    }
}
