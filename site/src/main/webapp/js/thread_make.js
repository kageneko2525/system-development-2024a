document.addEventListener('DOMContentLoaded', function() {
    const addCustomTagButton = document.getElementById('addCustomTag');
    const customTagInput = document.getElementById('customTag');
    const selectedTagsContainer = document.getElementById('selectedTags');
    const createThreadButton = document.getElementById('createThread');
    const agreeTermsCheckbox = document.getElementById('agreeTerms');
    const allTagsInput = document.getElementById('allTags'); // 隠しフィールド

    let selectedTags = [];

    // エンターキーでフォームが送信されるのを防ぐ
    customTagInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            addCustomTagButton.click();
        }
    });

    // カスタムタグを追加する
    addCustomTagButton.addEventListener('click', function() {
        const customTags = customTagInput.value.trim();
        
        if (customTags) {
            const tagsArray = customTags.split(',').map(tag => tag.trim());
            tagsArray.forEach(tag => {
                if (tag && !selectedTags.includes(tag)) {
                    selectedTags.push(tag);
                }
            });
            updateTagDisplay();
            customTagInput.value = '';
        }
    });

    // 標準タグのチェックボックスの変更を監視
    document.querySelectorAll('input[name="standardTags"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            const tag = this.value;
            if (this.checked) {
                if (!selectedTags.includes(tag)) {
                    selectedTags.push(tag);
                }
            } else {
                selectedTags = selectedTags.filter(t => t !== tag);
            }
            updateTagDisplay();
        });
    });

    // タグ欄の更新
    function updateTagDisplay() {
        // タグ表示部分のクリア
        selectedTagsContainer.innerHTML = '';
        
        // 選択したタグを表示
        selectedTags.forEach(function(tag) {
            const tagElement = document.createElement('span');
            tagElement.classList.add('tag');
            tagElement.textContent = tag;

            // 削除ボタンを作成
            const removeButton = document.createElement('button');
            removeButton.textContent = '×';
            removeButton.addEventListener('click', function() {
                selectedTags = selectedTags.filter(t => t !== tag);

                // チェックを外す
                document.querySelectorAll('input[name="standardTags"]').forEach(function(checkbox) {
                    if (checkbox.value === tag) {
                        checkbox.checked = false;
                    }
                });

                updateTagDisplay();
            });

            tagElement.appendChild(removeButton);
            selectedTagsContainer.appendChild(tagElement);
        });

        // 隠しフィールドに最新のタグリストをセット
        allTagsInput.value = selectedTags.join(',');
    }

    // 利用規約に同意しているかどうかをチェック
    agreeTermsCheckbox.addEventListener('change', function() {
        createThreadButton.disabled = !agreeTermsCheckbox.checked;
    });
});