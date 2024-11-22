package model;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/Thread/{room}")
public class Thread {
    // ルームごとにセッションを管理
    private static final Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();
    // セッションとユーザー情報を紐付け
    private static final Map<Session, String> sessionUserMap = new ConcurrentHashMap<>();
    
    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room) {
        // ルームのセッションセットを取得または作成
        roomSessions.computeIfAbsent(room, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                   .add(session);
        sessionUserMap.put(session, "User-" + session.getId());
        
        broadcastMessage(room, "システム", sessionUserMap.get(session) + "が入室しました。");
        System.out.println("新しい接続: " + session.getId() + " in room: " + room);
    }
    
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("room") String room) {
        System.out.println("受信メッセージ: " + message + " from " + session.getId() + " in room: " + room);
        broadcastMessage(room, sessionUserMap.get(session), message);
    }
    
    @OnClose
    public void onClose(Session session, @PathParam("room") String room) {
        removeSession(session, room);
        System.out.println("接続終了: " + session.getId() + " from room: " + room);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("エラー発生: " + session.getId() + " - " + throwable.getMessage());
    }
    
    private void broadcastMessage(String room, String sender, String message) {
        Set<Session> sessions = roomSessions.get(room);
        if (sessions != null) {
            sessions.stream()
                   .filter(Session::isOpen)
                   .forEach(session -> {
                       try {
                           session.getBasicRemote().sendText(String.format("%s: %s", sender, message));
                       } catch (IOException e) {
                           System.err.println("メッセージ送信エラー: " + e.getMessage());
                       }
                   });
        }
    }
    
    private void removeSession(Session session, String room) {
        Set<Session> sessions = roomSessions.get(room);
        if (sessions != null) {
            sessions.remove(session);
            String user = sessionUserMap.remove(session);
            broadcastMessage(room, "システム", user + "が退室しました。");
            
            // ルームが空になった場合、ルームを削除
            if (sessions.isEmpty()) {
                roomSessions.remove(room);
            }
        }
    }
}