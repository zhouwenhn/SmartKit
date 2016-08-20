package com.kit.cn.smartkit;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kit.cn.library.ioc.annotations.field.InjectChildView;
import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.smartkit.base.BaseFragment;

/**
 * Created by zhouwen on 16/7/23.
 */

@InjectContentView(value = R.layout.biz_component_fragment)
public class BizComponentFragment extends BaseFragment implements View.OnClickListener{

    @InjectChildView(value = R.id.btn_login, listener = View.OnClickListener.class)
    private Button mBtn;


    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Biz component", Toast.LENGTH_LONG).show();
    }
}
