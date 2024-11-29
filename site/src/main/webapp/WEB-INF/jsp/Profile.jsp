<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
model.Profile profile = (model.Profile) request.getAttribute("profile");
String errorMessage = (String) request.getAttribute("errorMessage");
%>
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
			<%
			// エラーメッセージがある場合
			if (errorMessage != null) {
			%>
			<p class="error"><%=errorMessage%></p>
			<%
			// プロフィールがある場合
			} else if (profile != null) {
			%>
			<button class="report-button">通報</button>
		</div>
		<div style="display: flex; gap: 20px;">
			<img id="profile-icon"
				src="https://www.chiba-fjb.ac.jp/www/img/staff/nomoto_01.jpg"
				alt="プロフィール画像" class="profile-icon">
			<div class="profile-info">
				<p>
					<b>名前:</b>
					<%=profile.getPublicName() == 0 ? profile.getUserName() : "非公開"%>
				</p>
				<p>
					<b>誕生日:</b>
					<%
					String userBirth = profile.getUserBirth(); // 例: "19010101"
					String formattedBirth = "非公開";
					if (profile.getPublicBirth() == 0 && userBirth != null && userBirth.matches("\\d{8}")) {
						// 月と日を切り出し
						formattedBirth = Integer.parseInt(userBirth.substring(4, 6)) + "月" + Integer.parseInt(userBirth.substring(6, 8))
						+ "日";
					}
					%>
					<%=formattedBirth%>
				</p>
				<p>
					<b>コメント:</b>
					<%=profile.getPublicComment() == 0 ? profile.getUserComment() : "非公開"%>
				</p>
			</div>
		</div>
		<%
		// プロフィールもエラーメッセージもない場合
		} else {
		%>
		<p>情報がありません。</p>
		<%
		}
		%>
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
