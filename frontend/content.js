function showProbability(event) {
  const element = event.target;

  // 텍스트가 없는 요소는 무시
  if (!element.textContent.trim()) {
      console.log("빈 요소에 툴팁 표시 방지");
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
  // 텍스트 관련 요소만 선택
  const elements = document.querySelectorAll("p, span, div, h1, h2, h3, h4, h5, h6");

  if (elements.length === 0) {
      console.log("선택된 요소가 없습니다. 페이지 구조를 확인하세요.");
      return;
  }

  console.log(`총 ${elements.length}개의 요소에 마우스 오버 이벤트 추가.`);

  elements.forEach(element => {
      // 중복 이벤트 방지를 위해 기존 이벤트 제거
      element.removeEventListener("mouseover", showProbability);

      // 마우스 오버 이벤트 추가
      element.addEventListener("mouseover", showProbability);
  });
}

// DOM 로드 시 기존 요소에 이벤트 추가
document.addEventListener("DOMContentLoaded", addHoverEffect);

// 동적으로 추가된 콘텐츠에도 이벤트 추가
const observer = new MutationObserver(() => {
  console.log("새로운 콘텐츠가 감지되었습니다. 이벤트를 추가합니다.");
  addHoverEffect();
});

observer.observe(document.body, {
  childList: true,
  subtree: true
});
