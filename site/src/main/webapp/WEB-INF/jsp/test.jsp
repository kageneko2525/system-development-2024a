<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.net.URLEncoder" %>
<% 
    // XSS対策のためのエスケープ処理
    int id = (Integer) request.getAttribute("id");
    String escapedId = String.valueOf(id);
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="リアルタイムWebSocketチャットアプリケーション">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="./css/test.css" type="text/css">
    <title>多対多チャット</title>
</head>
<body>
    <div class="chat-container">
        <header class="chat-header">
            <h1>WebSocket チャット</h1>
            <div id="connectStatus" class="connection-status">接続状態: 接続中...</div>
        </header>

        <main class="chat-main">
            <!-- チャットメッセージ表示エリア -->
            <div id="messages" class="messages-container" role="log" aria-live="polite"></div>

            <!-- 入力フォーム -->
            <div class="input-form">
                <input type="hidden" name="id" id="userId" value="<%= escapedId %>" data-room-id="<%= escapedId %>">
                <div class="message-input-container">
                    <input 
                        type="text" 
                        id="messageInput" 
                        class="message-input"
                        placeholder="メッセージを入力" 
                        aria-label="メッセージを入力"
                        maxlength="1000"
                    >
                    <button 
                        type="button" 
                        onclick="sendMessage()" 
                        class="send-button"
                        aria-label="メッセージを送信"
                    >
                        送信
                    </button>
                </div>
                <div id="charCount" class="char-count">0 / 1000文字</div>
            </div>
        </main>

        <!-- 接続エラー時の通知 -->
        <div id="errorNotification" class="error-notification" hidden>
            <p>接続が切断されました。再接続を試みています...</p>
        </div>
    </div>



    <script src="./js/test.js"></script>
</body>
</html>