const pageNum = document.getElementById('page-num');
const divThreadList = document.getElementsByClassName('thread-list');
const searchButton = document.getElementById('search_button');
const prevButton = document.getElementsByClassName('prev-btn')[0];
const nextButton = document.getElementsByClassName('next-btn')[0];
const tagRadio = document.getElementById('select-tag');
const titleRadio = document.getElementById('select-title');



let page = 1;
let maxPageThread = 10;

let data = {
	searchType: 'tag',
	searchText: '',
	sortType: 'new',
	page: '1',
	maxThread: '10'


}



loadPage();

function selectSort() {
	const selectElement = document.getElementById('sort-select');
	const selectedValue = selectElement.value;

	switch (selectedValue) {
		case 'new':
			console.log("新着順が選ばれました");
			data.sortType = 'new'
			break;
		case 'activity':
			console.log("勢い順が選ばれました");
			data.sortType = 'activity'
			break;
		case 'popularity':
			console.log("人気順が選ばれました");
			data.sortType = 'popularity'
			break;
		default:
			console.log("不明な選択肢です");
	}
	getThreadList();
}


async function getThreadList() {

	try {
		const response = await fetch('/site/ThreadListServlet', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		});

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`);
		}

		const result = await response.json();




		updateThreadList(result)
	} catch (error) {
		console.error('データの送信に失敗しました:', error);
		throw error;
	}
}


function loadPage() {
	getThreadList()
	console.log(pageNum)
}



function getAllthread() {

}
function formatDate(timestamp) {
	const date = new Date(timestamp);
	return date.toLocaleString();
}

function updateThreadList(threadList) {
	// デバッグ用のログ
	console.log('Updating thread list with:', threadList);

	// thread-listという要素が存在することを確認
	const divThreadList = document.getElementById('thread-list');
	if (!divThreadList) {
		console.error('Thread list container not found');
		return;
	}

	// 既存のコンテンツをクリア
	divThreadList.innerHTML = '';

	// データの存在確認（配列として直接受け取る）
	if (!Array.isArray(threadList)) {
		console.error('Invalid thread list data');
		return;
	}

	// 各スレッドのHTML要素を作成
	threadList.forEach(content => {
		// リンク要素の作成
		console.log("content :" + content)
		const aLink = document.createElement('a');
		aLink.href = `/site/ThreadServlet?threadId=${content.id}`;
		aLink.className = 'thread-link';

		// スレッドアイテムのコンテナ
		const divThreadItem = document.createElement('div');
		divThreadItem.className = 'thread-item';

		// タグの作成
		const pThreadTag = document.createElement('p');
		pThreadTag.className = 'thread-tag';
		if (Array.isArray(content.tags) && content.tags.length > 0) {
			pThreadTag.textContent = content.tags.join(' ');
		}

		// タイトルの作成
		const pThreadTitle = document.createElement('p');
		pThreadTitle.className = 'thread-title';
		pThreadTitle.textContent = content.title || 'No Title';

		// 日付の作成
		const pThreadDate = document.createElement('p');
		pThreadDate.className = 'thread-date';
		pThreadDate.textContent = formatDate(content.createTime);

		// 要素を組み立て
		divThreadItem.appendChild(pThreadTag);
		divThreadItem.appendChild(pThreadTitle);
		divThreadItem.appendChild(pThreadDate);
		aLink.appendChild(divThreadItem);
		divThreadList.appendChild(aLink);
	});

	// 更新後の確認
	console.log('Thread list updated, new content:', divThreadList.innerHTML);
}

//検索ボックスでエンターが押されたとき
function enterKeyPress(event) {
	if (event.key === 'Enter') {
		const radioSelectTag = document.getElementById('select-tag');
		if (radioSelectTag.checked) {
			data.searchType = 'tag'
		} else {
			data.searchType = 'title'
		}

		let inputValue = document.getElementById('textbox').value;
		let words = inputValue.split(/[\s　]+/);
		data.searchText = words

		selectSort()
	}
}


//検索押したとき
searchButton.addEventListener('click', async function() {
	try {
		data.searchText = document.getElementById('search-box').value;
		const response = await fetch('/site/ThreadListServlet', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		});

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`);
		}

		const result = await response.json();
		updateThreadList(result);
	} catch (error) {
		console.error('データの送信に失敗しました:', error);
		throw error;
	}
});

//ページ切り替え
prevButton.addEventListener('click', async function() {
	console.log('ページもどる')
	if (pageNum > 1) {
		pageNum.innerText = parseInt(pageNum.innerText) - 1;
		data.page -= 1;
		try {
			const response = await fetch('/site/ThreadListServlet', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(data)
			});

			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}

			const result = await response.json();
			updateThreadList(result);
		} catch (error) {
			console.error('データの送信に失敗しました:', error);
			throw error;
		}
	}



});


nextButton.addEventListener('click', async function() {
	console.log('ページ進む')
	try {
		data.page += 1;
		const response = await fetch('/site/ThreadListServlet', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		});

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`);
		}

		const result = await response.json();
		console.log("result : " + result)
		if (result.length !== 0) {
			console.log('中身ある')
			updateThreadList(result);
			pageNum.innerText = parseInt(pageNum.innerText) + 1;
		} else {
			data.page = parseInt(pageNum.innerText);
		}

	} catch (error) {
		console.error('データの送信に失敗しました:', error);
		throw error;
	}


});




tagRadio.addEventListener('change', () => {
        if (tagRadio.checked) {
            data.searchType = 'tag';
            console.log('Changed to tag search:', data);
        }
    });

    titleRadio.addEventListener('change', () => {
        if (titleRadio.checked) {
            data.searchType = 'title';
            console.log('Changed to title search:', data);
        }
    });

