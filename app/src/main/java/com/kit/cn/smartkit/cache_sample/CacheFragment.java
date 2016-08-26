package com.kit.cn.smartkit.cache_sample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.cn.library.cache.LoadDataManager;
import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.library.widget.toast.ToastUtil;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.base.BaseFragment;

import java.io.File;

/**
 * Created by zhouwen on 16/7/23.
 */

//@InjectContentView(value = R.layout.cache_fragment)
public class CacheFragment extends BaseFragment {

//    @InjectChildView(value = R.id.btn, listener = View.OnClickListener.class)
//    private Button mBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cache_fragment, container, false);
        setOnClickListener(view);
        LoadDataManager.getInstance().setFileCacheDir(
                getContext().getFilesDir().getAbsolutePath() + File.separator + "cache_data");
        return view;
    }

    private void setOnClickListener(View view) {
        view.findViewById(R.id.insert).setOnClickListener(this);
        view.findViewById(R.id.update).setOnClickListener(this);
        view.findViewById(R.id.remove).setOnClickListener(this);
        view.findViewById(R.id.query).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert:
                LoadDataManager.getInstance().put("key", "SmartKit!!!!!");
                ToastUtil.showToast(getContext(),"sava \"SmartKit!!!!!\"");
                break;
            case R.id.update:
                LoadDataManager.getInstance().update("key", "update cache!!!!!");
                ToastUtil.showToast(getContext(),"update \"update cache\"");
                break;
            case R.id.remove:
                LoadDataManager.getInstance().remove("key");
                break;
            case R.id.query:
                ToastUtil.showToast(getContext(),"query>>>"+LoadDataManager.getInstance().getCache("key"));
                break;
        }
       
    }
}
