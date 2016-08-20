package com.kit.cn.smartkit.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.cn.library.ioc.AnnotationProcessor;
import com.kit.cn.library.pagekit.FragmentWrapper;


/**
 * Fragment基类
 * @author zhouwen
 * @version 0.1
 * @since 16/7/23
 */
public class BaseFragment extends FragmentWrapper implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = AnnotationProcessor.getInstance().invokeContentView(getClass(), this,
                inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
