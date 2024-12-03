chrome.runtime.onInstalled.addListener(() => {
  // "측정하기" 메뉴: 모든 페이지와 이미지에서 항상 표시
  chrome.contextMenus.create({
    id: "Measure",
    title: "측정하기",
    contexts: ["page", "selection", "image"]
  });

  // "측정 정보 추가" 메뉴: 텍스트 선택 및 이미지 우클릭 시 표시
  chrome.contextMenus.create({
    id: "addCustomMeasure",
    title: "측정 정보 추가",
    contexts: ["selection", "image"] // "image" 컨텍스트 추가
  });
});

// 컨텍스트 메뉴 클릭 이벤트 처리
chrome.contextMenus.onClicked.addListener((info, tab) => {
  if (info.menuItemId === "Measure") {
    // "측정하기" 선택 시 로직 실행
    chrome.tabs.sendMessage(tab.id, { action: "Measure"});
  } else if (info.menuItemId === "addCustomMeasure") {
    // "측정 정보 추가" 선택 시 데이터 저장
    chrome.tabs.sendMessage(tab.id, { action: "addData", data: info });
  }
});

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === "Measure") {
    chrome.tabs.sendMessage(message.tabId, { action: "Measure" });
  }
});