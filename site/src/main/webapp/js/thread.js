
let threadId = document.querySelector('input[name="id"]').value;

// JSONファイルをfetchで取得
fetch(`${threadId}.json`)
	.then(response => response.json())  // レスポンスをJSONに変換
	.then(jsonData => {
		// タイムスタンプを日付形式に変換する関数
		function formatDate(timestamp) {
			const date = new Date(timestamp);
			return date.toLocaleString();
		}

		// HTML挿入先のdivを取得
		const outputDiv = document.getElementById('messages');

		// タイトルと作成時間を挿入
		const titleElement = document.createElement('h1');
		titleElement.className = 'title';
		titleElement.textContent = jsonData.name;
		outputDiv.appendChild(titleElement);

		const createTimeElement = document.createElement('h2');
		createTimeElement.className = 'title';
//		createTimeElement.textContent = formatDate(jsonData.createTime);
		createTimeElement.textContent = jsonData.createTime;

		outputDiv.appendChild(createTimeElement);

		// コンテンツを挿入
		jsonData.contents.forEach(content => {
			const contentIdElement = document.createElement('p');
			contentIdElement.className = 'contents';
			contentIdElement.textContent = `ID: ${content.id}`;
			outputDiv.appendChild(contentIdElement);

			const userNameElement = document.createElement('p');
			userNameElement.className = 'contents';
			userNameElement.textContent = `ユーザー名: ${content.userName}`;
			outputDiv.appendChild(userNameElement);

			const postingTimeElement = document.createElement('p');
			postingTimeElement.className = 'contents';
			postingTimeElement.textContent = `投稿時間: ${content.postingTime}`;
			outputDiv.appendChild(postingTimeElement);

			const contentTextElement = document.createElement('p');
			contentTextElement.className = 'contents';
			contentTextElement.textContent = `内容: ${content.content}`;

			// タグに従ってスタイルを追加
			content.tags.forEach(tag => {
				let cssProperty = tag.tag;
				if (cssProperty === 'font-color') {
					cssProperty = 'color'; // font-color を color に変換
				}
				contentTextElement.style[cssProperty] = tag.value;
			});

			outputDiv.appendChild(contentTextElement);
		});
	})
	.catch(error => {
		console.error('Error fetching JSON data:', error);
	});
