package com.dream.mis.core.web.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.dream.mis.core.common.GetHttpSessionConfigurator;
import com.dream.mis.core.web.model.User;
import com.jfinal.plugin.activerecord.Db;

@ServerEndpoint(value="/websocket",configurator=GetHttpSessionConfigurator.class)
public class WebSocketController {
	 //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	 private static int onlineCount = 0;
	 
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	//private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();
	private static Map<String,Object> wesocketmap=new ConcurrentHashMap<String,Object>();
	
	/**
	 * 连接建立成功调用的方法
	* @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	*/
	@OnOpen
	public void onOpen(Session session,EndpointConfig config) throws IOException {
		HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user=(User)httpSession.getAttribute("currentUser");
        String user_id="";
        if(user!=null) {
        	user_id=user.getStr("user_id");
        }
		String sessionId=session.getId();
        Map<String, Object> map=new HashMap<String,Object>();
		map.put("user_id", user_id);
		map.put("session", session);
		wesocketmap.put(sessionId, map);
		sendMessage(user_id);
	}
	
	/**
	 * 连接关闭调用的方法
	*/
	@OnClose
	public void onClose(Session session) {
		String sessionId=session.getId();
		wesocketmap.remove(sessionId);
	}

	/**
	 * 收到客户端消息后调用的方法
	* @param message 客户端发送过来的消息
	* @param session 可选的参数
	*/
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("来自客户端的消息:" + message);
		session.getBasicRemote().sendText(message);
	}
	
	public static void sendMessage(String userId){
		System.out.println("userId:"+userId);
		for(String key:wesocketmap.keySet()) {
			Map<String, Object> m=(Map<String, Object>) wesocketmap.get(key);
			String user_id=(String) m.get("user_id");
			System.out.println("user_id:"+user_id);
			if(userId.equals(user_id)) {
				String sql="select (a.count+b.count) count from " + 
						"(select count(*) count from v_warn where is_read=0 and user_id=?) a, " + 
						"(select count(*) count from v_notice where is_read=0 and user_id=?) b";
				long noReadCount=Db.queryLong(sql,userId,userId);
				Session session=(Session)m.get("session");
				session.getAsyncRemote().sendObject(noReadCount);
			}
		}
		//this.session.getBasicRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketController.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketController.onlineCount--;
	}

}
