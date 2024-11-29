function showProbability(event) {
  const element = event.target;

  // 텍스트 또는 이미지가 없는 요소는 무시
  if (element.tagName !== "IMG" && !element.textContent.trim()) {
      return;
  }

  const probability = Math.floor(Math.random() * 100); // 0-100 사이의 확률 생성

  // 툴팁 생성
  const tooltip = document.createElement('div');
  tooltip.style.position = 'absolute';
  tooltip.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
  tooltip.style.color = 'white';
  tooltip.style.padding = '5px';
  tooltip.style.borderRadius = '5px';
  tooltip.style.fontSize = '12px';
  tooltip.style.zIndex = '10000';
  tooltip.innerText = `낚시성 확률: ${probability}%`;

  document.body.appendChild(tooltip);

  // 툴팁 위치 설정
  const rect = element.getBoundingClientRect();
  tooltip.style.left = `${rect.left + window.scrollX}px`;
  tooltip.style.top = `${rect.top + window.scrollY - tooltip.offsetHeight}px`;

  // 마우스가 벗어나면 툴팁 제거
  element.addEventListener('mouseout', () => {
      tooltip.remove();
  }, { once: true });
}

function addHoverEffect() {
  // 텍스트 및 이미지 관련 요소 선택
  const elements = document.querySelectorAll("p, span, div, h1, h2, h3, h4, h5, h6, img");

  elements.forEach(element => {
      // 이미 이벤트가 추가된 요소인지 확인
      if (element.hasAttribute("data-hover-added")) {
          return;
      }

      // 마우스 오버 이벤트 추가
      element.addEventListener("mouseover", showProbability);

      // 이벤트가 추가된 요소로 표시
      element.setAttribute("data-hover-added", "true");
  });
}

// DOM 로드 시 실행
document.addEventListener("DOMContentLoaded", addHoverEffect);

// 동적으로 추가된 콘텐츠에도 적용
const observer = new MutationObserver(() => {
  addHoverEffect(); // 새로 추가된 요소에도 이벤트 추가
});

observer.observe(document.body, {
  childList: true,
  subtree: true
});
