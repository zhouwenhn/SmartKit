package com.kit.cn.smartkit.network_sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kit.cn.library.network.OkHttpTask;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.network_sample.callback.StringDialogCallback;
import com.kit.cn.smartkit.network_sample.utils.Constant;
import com.kit.cn.smartkit.network_sample.utils.Urls;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class RedirectActivity extends BaseActivity {

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_redirect);
        ButterKnife.bind(this);
        setTitle(Constant.getData().get(9)[0]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpTask.getInstance().cancelTag(this);
    }

    @OnClick(R.id.redirect)
    public void redirect(View view) {
        OkHttpTask.get(Urls.URL_REDIRECT)//
                .tag(this)//
                .headers("header1", "headerValue1")//
                .params("param1", "paramValue1")//
                .execute(new RedirectCallBack(this));
    }

    private class RedirectCallBack extends StringDialogCallback {
        public RedirectCallBack(Activity activity) {
            super(activity);
        }

        @Override
        public void onResponse(boolean isFromCache, String data, Request request, Response response) {
            handleResponse(isFromCache, data, request, response);

            responseData.setText("注意看请求头的url和响应头的url是不一样的！\n这代表了在请求过程中发生了重定向，" +
                    "okhttp默认将重定向封装在了请求内部，只有最后一次请求的数据会被真正的请求下来触发回调，中间过程" +
                    "是默认实现的，不会触发回调！");
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            handleError(isFromCache, call, response);
        }
    }
}