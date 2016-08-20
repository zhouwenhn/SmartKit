package com.kit.cn.library.network.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by zhouwen on 16/8/18.
 */
public abstract class BitmapCallback extends AbsCallback<Bitmap> {

    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }
}
