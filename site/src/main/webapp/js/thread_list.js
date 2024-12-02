document.getElementById("sort-select").addEventListener("change", function() {
    const sortOption = this.value;
    const threadList = document.getElementById("thread-list");
    const threads = Array.from(threadList.getElementsByClassName("thread-item"));
  
    threads.sort((a, b) => {
      if (sortOption === "new") {
        return new Date(b.getAttribute("data-date")) - new Date(a.getAttribute("data-date"));
      } else if (sortOption === "activity") {
        return b.getAttribute("data-activity") - a.getAttribute("data-activity");
      }
      return 0; // デフォルトは変更なし
    });
  
    threads.forEach(thread => threadList.appendChild(thread)); // 並べ替え後に再表示
  });
  