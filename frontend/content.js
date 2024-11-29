async function fetchPOSTExample(url, data) {
  url = "http://localhost:8080/api/v1/server/quick?url=" + encodeURIComponent(data.url);
  try {
      const response = await fetch(url, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
      });

      const result = await response.json();
      console.log('Success:', result);
      return result;
  }
  catch (error) {
      console.error('Error:', error);
  }
}

/*
[ API url mapping 가이드 ]
빠른 측정 : http://localhost:8080/api/v1/server/quick
정밀 측정 : http://localhost:8080/api/v1/server/precisiong
사용자 정의 측정 : http://localhost:8080/api/v1/server/custom
 */

const prob_dict = {}
const prob_queue = []
const tooltip = document.createElement('div')

async function getProbability(element) {
  const url = element.href;
  if (!(url in prob_dict)) {
    const recv = await fetchPOSTExample("http://localhost:8080/api/v1/server/quick", { url: url });
    console.log(recv.status)
    if (recv.status) return;
    const probability = parseInt(recv * 100);
    prob_dict[url] = "낚시성 확률: " + probability + "%"
  }
  if (tooltip._url === url) tooltip.innerText = prob_dict[url];
}

async function queueProbability(element) {
  if (element in prob_dict) return;
  prob_queue.push(element);
  if (prob_queue.length > 1) return;
  while (prob_queue.length > 0) {
    await getProbability(prob_queue.at(-1));
    prob_queue.pop();
  }
}

function showProbability(event) {
  const element = event.target;

  tooltip.style.display = 'block';
  if (!document.body.contains(tooltip)) {
    // 툴팁 생성
    tooltip.style.position = 'absolute';
    tooltip.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    tooltip.style.color = 'white';
    tooltip.style.padding = '5px';
    tooltip.style.borderRadius = '5px';
    tooltip.style.fontSize = '12px';
    tooltip.style.zIndex = '10000';

    document.body.appendChild(tooltip);
  }
  tooltip.innerText = `측정 중...`;
  tooltip._url = element.href;

  if (element.href in prob_dict) {
    tooltip.innerText = prob_dict[element.href]
  }
  else {
    setTimeout(queueProbability(element), 1);
  }

  // 툴팁 위치 설정
  const rect = element.getBoundingClientRect();
  tooltip.style.left = `${rect.left + window.scrollX}px`;
  tooltip.style.top = `${rect.top + window.scrollY - tooltip.offsetHeight}px`;

  // 마우스가 벗어나면 툴팁 제거
  element.addEventListener('mouseout', () => {
    tooltip.style.display = 'none';
  });
}


const allowLinks = [
  "https://www.khan.co.kr/article", // 경향신문
  "https://news.kbs.co.kr/news/", // KBS
  "https://www.seoul.co.kr/news/", // 서울신문
  "https://www.sisain.co.kr/news/", // 시사인
  "https://www.hankyung.com/article/", // 한경신문

  "https://blog.naver.com/", // 네이버 블로그
];

let seeker_data = [];

function checkLink(element) {
  const href = element.getAttribute("href");

  if (!href) {
      return false;
  }

  return allowLinks.some(link => href.startsWith(link));
}

function addHoverEffect() {
  // 텍스트 및 이미지 관련 요소 선택
  const elements = Array.from(document.querySelectorAll("a")).filter(element => {
      return checkLink(element);
  });

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

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === "predictCustomMeasure") {
      predictCustomMeasure();
  }
  else if (message.action === "addData") {
      addData(message.data);
  }
});

function addData(info) {
  let dataType = info.selectionText ? 0 : 1;
  let newData = info.selectionText || info.srcUrl;
  seeker_data.push({ type: dataType, data: newData });
  console.log("Data:", seeker_data);
}

function predictCustomMeasure() {
  //fetch code

  let result = {}
}