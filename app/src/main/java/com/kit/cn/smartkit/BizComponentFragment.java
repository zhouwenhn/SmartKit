package com.kit.cn.smartkit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kit.cn.library.ioc.annotations.field.InjectChildView;
import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.library.pagekit.FragmentWrapper;
import com.kit.cn.smartkit.base.BaseFragment;

/**
 * Created by zhouwen on 16/7/23.
 */

//@InjectContentView(value = R.layout.biz_component_fragment)
public class BizComponentFragment extends FragmentWrapper implements View.OnClickListener{

//    @InjectChildView(value = R.id.btn_login, listener = View.OnClickListener.class)
//    private Button mBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.biz_component_fragment, container, false);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Biz component", Toast.LENGTH_LONG).show();
    }
}
