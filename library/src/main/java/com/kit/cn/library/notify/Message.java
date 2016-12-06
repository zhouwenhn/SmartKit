package com.kit.cn.library.notify;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhouwen on 2016/11/13.
 * Description: 用于定义消息类型及创建消息，重置消息状态等
 */
public class Message {

	public final static int PRIORITY_NORMAL = 1;
	public final static int PRIORITY_HIGH = 2;
	public final static int PRIORITY_EXTREMELY_HIGH = 3;

	private static ConcurrentLinkedQueue<Message> mCachedMessagePool = new ConcurrentLinkedQueue<Message>();
	private final static int MAX_CACHED_MESSAGE_OBJ = 15;

	public Type type;
	public Object data;
	public int priority;
	public Object sender;
	public int referenceCount;

	public enum Type {
		NONE,

		DESTROY_MESSAGE_PUMP,

		NETWORK_STATE_CHANGED, // 网络状态变化

		MSG_EXAMPLE_TEST,

		SIMPLE,

		//聊天模块
		MSG_LIVE_FUCTION_AT_ONE, //@某个人

		MSG_LIVE_FUCTION_PTIVATGE_ONE, //私聊某人

		MSG_LIVE_USER_CARD, //资料卡

		MSG_OPEN_RECHARGE, //充值

		MSG_FOLLOW, //关注成功（直播间内的资料卡点击关注按钮）

		MSG_UNFOLLOW, //取消关注成功

		MSG_KICK_USER,//踢人

		MSG_GAG_USER, //禁言

		MSG_RECHARGE_SUCCESS //充值成功
	}

	public Message(Type type, Object data, int priority, Object sender) {
		this.type = type;
		this.data = data;
		this.priority = priority;
		this.sender = sender;
	}

	public Message(Type type, Object data, int priority) {
		this(type, data, priority, null);
	}

	public Message(Type type, Object data) {
		this(type, data, PRIORITY_NORMAL, null);
	}

	public Message(Type type, int priority) {
		this(type, null, priority);
	}

	public void reset() {
		type = Type.NONE;
		data = null;
		priority = PRIORITY_NORMAL;
		sender = null;
	}

	public void recycle() {
		if (mCachedMessagePool.size() < MAX_CACHED_MESSAGE_OBJ) {
			reset();
			mCachedMessagePool.add(this);
		}
	}

	public static Message obtainMessage(Type messageType, Object data,
			int priority, Object sender) {
		Message message = mCachedMessagePool.poll();

		if (message != null) {
			message.type = messageType;
			message.data = data;
			message.priority = priority;
			message.sender = sender;

		} else {
			message = new Message(messageType, data, priority, sender);
		}

		return message;
	}

}
