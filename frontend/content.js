async function fetchPOST(url, data) {
  try {
      const response = await fetch(url, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify(data),
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
정밀 측정 : http://localhost:8080/api/v1/server/precision
사용자 정의 측정 : http://localhost:8080/api/v1/server/custom
 */

const prob_dict = {}
const prob_queue = []
const tooltip = document.createElement('div')

async function getProbability(url) {
  if (!(url in prob_dict)) {
    const recv = await fetchPOST("http://localhost:8080/api/v1/server/quick?", { "url": url });
    console.log(recv.status)
    if (recv.status) return;
    const probability = parseInt(recv * 100);
    prob_dict[url] = probability;
  }
  if (tooltip._url === url) tooltip.innerText = "낚시성 확률: " + prob_dict[url] + "%"
}

async function queueProbability(url) {
  if (url in prob_dict) return;
  if (prob_queue.includes(url)) return;
  prob_queue.push(url);
  if (prob_queue.length > 1) return;
  while (true) {
    await getProbability(prob_queue.at(-1));
    prob_queue.pop();
    if (prob_queue.length === 0) return;
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
    tooltip.innerText = "낚시성 확률: " + prob_dict[element.href] + "%";
  }
  else {
    setTimeout(()=>{queueProbability(element.href)}, 1);
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
  "https://news.kbs.co.kr/news/pc/view", // KBS 뉴스
  "https://www.hankyung.com/article", //한국경제
  "https://imnews.imbc.com/news", // MBC 뉴스
  "https://www.ohmynews.com/NWS_Web/View", // 오마이뉴스
  "https://www.mk.co.kr/news", // 매일경제
  "https://www.dailian.co.kr/news/view", // 데일리안
  "https://www.nocutnews.co.kr/news", // 노컷뉴스
  "https://view.asiae.co.kr/article", // 아시아경제
  "https://www.edaily.co.kr/News", // 이데일리
  "https://biz.heraldcorp.com/article", // 해럴드경제
  "https://zdnet.co.kr/view",
  "https://www.seoul.co.kr/news", // 서울신문
  "https://www.osen.co.kr/article", // OSEN
  "https://news.sbs.co.kr/news", // SBS 뉴스
  "https://newstapa.org/article", //뉴스타파
  "https://www.hankookilbo.com/News/Read", // 한국일보
  "https://isplus.com/article/view", // 일간스포츠
  "https://www.newsis.com/view", // 뉴시스
  "https://www.inews24.com/view", // 아이뉴스
  "https://mydaily.co.kr/page/view", // 마이데일리
  "https://www.donga.com/news/NewsStand/article", // 동아일보
  "https://news.jtbc.co.kr/article", // JTBC 뉴스
  "https://www.ytn.co.kr/", // ytn 뉴스
  "https://www.newdaily.co.kr/site/data/html", // 뉴데일리
  "https://www.dt.co.kr/contents", // 디지털타임스
  "https://sports.chosun.com/", // 스포츠조선
  "https://www.chosun.com/", // 조선일보
  "https://www.sportsseoul.com/news/read", // 스포츠서울
  "https://www.khan.co.kr/article", // 경향신문
  "https://sports.donga.com/NewsStand/article", // 스포츠동아
  "https://news.mt.co.kr/mtview", // 머니투데이
  "https://www.etnews.com/", // 전자신문
  "https://www.sedaily.com/NewsView", // 서울경제
  "https://www.joongang.co.kr/article", // 중앙일보
  "https://www.sisain.co.kr/news/article", // 시사인
  "https://www.wowtv.co.kr/NewsCenter/News/Read", // 한국경제TV
  "https://www.yonhapnewstv.co.kr/news/", // 연합뉴스
  "https://www.kmib.co.kr/article/", // 국민일보
  "https://www.mbn.co.kr/news", // MBN 뉴스
  "https://biz.chosun.com/", // 조선비즈
  "https://www.segye.com/newsView", // 세계일보
  "https://www.fnnews.com/news", // 파이낸셜 뉴스
  "https://sportalkorea.com/news/", // 스포탈코리아
  "https://www.hani.co.kr/arti", // 한겨레
  "https://n.news.naver.com/article", // 네이버뉴스
];

let seeker_data = [];

function checkLink(href) {
  if (!href) {
    return false;
  }

  return allowLinks.some(link => href.startsWith(link));
}

function addHoverEffect() {
  // 텍스트 및 이미지 관련 요소 선택
  const elements = Array.from(document.querySelectorAll("a")).filter(element => {
      return checkLink(element.getAttribute("href"));
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
  if (message.action === "Measure") {
    measure();
  }
  else if (message.action === "addData") {
      addData(message.data);
  }
});

function addData(info) {
  let dataType = info.selectionText ? "text" : "image";
  let newData = info.selectionText || info.srcUrl;
  seeker_data.push(dataType);
  seeker_data.push(newData);
  console.log("Data:", seeker_data);
}

const banner = document.createElement("div");
const closeButton = document.createElement("button");
function showBanner(prob, explanation) {
  prob = Math.floor(prob * 100);
  banner.style.position = "fixed";
  banner.style.top = "0";
  banner.style.width = "100%";
  banner.style.padding = "10px 50px 10px 10px"; // 오른쪽 패딩 추가
  banner.style.zIndex = "9999";
  banner.style.color = "white";
  banner.style.display = "flex";
  banner.style.justifyContent = "space-between";
  banner.style.alignItems = "center";
  banner.style.boxSizing = "border-box";
  banner.style.backgroundColor = prob >= 50 ? "rgba(255, 0, 0, 0.8)" : "rgba(0, 128, 0, 0.8)";
  banner.innerHTML = `
    <div>
      낚시성 정보 확률: ${prob}%<br>해석:<br>${explanation}
    </div>
  `;

  closeButton.innerText = "닫기";
  closeButton.style.position = "absolute";
  closeButton.style.top = "10px"; // 버튼 위치를 상단으로
  closeButton.style.right = "10px"; // 버튼을 오른쪽에 고정
  closeButton.style.width = "80px"; // 버튼 크기 조정
  closeButton.style.background = "rgba(0, 0, 0, 0.5)";
  closeButton.style.color = "white";
  closeButton.style.border = "none";
  closeButton.style.padding = "5px"; // 버튼 내부 여백 조정
  closeButton.style.cursor = "pointer";
  closeButton.style.borderRadius = "5px"; // 버튼 모서리를 둥글게
  closeButton.addEventListener("click", () => {
    banner.removeChild(closeButton);
    document.body.removeChild(banner);
  });

  banner.appendChild(closeButton);
  document.body.appendChild(banner);
}

function highlight_text(text_no_spaces) {
  // 텍스트에서 공백을 제거하는 함수
  function removeSpaces(str) {
      return str.replace(/\s/g, '');
  }
  
  // LCS 알고리즘을 통해 가장 긴 공통 부분 문자열 찾기
  function findLongestCommonSubstring(str1, str2) {
      const m = str1.length;
      const n = str2.length;
      const dp = Array.from({ length: m + 1 }, () => new Array(n + 1).fill(0));
      let maxLength = 0;
      let endIndex = 0;
      for (let i = 1; i <= m; i++) {
          for (let j = 1; j <= n; j++) {
              if (str1[i - 1] === str2[j - 1]) {
                  dp[i][j] = dp[i - 1][j - 1] + 1;
                  if (dp[i][j] > maxLength) {
                      maxLength = dp[i][j];
                      endIndex = i - 1;
                  }
              }
          }
      }
      return {
          startIndex: endIndex - maxLength + 1,
          endIndex: endIndex,
          length: maxLength
      };
  }
  
  // 본문에서 모든 텍스트 노드 찾기
  function findTextNodesIn(node) {
      const textNodes = [];
      function traverseNodes(node) {
          if (node.nodeType === Node.TEXT_NODE) {
              textNodes.push(node);
          } else {
              for (let child of node.childNodes) {
                  traverseNodes(child);
              }
          }
      }
      traverseNodes(node);
      return textNodes;
  }
  
  // 텍스트 하이라이팅 함수 (수정됨)
  function highlightTextNode(textNode, start, end) {
      try {
          // 텍스트 노드를 분할하여 하이라이트
          const range = document.createRange();
          range.setStart(textNode, start);
          range.setEnd(textNode, end);
          
          // 하이라이트할 텍스트를 감싸는 mark 요소 생성
          const highlightSpan = document.createElement('mark');
          highlightSpan.style.backgroundColor = 'yellow';
          
          // 범위를 mark 요소로 감싸기
          range.surroundContents(highlightSpan);
      } catch (error) {
          console.error('하이라이트 중 오류 발생:', error);
          
          // 대체 방법: 텍스트 노드 분할 및 래핑
          const beforeText = textNode.textContent.slice(0, start);
          const highlightText = textNode.textContent.slice(start, end);
          const afterText = textNode.textContent.slice(end);
          
          const beforeNode = document.createTextNode(beforeText);
          const highlightNode = document.createElement('mark');
          highlightNode.style.backgroundColor = 'yellow';
          highlightNode.textContent = highlightText;
          const afterNode = document.createTextNode(afterText);
          
          const parentNode = textNode.parentNode;
          parentNode.replaceChild(afterNode, textNode);
          parentNode.insertBefore(highlightNode, afterNode);
          parentNode.insertBefore(beforeNode, highlightNode);
      }
  }
  
  // 메인 로직
  const bodyElement = document.body;
  const textNodes = findTextNodesIn(bodyElement);
  for (let textNode of textNodes) {
      const nodeText = textNode.textContent;
      const nodeTextNoSpaces = removeSpaces(nodeText);
      if (nodeTextNoSpaces.includes(text_no_spaces)) {
          const result = findLongestCommonSubstring(nodeTextNoSpaces, text_no_spaces);
          
          if (result.length === text_no_spaces.length) {
              // 원본 텍스트에서의 실제 인덱스 계산
              let actualStart = 0;
              let spaceCount = 0;
              for (let i = 0; i < nodeText.length; i++) {
                  if (removeSpaces(nodeText.slice(0, i + 1)) === nodeTextNoSpaces.slice(0, result.startIndex + 1)) {
                      actualStart = i;
                      break;
                  }
                  if (nodeText[i].trim() === '') spaceCount++;
              }
              highlightTextNode(
                  textNode, 
                  actualStart, 
                  actualStart + result.length + spaceCount
              );
              break; // 첫 번째 일치하는 노드만 하이라이트
          }
      }
  }
}

function process_recv(recv) {
  if (typeof(recv) === "number") {
    showBanner(recv, "낚시성으로 판단되지 않았습니다.");
    return;
  }
  for (sentence of recv.sentence) {
    highlight_text(sentence);
  }
  showBanner(recv.probability, recv.explanation);
}


async function measure() {
  const url = window.location.href;
  if (seeker_data.length == 0 && !checkLink(url)) {
    alert("측정할 데이터가 없습니다.");
    return;
  }
  showBanner(0, "측정 중...");
  if (seeker_data.length > 0) {
    const recv = await fetchPOST("http://localhost:8080/api/v1/server/custom?", { "content": seeker_data.join(",") });
    process_recv(recv);
  }
  else {
    const recv = await fetchPOST("http://localhost:8080/api/v1/server/precision?", { "url": url });
    process_recv(recv);
  }
}