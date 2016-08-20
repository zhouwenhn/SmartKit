package com.kit.cn.library.network.request;


import com.kit.cn.library.network.utils.HttpUtils;
import com.kit.cn.library.network.utils.OkLogger;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhouwen on 16/8/18.
 */
public class PutRequest extends BaseBodyRequest<PutRequest> {

    public PutRequest(String url) {
        super(url);
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        try {
            headers.put("Content-Length", String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.put(requestBody).url(url).tag(tag).build();
    }
}