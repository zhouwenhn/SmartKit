package com.kit.cn.smartkit.message_sample;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kit.cn.library.ioc.annotations.field.InjectChildView;
import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.library.message.MessagePump;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.base.BaseFragment;

import static com.kit.cn.library.message.Message.Type.MSG_EXAMPLE_TEST;

/**
 * Created by zhouwen on 16/7/23.
 */

@InjectContentView(value = R.layout.message_fragment)
public class MessageFragment extends BaseFragment {

    @InjectChildView(value = R.id.btn_send_mes, listener = View.OnClickListener.class)
    private Button mBtn;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_mes:
                //1.未带数据
                MessagePump.getInstance().broadcastMessage(MSG_EXAMPLE_TEST, null);
                //2.带bundle数据，这里可以带任意类型的数据
                Bundle bundle = new Bundle();
                bundle.putString("key", "value");
                MessagePump.getInstance().broadcastMessage(MSG_EXAMPLE_TEST, bundle);
                break;
        }
    }
}
