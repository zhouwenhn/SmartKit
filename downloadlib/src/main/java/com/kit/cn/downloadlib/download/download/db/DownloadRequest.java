package com.kit.cn.downloadlib.download.download.db;



import com.kit.cn.library.network.cache.CacheMode;
import com.kit.cn.library.network.model.HttpHeaders;
import com.kit.cn.library.network.model.HttpParams;
import com.kit.cn.library.network.request.BaseRequest;
import com.kit.cn.library.network.request.DeleteRequest;
import com.kit.cn.library.network.request.GetRequest;
import com.kit.cn.library.network.request.HeadRequest;
import com.kit.cn.library.network.request.OptionsRequest;
import com.kit.cn.library.network.request.PostRequest;
import com.kit.cn.library.network.request.PutRequest;

import java.io.Serializable;

/**
 * Created by zhouwen on 16/8/19.
 */
public class DownloadRequest implements Serializable {

    private static final long serialVersionUID = -6883956320373276785L;

    public String method;
    public String url;
    public CacheMode cacheMode;
    public String cacheKey;
    public long cacheTime;
    public HttpParams params;
    public HttpHeaders headers;

    public static String getMethod(BaseRequest request) {
        if (request instanceof GetRequest) return "get";
        if (request instanceof PostRequest) return "post";
        if (request instanceof PutRequest) return "put";
        if (request instanceof DeleteRequest) return "delete";
        if (request instanceof OptionsRequest) return "options";
        if (request instanceof HeadRequest) return "head";
        return "";
    }

    public static BaseRequest createRequest(String url, String method) {
        if (method.equals("get")) return new GetRequest(url);
        if (method.equals("post")) return new PostRequest(url);
        if (method.equals("put")) return new PutRequest(url);
        if (method.equals("delete")) return new DeleteRequest(url);
        if (method.equals("options")) return new OptionsRequest(url);
        if (method.equals("head")) return new HeadRequest(url);
        return null;
    }
}
