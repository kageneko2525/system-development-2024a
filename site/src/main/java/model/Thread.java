package model;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
@ServerEndpoint("/Thread/{room}")
public class Thread{
    // 全クライアントのセッションを保持するセット（スレッドセーフな実装）
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());
    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("新しい接続: " + session.getId());
    }
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("受信メッセージ: " + message + " from " + session.getId());
        synchronized (clients) {
            for (Session client : clients) {
                if (client.isOpen() && !client.getId().equals(session.getId())) {
                    client.getBasicRemote().sendText("クライアント " + session.getId() + ": " + message);
                }
            }
        }
    }
    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("接続終了: " + session.getId());
    }
}