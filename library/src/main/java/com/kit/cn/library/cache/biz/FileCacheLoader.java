package com.kit.cn.library.cache.biz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.kit.cn.library.cache.ValueType;
import com.kit.cn.library.cache.config.CacheConfig;
import com.kit.cn.library.cache.utils.CacheHelper;
import com.kit.cn.library.cache.utils.CheckException;
import com.kit.cn.library.cache.utils.FileNameGenerator;
import com.kit.cn.library.utils.log.L;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 * @author zhouwen
 * @version 0.1
 * @since 2016/4/8
 */
public class FileCacheLoader<V> extends AbsCache<String, V> implements IUpdateCache {

    private static FileCacheLoader sFileCacheLoader;

    private final static int MB = 1024 * 1024;

    private final static int CACHE_SIZE = 20 * MB;

    private final static int SD_CARD_SIZE = 10 * MB;

    private static String sPathDir = "";

    public FileCacheLoader() {
        if (TextUtils.isEmpty(sPathDir))
            throw new RuntimeException("sPathDir can't empty, please sure not nullpoint");

        File file = new File(sPathDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static FileCacheLoader getInstance() {
        if (sFileCacheLoader == null) {
            synchronized (FileCacheLoader.class) {
                if (sFileCacheLoader == null) {
                    sFileCacheLoader = new FileCacheLoader();
                }
            }
        }
        return sFileCacheLoader;
    }

    /**
     * set cache dir
     * @param pathDir cache path dir
     */
    public static void setFileCacheDir(@ NonNull String pathDir){
        if (!TextUtils.isEmpty(pathDir)){
            sPathDir = pathDir;
        } else {
            sPathDir = CacheConfig.CACHE_PATH;
        }
    }

    @Override
    public V getCache(String s) {
        return null;
    }

    /**
     * get data from file's cache
     * @param key key
     * @return cache's data
     */
    @Override
    public V getCache(@NonNull String key, int valueType) {
        long startGetCache = System.currentTimeMillis();
        String path = sPathDir + File.separator + FileNameGenerator.generator(key);
        L.d("cache>>>>getCache path>>" + path);
        File file = new File(path);

        if (valueType == ValueType.BITMAP.getValue()) {
            if (file != null && file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap == null) {
                    file.delete();
                } else {
                    CacheHelper.updateFileCreateTime(path);
                    return (V) bitmap;
                }
            }
        } else if (valueType == ValueType.STRING.getValue()) {
            byte[] buffer = new byte[1024];
            BufferedInputStream bs = null;
            StringBuffer value = new StringBuffer();
            if (file != null && file.exists()) {
                try {
                    bs = new BufferedInputStream(new FileInputStream(file));
                    int bytesRead = 0;
                    while ((bytesRead = bs.read(buffer)) != -1) {
                        value.append(new String(buffer, 0, bytesRead));
                    }
                    if (value == null) {
                        file.delete();
                    } else {
                        CacheHelper.updateFileCreateTime(path);
                        return (V) value.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    L.d("cache>>>>get data from cache duration=" + (System.currentTimeMillis() - startGetCache) / 1000 + "s");
                    if (bs != null) {
                        try {
                            bs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * add data to file's cache
     * @param key key
     * @param value value
     */
    @Override
    public void addCache(String key, V value) {
        long startWriteTimeMillis = System.currentTimeMillis();
        File rootFile = new File(sPathDir);
        File file = new File(sPathDir + File.separator + FileNameGenerator.generator(key));
        L.d("cache>>>>addCache path>>" + file.toString());
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(FileCacheLoader.class.getSimpleName(), "sdcard is not exist");
            return;
        }
        //大于sdcard space & file space，更新下存储空间
        if (CACHE_SIZE < CacheHelper.getCacheSize(rootFile.toString()) || SD_CARD_SIZE > CacheHelper.getSDCardFreeSpace()) {
            updateCacheSize(rootFile.toString());
        }
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        BufferedOutputStream bos = null;
        try {
            file.createNewFile();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            if (value instanceof String) {
                byte[] b = ((String) value).getBytes();
                bos.write(b);
            } else if (value instanceof Bitmap) {
                ((Bitmap) value).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            L.d("cache>>>>write data to cache duration=" + (System.currentTimeMillis() - startWriteTimeMillis) / 1000 + "s");
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * update cache size
     * @param path path
     * @return update success return true otherwise false
     */
    @Override
    public boolean updateCacheSize(@NonNull String path) {
        CheckException.checkNPE(path);
        File[] files = new File(path).listFiles();
        if (files == null) {
            return true;
        }
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        int length = files.length;
        //升序排列文件
        Collections.sort(Arrays.asList(files), new FileLastModifySort());
        //清除50%最近没用的文件
        int removeCount = (int) (0.5 * length);
        for (int i = 0; i < removeCount; i++) {
            File file = files[i];
            if (file.exists()) {
                file.delete();
            }
        }
        return true;
    }

    /**
     * remove file's cache
     * @param key key
     */
    @Override
    public void remove(@NonNull String key) {
        File file = new File(sPathDir + File.separator + FileNameGenerator.generator(key));
        L.d("cache>>>>remove path>>" + file.toString());
        file.delete();
    }

    /**
     * update file's cache
     * @param key key
     * @param value value
     */
    @Override
    public void update(String key, V value) {
        remove(key);
        addCache(key, value);
    }

    /**
     * remove all file's cache
     */
    @Override
    public void removeAll() {
        File file = new File(sPathDir);
        file.delete();
    }

    /**
     * release cache size
     * @return success return true otherwise false
     */
    public boolean releaseCachesize() {
        File rootFile = new File(sPathDir);
        if (CACHE_SIZE < CacheHelper.getCacheSize(rootFile.toString()) || SD_CARD_SIZE > CacheHelper.getSDCardFreeSpace()) {
            return updateCacheSize(rootFile.toString());
        }
        return false;
    }

    private class FileLastModifySort implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() > rhs.lastModified()) {
                return 1;
            } else if (lhs.lastModified() == rhs.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
