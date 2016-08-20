package com.kit.cn.smartkit.widget_sample;


import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.base.BaseFragment;

/**
 * Created by zhouwen on 16/7/23.
 */

@InjectContentView(value = R.layout.ioc_simple_activity_main)
public class ToastFragment extends BaseFragment {

//    @InjectChildView(value = R.id.btn, listener = View.OnClickListener.class)
//    private Button mBtn;


//    @Override
//    public void onClick(View v) {
//        Toast.makeText(getActivity(), "return activity!", Toast.LENGTH_LONG).show();
//        getActivity().onBackPressed();
//    }
}
