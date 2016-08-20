package com.kit.cn.smartkit.network_sample.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;


import com.kit.cn.library.network.OkHttpTask;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.network_sample.callback.BitmapDialogCallback;
import com.kit.cn.smartkit.network_sample.utils.Constant;
import com.kit.cn.smartkit.network_sample.utils.Urls;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class BitmapRequestActivity extends BaseActivity {

    @Bind(R.id.imageView) ImageView imageView;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bitmap_request);
        ButterKnife.bind(this);
        setTitle(Constant.getData().get(2)[0]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpTask.getInstance().cancelTag(this);
    }

    @OnClick(R.id.requestImage)
    public void requestJson(View view) {
        OkHttpTask.get(Urls.URL_IMAGE)//
                .tag(this)//
                .headers("header1", "headerValue1")//
                .params("param1", "paramValue1")//
                .execute(new BitmapDialogCallback(this) {
                    @Override
                    public void onResponse(boolean isFromCache, Bitmap bitmap, Request request, Response response) {
                        handleResponse(isFromCache, bitmap, request, response);
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        handleError(isFromCache, call, response);
                    }
                });
    }
}
