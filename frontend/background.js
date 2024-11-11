// background.js
chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
      id: "precisionMeasure",
      title: "정밀 측정 검사",
      contexts: ["selection"] // 선택된 텍스트가 있을 때만 보임
  });
});

chrome.contextMenus.onClicked.addListener((info) => {
  if (info.menuItemId === "precisionMeasure") {
      // 선택된 텍스트를 content.js로 전달
      chrome.scripting.executeScript({
          target: { tabId: info.tabId },
          function: startMeasurement,
          args: [info.selectionText] // 선택된 텍스트 전달
      });
  }
});

function startMeasurement(selectedText) {
  // 선택된 텍스트로 측정 검사 시작 (여기에서 필요한 작업을 수행)
  alert(`측정 검사를 시작합니다: ${selectedText}`);
}