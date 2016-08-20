package com.kit.cn.smartkit.network_sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;


import com.kit.cn.library.network.OkHttpTask;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.network_sample.utils.Constant;
import com.kit.cn.smartkit.network_sample.utils.Urls;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class SyncActivity extends BaseActivity {

    private Handler handler = new InnerHandler();

    private static class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                Response response = (Response) msg.obj;
                //对response需要自己做解析
                String data = response.body().string();
                System.out.println("同步请求的数据：" + data);
                Toast.makeText(OkHttpTask.getContext(), "同步请求成功" + data, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);
        setTitle(Constant.getData().get(8)[0]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpTask.getInstance().cancelTag(this);
    }

    @OnClick(R.id.sync)
    public void sync(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //同步会阻塞主线程，必须开线程
                    Response response = OkHttpTask.get(Urls.URL_JSONOBJECT)//
                            .tag(this)//
                            .headers("header1", "headerValue1")//
                            .params("param1", "paramValue1")//
                            .execute();  //不传callback即为同步请求

                    Message message = Message.obtain();
                    message.obj = response;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
