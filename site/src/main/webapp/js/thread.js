
let threadId = document.querySelector('input[name="id"]').value;

// JSONファイルをfetchで取得
fetch(`./json/json${threadId}.json`)
	//fetch(`./json/json2.json`)
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
		titleElement.textContent = jsonData.title;
		outputDiv.appendChild(titleElement);

		const createTimeElement = document.createElement('h2');
		createTimeElement.className = 'time';
		//		createTimeElement.textContent = formatDate(jsonData.createTime);
		createTimeElement.textContent = jsonData.createTime;

		outputDiv.appendChild(createTimeElement);
		
		if (jsonData.contents && Array.isArray(jsonData.contents) && jsonData.contents.length > 0) {

		// コンテンツを挿入
		jsonData.contents.forEach(content => {
			const contntBoxElement = document.createElement('div')
			contntBoxElement.className = "content-box"
			contntBoxElement.id = "contentId-" + message.id;

			const contentIdElement = document.createElement('p');
			contentIdElement.className = 'id';
			contentIdElement.textContent = `ID: ${content.id}`;
			contntBoxElement.appendChild(contentIdElement);

			const userNameElement = document.createElement('p');
			userNameElement.className = 'userName';
			userNameElement.textContent = `ユーザー名: ${content.userName}`;
			contntBoxElement.appendChild(userNameElement);

			const postingTimeElement = document.createElement('p');
			postingTimeElement.className = 'time';
			postingTimeElement.textContent = `投稿時間: ${formatDate(content.postingTime)}`;
			contntBoxElement.appendChild(postingTimeElement);

			const contentTextElement = document.createElement('p');
			contentTextElement.className = 'content';
			contentTextElement.textContent = `内容: ${content.content}`;


			if (content.option != null) {

				// タグに従ってスタイルを追加
				content.tags.forEach(tag => {
					let cssProperty = tag.tag;
					if (cssProperty === 'font-color') {
						cssProperty = 'color'; // font-color を color に変換
					}
					contentTextElement.style[cssProperty] = tag.value;
				});
			}

			contntBoxElement.appendChild(contentTextElement);

			outputDiv.appendChild(contntBoxElement);

			document.documentElement.scrollTop = outputDiv.scrollHeight;
		});
		}
	})
	.catch(error => {
		console.error('Error fetching JSON data:', error);
	});
