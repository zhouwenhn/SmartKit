package com.kit.cn.library.network.request;

import com.kit.cn.library.network.utils.HttpUtils;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhouwen on 16/8/18.
 */
public class GetRequest extends BaseRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected RequestBody generateRequestBody() {
        return null;
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        url = HttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);
        return requestBuilder.get().url(url).tag(tag).build();
    }
}