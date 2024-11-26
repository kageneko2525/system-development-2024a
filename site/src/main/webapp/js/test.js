//test.js

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

			appendContent(extractJsonFromMessage(event.data));
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


	function extractJsonFromMessage(message) {
		try {
			const jsonMatch = message.match(/{.*}/);
			if (jsonMatch) {
				return JSON.parse(jsonMatch[0]);
			}
			return null;
		} catch (error) {
			console.error('JSON解析エラー:', error);
			return null;
		}
	}


	function appendContent(message) {
		console.log("アペンド開始")

		const contntBoxElement = document.createElement('div')
		contntBoxElement.className = "content-box"

		const contentIdElement = document.createElement('p');
		contentIdElement.className = 'id';
		contentIdElement.textContent = `ID: ${message.id}`;
		contntBoxElement.appendChild(contentIdElement);

		const userNameElement = document.createElement('p');
		userNameElement.className = 'userName';
		userNameElement.textContent = `ユーザー名: ${message.userName}`;
		contntBoxElement.appendChild(userNameElement);

		const postingTimeElement = document.createElement('p');
		postingTimeElement.className = 'time';
		postingTimeElement.textContent = `投稿時間: ${message.postingTime}`;
		contntBoxElement.appendChild(postingTimeElement);

		const contentTextElement = document.createElement('p');
		contentTextElement.className = 'content';
		contentTextElement.textContent = `内容: ${message.content}`;



		if (message.option != null) {

			// タグに従ってスタイルを追加
			message.tags.forEach(tag => {
				let cssProperty = tag.tag;
				if (cssProperty === 'font-color') {
					cssProperty = 'color'; // font-color を color に変換
				}
				contentTextElement.style[cssProperty] = tag.value;
			});
		}
		contntBoxElement.appendChild(contentTextElement);

		messagesDiv.appendChild(contntBoxElement);

		messagesDiv.scrollTop = messagesDiv.scrollHeight;
	}

	// test.js内のsendMessage関数を1つに統一
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

	// グローバルスコープのsendMessage関数を削除し、上記の関数を使用
	window.sendMessage = sendMessage;


	function appendMessage(message){
		console.log(message);
	}

	messageInput.addEventListener('keypress', function(e) {
		if (e.key === 'Enter') {
			sendMessage();
		}
	});

	// 初期接続
	connectWebSocket();
});