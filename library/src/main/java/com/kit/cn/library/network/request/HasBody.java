package com.kit.cn.library.network.request;

import android.support.annotation.NonNull;


import com.kit.cn.library.network.model.HttpParams;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zhouwen on 16/8/18.
 */
public interface HasBody<R> {
    R requestBody(@NonNull RequestBody requestBody);

    R params(String key, File file);

    R addFileParams(String key, List<File> files);

    R addFileWrapperParams(String key, List<HttpParams.FileWrapper> fileWrappers);

    R params(String key, File file, String fileName);

    R params(String key, File file, String fileName, MediaType contentType);
}