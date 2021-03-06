package com.kit.cn.smartkit.message_sample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.base.BaseFragment;


/**
 * Created by zhouwen on 16/7/23.
 */

//@InjectContentView(value = R.layout.message_fragment)
public class MessageFragment extends BaseFragment {

//    @InjectChildView(value = R.id.btn_send_mes, listener = View.OnClickListener.class)
//    private Button mBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        view.findViewById(R.id.btn_send_mes).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_mes:
                //1.未带数据
                //2.带bundle数据，这里可以带任意类型的数据
                break;
        }
    }
}
