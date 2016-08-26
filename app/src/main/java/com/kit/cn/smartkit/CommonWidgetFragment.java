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
import com.kit.cn.smartkit.base.BaseFragment;

/**
 * Created by zhouwen on 16/7/23.
 */

//@InjectContentView(value = R.layout.common_widget_fragment)
public class CommonWidgetFragment extends BaseFragment {

//    @InjectChildView(value = R.id.btn_dialog, listener = View.OnClickListener.class)
//    private Button mBtnDialog;
//
//    @InjectChildView(value = R.id.btn_toast, listener = View.OnClickListener.class)
//    private Button mBtnToast;
//
//    @InjectChildView(value = R.id.btn_adapter, listener = View.OnClickListener.class)
//    private Button mBtnAdapter;
//
//    @InjectChildView(value = R.id.btn_loading, listener = View.OnClickListener.class)
//    private Button mBtnLoading;
//
//    @InjectChildView(value = R.id.btn_ui, listener = View.OnClickListener.class)
//    private Button mBtnUi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_widget_fragment, container, false);
        setOnClickListener(view);
        return view;
    }

    private void setOnClickListener(View view) {
        view.findViewById(R.id.btn_dialog).setOnClickListener(this);
        view.findViewById(R.id.btn_toast).setOnClickListener(this);
        view.findViewById(R.id.btn_adapter).setOnClickListener(this);
        view.findViewById(R.id.btn_loading).setOnClickListener(this);
        view.findViewById(R.id.btn_ui).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog:
                Toast.makeText(getActivity(), "btn_dialog!", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_toast:
                Toast.makeText(getActivity(), "btn_toast!", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_adapter:
                Toast.makeText(getActivity(), "btn_adapter!", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_loading:
                Toast.makeText(getActivity(), "btn_loading!", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_ui:
                Toast.makeText(getActivity(), "btn_ui!", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
