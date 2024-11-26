<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/Profile.css" type="text/css">
    <title>ユーザープロフィール</title>
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
    </script>
</body>

</html>
