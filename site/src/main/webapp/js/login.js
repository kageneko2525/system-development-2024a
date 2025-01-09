// エラーメッセージがあればアラートで表示
function getParameterByName(name) {
    const url = new URL(window.location.href);
    const param = url.searchParams.get(name);
    console.log("Param: ", param); // デバッグメッセージ
    return param ? decodeURIComponent(param.replace(/\+/g, ' ')) : null;
}

const errorMessage = getParameterByName('error');
console.log("Error Message: ", errorMessage); // デバッグメッセージ
if (errorMessage) {
    alert(errorMessage);
}
