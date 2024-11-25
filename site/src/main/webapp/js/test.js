document.addEventListener('DOMContentLoaded', function() {
	let threadId = document.querySelector('input[name="id"]').value;
	let messageInput = document.getElementById('messageInput');
	let messagesDiv = document.getElementById('messages');
	let socket = null;
	let isConnecting = false;
	let reconnectAttempts = 0;
	const MAX_RECONNECT_ATTEMPTS = 5;
	const RECONNECT_DELAY = 3000;

	// ユーザーIDの取得または生成
	let userId = localStorage.getItem('userId');
	if (!userId) {
		userId = 'user_' + Date.now();
		localStorage.setItem('userId', userId);
	}

	function connectWebSocket() {
		if (isConnecting) return;
		isConnecting = true;

		const contextPath = window.location.pathname.split('/')[1];
		const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
		const wsUrl = `${wsProtocol}//${window.location.host}/${contextPath}/Thread/${threadId}?userId=${userId}`;

		socket = new WebSocket(wsUrl);

		socket.onopen = function(event) {
			console.log("接続が確立されました。");
			appendMessage("システム: 接続が確立されました。");
			isConnecting = false;
			reconnectAttempts = 0;
		};

		socket.onmessage = function(event) {
			console.log("サーバーからのメッセージ: " + event.data);
			appendMessage(event.data);
		};

		socket.onclose = function(event) {
			console.log("接続が終了しました。");
			appendMessage("システム: 接続が終了しました。");
			isConnecting = false;

			if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
				reconnectAttempts++;
				console.log(`再接続を試みます (${reconnectAttempts}/${MAX_RECONNECT_ATTEMPTS})...`);
				appendMessage(`システム: 再接続を試みています... (${reconnectAttempts}/${MAX_RECONNECT_ATTEMPTS})`);
				setTimeout(connectWebSocket, RECONNECT_DELAY);
			} else {
				appendMessage("システム: サーバーに接続できません。ページを更新してください。");
			}
		};

		socket.onerror = function(error) {
			console.error("WebSocketエラー:", error);
			isConnecting = false;
		};
	}

	function appendMessage(message) {
		const messageElement = document.createElement('div');
		messageElement.className = 'message';
		messageElement.textContent = message;
		messagesDiv.appendChild(messageElement);
		messagesDiv.scrollTop = messagesDiv.scrollHeight;
	}
	function sendMessage() {
		if (socket && socket.readyState === WebSocket.OPEN) {
			let message = messageInput.value.trim();
			if (message) {
				const messageObj = {
					threadId: threadId,
					userId: userId,
					message: message,
					timestamp: new Date().toISOString()
				};
				socket.send(JSON.stringify(messageObj));
				messageInput.value = '';
			}
		} else {
			appendMessage("システム: サーバーに接続されていません。");
		}
	}
	window.sendMessage = function() {
		if (socket && socket.readyState === WebSocket.OPEN) {
			let message = messageInput.value.trim();
			if (message) {
				socket.send(message);
				messageInput.value = '';
			}
		} else {
			appendMessage("システム: サーバーに接続されていません。");
		}
	};

	messageInput.addEventListener('keypress', function(e) {
		if (e.key === 'Enter') {
			sendMessage();
		}
	});

	// 初期接続
	connectWebSocket();
});