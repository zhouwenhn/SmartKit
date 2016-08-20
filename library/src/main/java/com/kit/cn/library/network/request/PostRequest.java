package com.kit.cn.library.network.request;

import com.kit.cn.library.network.utils.HttpUtils;
import com.kit.cn.library.network.utils.OkLogger;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhouwen on 16/8/18.
 */
public class PostRequest extends BaseBodyRequest<PostRequest> {

    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    protected MediaType mediaType;      //上传的MIME类型
    protected String string;            //上传的文本内容
    protected String json;              //上传的Json
    protected byte[] bs;                //上传的字节数据

    public PostRequest(String url) {
        super(url);
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    public PostRequest postString(String string) {
        this.string = string;
        this.mediaType = MEDIA_TYPE_PLAIN;
        return this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    public PostRequest postJson(String json) {
        this.json = json;
        this.mediaType = MEDIA_TYPE_JSON;
        return this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    public PostRequest postBytes(byte[] bs) {
        this.bs = bs;
        this.mediaType = MEDIA_TYPE_STREAM;
        return this;
    }

    public PostRequest mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    protected RequestBody generateRequestBody() {
        if (requestBody != null)
            return requestBody;                                           //自定义的请求体
        if (string != null && mediaType != null)
            return RequestBody.create(mediaType, string); //post上传字符串数据
        if (json != null && mediaType != null)
            return RequestBody.create(mediaType, json);     //post上传json数据
        if (bs != null && mediaType != null)
            return RequestBody.create(mediaType, bs);         //post上传字节数组
        return HttpUtils.generateMultipartRequestBody(params);
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        try {
            headers.put("Content-Length", String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}