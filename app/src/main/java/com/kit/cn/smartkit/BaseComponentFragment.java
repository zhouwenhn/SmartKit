package com.kit.cn.smartkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.cn.library.message.IMessageAble;
import com.kit.cn.library.message.Message;
import com.kit.cn.library.message.MessagePump;
import com.kit.cn.library.pagekit.FragmentWrapper;
import com.kit.cn.library.utils.log.Logger;
import com.kit.cn.library.widget.toast.ToastUtil;
import com.kit.cn.smartkit.cache_sample.CacheFragment;
import com.kit.cn.smartkit.db_sample.ORMFragment;
import com.kit.cn.smartkit.download_sample.DownloadFragment;
import com.kit.cn.smartkit.message_sample.MessageFragment;
import com.kit.cn.smartkit.network_sample.activity.OkhttpMainActivity;
import com.kit.cn.smartkit.utils_sample.UtilsFragment;

import static com.kit.cn.library.message.Message.Type.MSG_EXAMPLE_TEST;

/**
 * Created by zhouwen on 16/7/23.
 */

//@InjectContentView(value = R.layout.base_component_fragment)
public class BaseComponentFragment extends FragmentWrapper
        implements IMessageAble, View.OnClickListener {

//    @InjectChildView(value = R.id.btn_orm, listener = View.OnClickListener.class)
//    private Button mBtnOrm;
//
//    @InjectChildView(value = R.id.btn_net, listener = View.OnClickListener.class)
//    private Button mBtnNet;
//
//    @InjectChildView(value = R.id.btn_download, listener = View.OnClickListener.class)
//    private Button mBtnDownload;
//
//    @InjectChildView(value = R.id.btn_cache, listener = View.OnClickListener.class)
//    private Button mBtnCache;
//
//    @InjectChildView(value = R.id.btn_log, listener = View.OnClickListener.class)
//    private Button mBtnLog;
//
//    @InjectChildView(value = R.id.btn_invoke, listener = View.OnClickListener.class)
//    private Button mBtnInvoke;
//
//    @InjectChildView(value = R.id.btn_utils, listener = View.OnClickListener.class)
//    private Button mBtnUtils;
//
//    @InjectChildView(value = R.id.btn_mes, listener = View.OnClickListener.class)
//    private Button mBtnMes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MessagePump.getInstance().register(MSG_EXAMPLE_TEST, this);
        View view = inflater.inflate(R.layout.base_component_fragment, container, false);
        setOnClickListener(view);
        Logger.e("getFilesDir>>"+getContext().getFilesDir().getAbsolutePath());
        return view;
    }

    private void setOnClickListener(View view) {
        view.findViewById(R.id.btn_orm).setOnClickListener(this);
        view.findViewById(R.id.btn_net).setOnClickListener(this);
        view.findViewById(R.id.btn_download).setOnClickListener(this);
        view.findViewById(R.id.btn_cache).setOnClickListener(this);
        view.findViewById(R.id.btn_log).setOnClickListener(this);
        view.findViewById(R.id.btn_invoke).setOnClickListener(this);
        view.findViewById(R.id.btn_utils).setOnClickListener(this);
        view.findViewById(R.id.btn_mes).setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        MessagePump.getInstance().unregister(MSG_EXAMPLE_TEST, this);
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_orm:
                ToastUtil.showToast(getActivity(), "dev ing");
                openPage(new ORMFragment());
                break;
            case R.id.btn_net:
                startActivity(new Intent(getActivity(), OkhttpMainActivity.class));
                break;
            case R.id.btn_download:
                openPage(new DownloadFragment());
                break;
            case R.id.btn_cache:
                openPage(new CacheFragment());
                break;
            case R.id.btn_log:
                ToastUtil.showToast(getActivity(), "sample>>>Logger.d(\"see logcat!!\")");
                break;
            case R.id.btn_invoke:
                ToastUtil.showToast(getActivity(), "the current pages is view inject");
                break;
            case R.id.btn_utils:
                openPage(new UtilsFragment());
                break;
            case R.id.btn_mes:
                openPage(new MessageFragment());
                break;
        }
    }

    @Override
    public void onMessage(Message message) {
        switch (message.type){
            case MSG_EXAMPLE_TEST:
                ToastUtil.showToast(getContext(), "收到发送消息>>来自消息界面<MessageFragment>");
                break;
        }
    }
}
