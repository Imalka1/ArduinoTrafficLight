package socket_controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/light-end-point")
public class ServerEndPoint {

    //    private static Set<Session> userSessions = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());
    private static Set<Session> userSessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session userSession) {
        userSessions.add(userSession);
    }

    @OnClose
    public void onClose(Session userSession) {
        userSessions.remove(userSession);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
//        Set<Session> userSessions = ServerEndPoint.getUserSessions();
//        for (Session session : userSessions) {
//            session.getAsyncRemote().sendText(message);
//        }
//        System.out.println(message);
    }

    public static Set<Session> getUserSessions() {
        return userSessions;
    }
}