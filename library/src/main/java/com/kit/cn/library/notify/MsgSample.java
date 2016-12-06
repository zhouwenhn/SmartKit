package com.kit.cn.library.notify;

/**
 * Created by zhouwen on 2016/11/16.
 * Description: message sample
 */
//发广播 eg--->
//MessageDispatcher.getInstance().broadcastMessage(Message.Type.SIMPLE,
//        new MessageData<String, String>("", ""));
//Message msg = new Message(Message.Type.SIMPLE, new MessageData<String, String>("", ""));
//        MessageDispatcher.getInstance().broadcastMessage(Message.Type.SIMPLE, msg);
public class MsgSample implements IMessageAble{

    public void onCreate(){
        MessageDispatcher.getInstance().register(Message.Type.SIMPLE, this);
    }

    public void onDestroy(){
        MessageDispatcher.getInstance().unregister(Message.Type.SIMPLE, this);
    }



    @Override
    public void onMessage(Message message) {
        // TODO: 2016/11/15 各自处理业务
    }
}
