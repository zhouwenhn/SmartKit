package com.kit.cn.library.cache.biz;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.kit.cn.library.cache.utils.CheckException;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * @author zhouwen
 * @version 0.1
 * @since 2016/4/8
 */
public class MemoryCacheLoader<V> extends AbsCache<String, V> {

    private static MemoryCacheLoader sMemoryCacheLoader;

    private final static int MB = 1024 * 1024;

    private static int LRC_CACHE_SIZE = 6 * MB;

    private static int SOFT_REFERENCE_COUNT = 30;

    private LruCache<String, V> mLruCache;

    private LinkedHashMap<String, SoftReference<V>> mLinkedHashMap;

    public static MemoryCacheLoader getInstance() {
        if (sMemoryCacheLoader == null) {
            synchronized (MemoryCacheLoader.class) {
                if (sMemoryCacheLoader == null) {
                    sMemoryCacheLoader = new MemoryCacheLoader();
                }
            }
        }
        return sMemoryCacheLoader;
    }

    public MemoryCacheLoader() {
        mLruCache = new LruCache<String, V>(LRC_CACHE_SIZE) {
            @Override
            protected void entryRemoved(boolean evicted, String key, V oldValue, V newValue) {
                if (oldValue != null) {
                    mLinkedHashMap.put(key, new SoftReference<V>(oldValue));
                }
            }

            @Override
            protected int sizeOf(String key, V value) {
                if (value instanceof String)
                    return ((String) value).length();
                else if (value instanceof Bitmap)
                    return ((Bitmap) value).getRowBytes() * ((Bitmap) value).getHeight();
                else
                    return 0;
            }
        };

        mLinkedHashMap = new LinkedHashMap<String, SoftReference<V>>(SOFT_REFERENCE_COUNT, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<V>> eldest) {
                if (size() > SOFT_REFERENCE_COUNT) {
                    return true;
                }
                return super.removeEldestEntry(eldest);
            }
        };
    }

    @Override
    public V getCache(String s, int valueType) {
        return null;
    }

    /**
     * get memory's cache
     * @param key key
     * @return cache's data
     */
    @Override
    public V getCache(@NonNull String key) {
        CheckException.checkNPE(key);
        //先到强引用中取，没取到再去弱引用取
        V value;
        synchronized (MemoryCacheLoader.class) {
            value = mLruCache.get(key);
            if (value != null) {
                mLruCache.remove(key);
                mLinkedHashMap.put(key, new SoftReference<>(value));
            } else {
                SoftReference<V> softReference = mLinkedHashMap.get(key);
                if (softReference != null){
                    value = softReference.get();
                    if (value != null) {
                        mLruCache.put(key, value);
                        mLinkedHashMap.remove(key);
                    }
                }
            }
        }
        return value;
    }

    /**
     * add data to memory's cache
     * @param key key
     * @param value value
     */
    @Override
    public void addCache(@NonNull String key, V value) {
        CheckException.checkNPE(key);
        CheckException.checkNPE(value);
        synchronized (mLruCache) {
            mLruCache.put(key, value);
        }
    }

    /**
     * remove cache
     * @param key key
     */
    @Override
    public void remove(@NonNull String key) {
        synchronized (mLruCache) {
            mLruCache.remove(key);
            mLinkedHashMap.remove(key);
        }
    }

    /**
     * update memory's cache
     * @param key key
     * @param value value
     */
    @Override
    public void update(String key, V value) {
        remove(key);
        addCache(key, value);
    }

    /**
     * remove all memory's cache
     */
    @Override
    public void removeAll() {
        mLruCache.evictAll();
        mLinkedHashMap.clear();
    }
}
