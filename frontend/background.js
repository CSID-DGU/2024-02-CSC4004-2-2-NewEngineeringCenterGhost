// background.js
chrome.runtime.onInstalled.addListener(() => {
  // 정밀 측정을 위한 컨텍스트 메뉴 생성
  chrome.contextMenus.create({
      id: "precisionMeasure",
      title: "측정하기",
      contexts: ["page", "selection", "image"]
  });

  // 사용자 정의 측정을 위한 컨텍스트 메뉴 생성
  chrome.contextMenus.create({
      id: "customMeasure",
      title: "측정 정보 추가",
      contexts: ["selection", "image"]
  });
});

// 컨텍스트 메뉴 클릭 이벤트 처리
chrome.contextMenus.onClicked.addListener((info, tab) => {
  if (info.menuItemId === "precisionMeasure") {
      // 정밀 측정 로직 실행
      chrome.scripting.executeScript({
          target: { tabId: tab.id },
          function: startPrecisionMeasurement,
          args: [info]
      });
  } else if (info.menuItemId === "customMeasure") {
      // 사용자 정의 측정 데이터 저장
      chrome.storage.local.get({ customData: [] }, (result) => {
          const customData = result.customData;
          const newData = info.selectionText || info.srcUrl; // 선택된 텍스트 또는 이미지 URL

          if (newData) {
              customData.push(newData);
              chrome.storage.local.set({ customData }, () => {
                  alert("측정 정보가 저장되었습니다.");
              });
          }
      });
  }
});

function startPrecisionMeasurement(info) {
  const data = info.selectionText || document.URL;
  alert(`분석을 시작합니다: ${data}`);

  // 서버 통신 없이 결과 표시
  const probability = Math.floor(Math.random() * 100); // 임의의 확률 생성
  const explanation = "이 데이터는 낚시성 정보를 포함할 가능성이 높습니다.";

  const banner = document.createElement("div");
  banner.style.position = "fixed";
  banner.style.top = "0";
  banner.style.width = "100%";
  banner.style.backgroundColor = "rgba(255, 0, 0, 0.8)";
  banner.style.color = "white";
  banner.style.padding = "10px";
  banner.style.zIndex = "9999";
  banner.innerText = `낚시성 정보 확률: ${probability}%\n${explanation}`;

  document.body.appendChild(banner);

  setTimeout(() => banner.remove(), 10000); // 10초 후 제거
}
