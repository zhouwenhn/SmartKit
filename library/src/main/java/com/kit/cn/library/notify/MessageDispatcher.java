package com.kit.cn.library.notify;

import com.kit.cn.library.task.TaskExecutor;
import com.kit.cn.library.utils.log.L;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by zhouwen on 2016/11/19 11:24.
 * Description: 消息转发
 */
public class MessageDispatcher extends Thread {
    private static MessageDispatcher sInstance;

    private PriorityBlockingQueue<Message> mMessageDispatcher;
    private List<List<WeakReference<IMessageAble>>> mMessageAndObserverList;

    public static MessageDispatcher getInstance() {
        if (sInstance == null) {
            synchronized (MessageDispatcher.class) {
                if (sInstance == null) {
                    sInstance = new MessageDispatcher();
                }
            }
        }

        return sInstance;
    }

    private MessageDispatcher() {
        setName("MessageDispatcher");
        mMessageDispatcher = new PriorityBlockingQueue<Message>(100, new Comparator<Message>() {
            public int compare(Message o1, Message o2) {
                return o1.priority < o2.priority ? -1 : o1.priority > o2.priority ? 1 : 1;
            }
        });
        mMessageAndObserverList = new ArrayList<List<WeakReference<IMessageAble>>>(
            Collections.<List<WeakReference<IMessageAble>>>nCopies(Message.Type.values().length, null));
        // start the background thread to process messages.
        start();

        if (L.DEBUG) L.d("started MessageDispatcher");
    }

    public void destroyMessagePump() {
        if (L.DEBUG) L.d("start destroying MessageDispatcher");

        // this message is used to destroy the message center,
        // we use the "Poison Pill Shutdown" approach, see: http://stackoverflow.com/a/812362/668963
        broadcastMessage(Message.Type.DESTROY_MESSAGE_PUMP, null, Message.PRIORITY_EXTREMELY_HIGH);
        sInstance = null;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(MIN_PRIORITY);
        dispatchMessages();

        if (L.DEBUG) L.d("destroyed MessageDispatcher");
    }

    public synchronized void register(Message.Type messageType, IMessageAble callback) {
        List<WeakReference<IMessageAble>> observerList = mMessageAndObserverList.get(messageType.ordinal());

        if (observerList == null) {
            observerList = new ArrayList<WeakReference<IMessageAble>>();
            mMessageAndObserverList.set(messageType.ordinal(), observerList);
        }

        if (indexOf(callback, observerList) == -1)
            observerList.add(new WeakReference<IMessageAble>(callback));
    }

    private int indexOf(IMessageAble callback, List<WeakReference<IMessageAble>> observerList) {
        try {
            for (int i = 0; i < observerList.size(); ++i) {
                if (observerList.get(i).get() == callback)
                    return i;
            }

        } catch (Exception e) {
            // ignore the exception
            // the observerList may be modified from within dispatchMessages() method,
            // we should catch all exceptions in case observerList is not in a right
            // state in terms item count.
        }

        return -1;
    }

    public synchronized void unregister(Message.Type messageType, IMessageAble callback) {
        List<WeakReference<IMessageAble>> observerList = mMessageAndObserverList.get(messageType.ordinal());

        if (observerList != null) {
            int index = indexOf(callback, observerList);

            if (index != -1) {
                observerList.remove(index);
            }
        }
    }


    public synchronized void unregister(IMessageAble callback) {
        Message.Type[] types = Message.Type.values();

        for (int i = 0; i < types.length; ++i) {
            unregister(types[i], callback);
        }
    }

    public void broadcastMessage(Message.Type messageType, Object data) {
        mMessageDispatcher.put(Message.obtainMessage(messageType, data, Message.PRIORITY_NORMAL, null));
    }

    public void broadcastMessage(Message.Type messageType, Object data, int priority) {
        mMessageDispatcher.put(Message.obtainMessage(messageType, data, priority, null));
    }

    public void broadcastMessage(Message.Type messageType, Object data, int priority, Object sender) {
        mMessageDispatcher.put(Message.obtainMessage(messageType, data, priority, sender));
    }

    private void dispatchMessages() {
        while (true) {
            try {
                final Message message = mMessageDispatcher.take();

                if (message.type == Message.Type.DESTROY_MESSAGE_PUMP)
                    break;

                final List<WeakReference<IMessageAble>> observerList = mMessageAndObserverList.get(message.type.ordinal());

                if (observerList != null && observerList.size() > 0) {
                    message.referenceCount = observerList.size();

                    for (int i = 0; i < observerList.size(); ++i) {
                        final IMessageAble callback = observerList.get(i).get();

                        if (callback == null) {
                            observerList.remove(i);
                            --i;

                            if (--message.referenceCount == 0) {
                                message.recycle();
                            }

                        } else {
                            TaskExecutor.executeRunOnUIExecutorTask(new Runnable() {
                                @Override
                                public void run() {
                                    if (L.DEBUG) {
                                        callback.onMessage(message);

                                    } else {
                                        try {
                                            // call the target on the UI thread
                                            callback.onMessage(message);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // recycle the Message object
                                    if (--message.referenceCount == 0) {
                                        message.recycle();
                                    }
                                }
                            });
                        }
                    }

                } else {
                    message.recycle();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}