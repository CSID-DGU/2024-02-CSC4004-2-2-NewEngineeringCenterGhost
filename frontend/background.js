chrome.runtime.onInstalled.addListener(() => {
  // "측정하기" 메뉴: 모든 페이지와 이미지에서 항상 표시
  chrome.contextMenus.create({
    id: "precisionMeasure",
    title: "측정하기",
    contexts: ["page", "selection", "image"]
  });

  // "측정 정보 추가" 메뉴: 텍스트 선택 및 이미지 우클릭 시 표시
  chrome.contextMenus.create({
    id: "customMeasure",
    title: "측정 정보 추가",
    contexts: ["selection", "image"] // "image" 컨텍스트 추가
  });
});

// 컨텍스트 메뉴 클릭 이벤트 처리
chrome.contextMenus.onClicked.addListener((info, tab) => {
  if (info.menuItemId === "precisionMeasure") {
    // "측정하기" 선택 시 로직 실행
    chrome.scripting.executeScript({
      target: { tabId: tab.id },
      function: startPrecisionMeasurement,
      args: [info]
    });
  } else if (info.menuItemId === "customMeasure") {
    // "측정 정보 추가" 선택 시 데이터 저장
    chrome.storage.local.get({ customData: [] }, (result) => {
      const customData = result.customData;
      const newData = info.selectionText || info.srcUrl; // 선택된 텍스트 또는 이미지 URL 저장

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
  const data = info.selectionText || info.srcUrl || document.URL;
  alert(`분석을 시작합니다: ${data}`);

  // 임의의 확률 생성 및 해설
  const probability = Math.floor(Math.random() * 100);
  const explanation = probability >= 50
      ? "이 데이터는 낚시성 정보를 포함할 가능성이 높습니다."
      : "이 데이터는 신뢰할 가능성이 높습니다.";

  const llmGeneratedExplanation = probability >= 50
      ? "이 정보는 특정 키워드가 과도하게 반복되어 낚시성으로 판단될 수 있습니다."
      : "이 정보는 문맥상 자연스럽고 신뢰할 수 있는 표현을 포함하고 있습니다.";

  const banner = document.createElement("div");
  banner.style.position = "fixed";
  banner.style.top = "0";
  banner.style.width = "100%";
  banner.style.padding = "10px";
  banner.style.zIndex = "9999";
  banner.style.color = "white";
  banner.style.backgroundColor = probability >= 50 ? "rgba(255, 0, 0, 0.8)" : "rgba(0, 128, 0, 0.8)";
  banner.innerText = `낚시성 정보 확률: ${probability}%\n${explanation}\n\nLLM 해설: ${llmGeneratedExplanation}`;

  document.body.appendChild(banner);

  // 신뢰도가 낮은 경우 텍스트 또는 이미지 강조
  if (probability >= 50) {
      if (info.selectionText) {
          // 선택된 텍스트 강조 처리
          const selection = info.selectionText;
          const regex = new RegExp(selection, "g");
          const originalHTML = document.body.innerHTML;

          document.body.innerHTML = document.body.innerHTML.replace(
              regex,
              `<span style="background-color: rgba(255, 0, 0, 0.4); font-weight: bold;">${selection}</span>`
          );

          // 3초 후 강조 제거
          setTimeout(() => {
              document.body.innerHTML = originalHTML;
          }, 3000);
      } else if (info.mediaType === "image" && info.srcUrl) {
          // 이미지 강조 처리
          const images = document.querySelectorAll(`img[src="${info.srcUrl}"]`);
          images.forEach(img => {
              // 빨간색 테두리 추가
              const originalBorder = img.style.border;
              const originalOpacity = img.style.opacity;

              img.style.border = "5px solid red";
              img.style.opacity = "0.5";

              // 경고 아이콘 추가
              const warningIcon = document.createElement("div");
              warningIcon.innerText = "⚠️";
              warningIcon.style.position = "absolute";
              warningIcon.style.top = "5px";
              warningIcon.style.right = "5px";
              warningIcon.style.fontSize = "24px";
              warningIcon.style.color = "red";
              warningIcon.style.backgroundColor = "white";
              warningIcon.style.borderRadius = "50%";
              warningIcon.style.padding = "2px";
              warningIcon.style.zIndex = "10000";
              warningIcon.title = "이 이미지의 신뢰도가 낮습니다.";

              // 이미지의 부모 요소에 경고 아이콘 추가
              img.parentElement.style.position = "relative";
              img.parentElement.appendChild(warningIcon);

              // 3초 후 강조 제거
              setTimeout(() => {
                  img.style.border = originalBorder || "";
                  img.style.opacity = originalOpacity || "1";
                  if (warningIcon.parentElement) {
                      warningIcon.parentElement.removeChild(warningIcon);
                  }
              }, 3000);
          });
      }
  }

  setTimeout(() => banner.remove(), 5000); // 5초 후 배너 제거
}
