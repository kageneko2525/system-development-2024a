document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault(); // フォーム送信を防止

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
 
});

//エラーメッセージがあればアラートで表示
window.onload = function() {
	var errorMessage = "${errorMessage}";
	if (errorMessage) {
		alert(errorMessage);
	}
};
