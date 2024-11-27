//Thread.java

package model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * WebSocketエンドポイントを定義するクラス
 * パスパラメータとして'room'を受け取り、各ルームごとのチャット機能を提供する
 */
@ServerEndpoint("/Thread/{room}")
public class Thread {
	/**
	 * ルームごとのセッション管理用マップ
	 * Key: ルームID
	 * Value: そのルームに参加しているセッションのSet
	 * ConcurrentHashMapを使用して複数のスレッドからの同時アクセスに対応
	 */
	private static final Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

	/**
	 * セッションとユーザー情報を紐付けるマップ
	 * Key: WebSocketセッション
	 * Value: ユーザー情報オブジェクト
	 */
	private static final Map<Session, UserInfo> sessionUserMap = new ConcurrentHashMap<>();

	/**
	 * ユーザー情報を保持する内部クラス
	 * ユーザーIDと表示名を管理
	 */
	private static class UserInfo {
		String userId; // ユーザーの一意識別子
		String displayName; // 表示用の名前

		UserInfo(String userId, String displayName) {
			this.userId = userId;
			this.displayName = displayName;
		}
	}

	/**
	 * WebSocket接続が確立された時の処理
	 * @param session 確立されたWebSocketセッション
	 * @param room ルームID
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("room") String room) {
		try {
			// クエリパラメータからユーザーIDを取得（nullの場合は新規生成）
			String userId = null;
			Map<String, List<String>> params = session.getRequestParameterMap();
			if (params != null && params.containsKey("userId")) {
				userId = params.get("userId").get(0);
			}

			// ユーザーIDがない場合は新規生成
			if (userId == null || userId.isEmpty()) {
				userId = generateUserId();
			}

			// 同じユーザーIDの既存セッションがある場合は切断
			removeExistingUserSession(userId, room);

			// このルームのセッションセットを取得（存在しない場合は新規作成）し、
			// 新しいセッションを追加
			roomSessions.computeIfAbsent(room, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
					.add(session);

			// セッションとユーザー情報を紐付け
			sessionUserMap.put(session, new UserInfo(userId, "User-" + userId));

			// 入室メッセージをブロードキャスト
//			broadcastMessage(room, "システム", sessionUserMap.get(session).displayName + "が入室しました。");

			// ログ出力
			System.out.println("新しい接続: " + session.getId() + " in room: " + room + " userId: " + userId);

		} catch (Exception e) {
			System.err.println("onOpen処理エラー: " + e.getMessage());
			try {
				session.close();
			} catch (IOException ioe) {
				System.err.println("セッション終了エラー: " + ioe.getMessage());
			}
		}
	}

	/**
	 * メッセージを受信した時の処理
	 * @param message 受信したメッセージ
	 * @param session 送信元のセッション
	 * @param room ルームID
	 */
	@OnMessage
	public void onMessage(String message, Session session, @PathParam("room") String room) {
		System.out.println("受信メッセージ: " + message + " from " + session.getId() + " in room: " + room);
		JsonLogic jsonLogic = new JsonLogic();
		ContentDto contentDto = null;
		try {
			// 受信したJSONメッセージをパース
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(message);
			

			// メッセージデータを取得
			String threadId = jsonNode.get("threadId").asText();
			String userId = jsonNode.get("userId").asText();
			String userName = null;
			String messageText = jsonNode.get("message").asText();

			// 現在のタイムスタンプを生成
			Date date = new Date();
			Timestamp contenttime = new Timestamp(date.getTime());
			contentDto = new ContentDto(jsonLogic.getContentId(threadId)+1,userId,userName,contenttime,messageText);

			
			
			// メッセージを永続化（JSONファイルに保存）
			jsonLogic.addContent(threadId, contentDto);

		} catch (Exception e) {
			System.err.println("メッセージ処理エラー: " + e.getMessage());
		}
		contentDto.setUserId(null);
		
		
		// メッセージをルーム内の全員にブロードキャスト
		broadcastMessage(room, sessionUserMap.get(session).displayName,jsonLogic.changeContentDtoToObjectNode(contentDto).toString() );
	}

	/**
	 * WebSocket接続が閉じられた時の処理
	 * @param session 閉じられたセッション
	 * @param room ルームID
	 */
	@OnClose
	public void onClose(Session session, @PathParam("room") String room) {
		removeSession(session, room);
		System.out.println("接続終了: " + session.getId() + " from room: " + room);
	}

	/**
	 * エラーが発生した時の処理
	 * @param session エラーが発生したセッション
	 * @param throwable 発生した例外
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("エラー発生: " + session.getId() + " - " + throwable.getMessage());
	}

	/**
	 * 指定されたルームの全セッションにメッセージをブロードキャスト
	 * @param room メッセージを送信するルーム
	 * @param sender 送信者の名前
	 * @param message 送信するメッセージ
	 */
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

	/**
	 * 既存のユーザーセッションを削除
	 * @param userId 削除対象のユーザーID
	 * @param room 対象のルーム
	 */
	private void removeExistingUserSession(String userId, String room) {
		Set<Session> sessions = roomSessions.get(room);
		if (sessions != null) {
			sessions.stream()
					.filter(s -> {
						UserInfo info = sessionUserMap.get(s);
						return info != null && info.userId.equals(userId);
					})
					.forEach(s -> removeSession(s, room));
		}
	}

	/**
	 * セッションの削除処理
	 * @param session 削除するセッション
	 * @param room 対象のルーム
	 */
	private void removeSession(Session session, String room) {
		Set<Session> sessions = roomSessions.get(room);
		if (sessions != null) {
			sessions.remove(session);
			UserInfo userInfo = sessionUserMap.remove(session);
			if (userInfo != null) {
				broadcastMessage(room, "システム", userInfo.displayName + "が退室しました。");
			}

			// ルームが空になった場合、ルームを削除
			if (sessions.isEmpty()) {
				roomSessions.remove(room);
			}
		}
	}

	/**
	 * 新しいユーザーIDを生成
	 * @return 生成されたユーザーID
	 */
	private String generateUserId() {
		return "user_" + UUID.randomUUID().toString().substring(0, 8);
	}
}