<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ユーザープロフィール</title>
    <style>
        .right {
            text-align: end;
        }
        .profile-icon {
            width: 200px;
            height: 200px;
            border-radius: 50%;
            margin: 5vw 0 0 10vw;
        }
        .button-container {
            display: flex;
            align-items: center;
        }
        h1 {
            flex-grow: 1;
            text-align: center;
            margin: 0;
        }
        .report-button {
            font-size: 16px;
            padding: 5px 10px;
            cursor: pointer;
            margin-right: 10vw;
        }
        .close-button {
            margin: 0 0 0 auto;
            font-size: 42px;
            color: #333;
            background: none;
            border: none;
            cursor: pointer;
        }
        #user-birth-container,
        #user-comment-container {
            display: none;
        }
    </style>
</head>

<body>
    <div class="profile-container">
        <div class="right">
            <button class="close-button" onclick="closePopup()">×</button>
        </div>
        <div class="button-container">
            <h1>プロフィール</h1>
            <button class="report-button">通報</button>
        </div>
        <div style="display: flex; gap: 20px;">
            <img id="profile-icon" src="https://www.chiba-fjb.ac.jp/www/img/staff/nomoto_01.jpg" alt="プロフィール画像" class="profile-icon">
            <div class="profile-info">
                <div id="user-name-container">
                    <span id="user-name-label"></span><span id="user-name"></span>
                </div>
                <div id="user-birth-container">
                    <span id="user-birth-label">生年月日: </span><span id="user-birth"></span>
                </div>
                <div id="follower-display-container" style="display: none;">
                    <p>フォロー・フォロワー情報が公開されています</p>
                </div>
                <div id="user-comment-container">
                    <span id="user-comment-label">自己紹介: </span><span id="user-comment"></span>
                </div>
            </div>
        </div>
    </div>

    <script>
        function closePopup() {
            if (confirm('このウィンドウを閉じますか？')) {
                window.close();
            }
        }

        const userId = new URLSearchParams(window.location.search).get('id');
        const apiUrl = `http://localhost:8080/site/Profile?id=${userId}`;

        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                const profileIcon = document.getElementById('profile-icon');
                profileIcon.src = data.userIcon || 'default.png';

                const userNameContainer = document.getElementById('user-name-container');
                const userName = document.getElementById('user-name');
                userName.textContent = data.public_name === 1 && data.userName ? data.userName : '非公開ユーザー';
                userNameContainer.style.display = 'block';

                const userBirthContainer = document.getElementById('user-birth-container');
                const userBirth = document.getElementById('user-birth');
                if (data.public_birth === 1 && data.userBirth && data.userBirth !== '非公開') {
                    userBirth.textContent = data.userBirth;
                    userBirthContainer.style.display = 'block';
                } else {
                    userBirthContainer.style.display = 'none';
                }

                const userCommentContainer = document.getElementById('user-comment-container');
                const userComment = document.getElementById('user-comment');
                if (data.userComment && data.userComment.trim() !== '') {
                    userComment.textContent = data.userComment;
                    userCommentContainer.style.display = 'block';
                } else {
                    userCommentContainer.style.display = 'none';
                }

                const followerDisplayContainer = document.getElementById('follower-display-container');
                followerDisplayContainer.style.display = data.public_display === 1 ? 'block' : 'none';
            })
            .catch(error => {
                console.error('エラー:', error);
                alert('ユーザー情報の取得に失敗しました');
            });
    </script>
</body>

</html>
