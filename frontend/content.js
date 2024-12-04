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


function getAbsoluteUrl(href) {
  if (href.startsWith("/")) {
    href = window.location.origin + href;
  }
  return href;
}

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
  let element = event.target;
  if (element.href === undefined) {
    element = element.closest('a');
  }
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
    setTimeout(()=>{queueProbability(getAbsoluteUrl(element.href))}, 1);
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
  "news.kbs.co.kr/news/pc/view", // KBS 뉴스
  "www.hankyung.com/article", //한국경제
  "imnews.imbc.com/news", // MBC 뉴스
  "www.ohmynews.com/NWS_Web/View", // 오마이뉴스
  "www.mk.co.kr/news", // 매일경제
  "www.dailian.co.kr/news/view", // 데일리안
  "www.nocutnews.co.kr/news", // 노컷뉴스
  "view.asiae.co.kr/article", // 아시아경제
  "www.edaily.co.kr/News", // 이데일리
  "biz.heraldcorp.com/article", // 해럴드경제
  "zdnet.co.kr/view",
  "www.seoul.co.kr/news", // 서울신문
  "www.osen.co.kr/article", // OSEN
  "news.sbs.co.kr/news", // SBS 뉴스
  "newstapa.org/article", //뉴스타파
  "www.hankookilbo.com/News/Read", // 한국일보
  "isplus.com/article/view", // 일간스포츠
  "www.newsis.com/view", // 뉴시스
  "www.inews24.com/view", // 아이뉴스
  "mydaily.co.kr/page/view", // 마이데일리
  "www.donga.com/news/NewsStand/article", // 동아일보
  "news.jtbc.co.kr/article", // JTBC 뉴스
  "www.ytn.co.kr/", // ytn 뉴스
  "www.newdaily.co.kr/site/data/html", // 뉴데일리
  "sports.chosun.com/", // 스포츠조선
  "www.chosun.com/", // 조선일보
  "www.sportsseoul.com/news/read", // 스포츠서울
  "www.khan.co.kr/article", // 경향신문
  "sports.donga.com/NewsStand/article", // 스포츠동아
  "news.mt.co.kr/mtview", // 머니투데이
  "www.sedaily.com/NewsView", // 서울경제
  "www.joongang.co.kr/article", // 중앙일보
  "www.sisain.co.kr/news/article", // 시사인
  "www.wowtv.co.kr/NewsCenter/News/Read", // 한국경제TV
  "www.yonhapnewstv.co.kr/news/", // 연합뉴스
  "www.kmib.co.kr/article/", // 국민일보
  "www.mbn.co.kr/news", // MBN 뉴스
  "biz.chosun.com/", // 조선비즈
  "www.segye.com/newsView", // 세계일보
  "www.fnnews.com/news", // 파이낸셜 뉴스
  "sportalkorea.com/news/", // 스포탈코리아
  "www.hani.co.kr/arti", // 한겨레
  "n.news.naver.com/article", // 네이버뉴스
  "instagram.com", // 인스타그램
  "blog.naver.com", // 네이버블로그
  "tistory.com", // 티스토리
];

let seeker_data = [];


function checkLink(href) {
  if (!href) {
    return false;
  }

  return allowLinks.some(link => getAbsoluteUrl(href).includes(link))
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
  
  // 배너 스타일 설정
  banner.style.position = "fixed";
  banner.style.top = "0";
  banner.style.width = "100%";
  banner.style.padding = "10px 50px 10px 10px";
  banner.style.zIndex = "9999";
  banner.style.display = "flex";
  banner.style.justifyContent = "space-between";
  banner.style.alignItems = "center";
  banner.style.boxSizing = "border-box";
  banner.style.backgroundColor = prob >= 50 ? "rgba(255, 0, 0, 0.8)" : "rgba(0, 128, 0, 0.8)";
  banner.style.fontSize = '16px';
  banner.style.fontFamily = 'Arial, sans-serif';
  // 폰트 색상 설정 (우선순위 높이기)
  banner.style.setProperty('color', 'white', 'important');
  
  // 내용 컨테이너 생성 및 스타일 설정
  const contentDiv = document.createElement("div");
  contentDiv.style.fontSize = '16px';
  contentDiv.style.fontFamily = 'Arial, sans-serif';
  // 폰트 색상 설정 (우선순위 높이기)
  contentDiv.style.setProperty('color', 'white', 'important');
  contentDiv.innerHTML = `
    낚시성 정보 확률: ${prob}%<br>해석:<br>${parseMd(explanation)}
  `;
  
  // 닫기 버튼 스타일 설정
  closeButton.innerText = "닫기";
  closeButton.style.position = "absolute";
  closeButton.style.top = "10px";
  closeButton.style.right = "10px";
  closeButton.style.width = "80px";
  closeButton.style.background = "rgba(0, 0, 0, 0.5)";
  closeButton.style.border = "none";
  closeButton.style.padding = "5px";
  closeButton.style.cursor = "pointer";
  closeButton.style.borderRadius = "5px";
  closeButton.style.fontSize = '14px';
  closeButton.style.fontFamily = 'Arial, sans-serif';
  // 버튼 텍스트 색상 설정 (우선순위 높이기)
  closeButton.style.setProperty('color', 'white', 'important');
  
  // 닫기 버튼 이벤트 리스너
  closeButton.addEventListener("click", () => {
    document.body.removeChild(banner);
  });
  
  // 배너에 요소 추가
  banner.innerHTML = ''; // 기존 내용 초기화
  banner.appendChild(contentDiv);
  banner.appendChild(closeButton);
  
  // 배너를 문서에 추가
  document.body.appendChild(banner);
}

function process_recv(recv) {
  console.log(recv);
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