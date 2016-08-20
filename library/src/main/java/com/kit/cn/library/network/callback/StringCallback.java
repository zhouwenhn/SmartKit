package com.kit.cn.library.network.callback;

import okhttp3.Response;

/**
 * Created by zhouwen on 16/8/18.
 */
public abstract class StringCallback extends AbsCallback<String> {

    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        return response.body().string();
    }
}
