document.addEventListener('DOMContentLoaded', () => {
    const measureButton = document.getElementById('measureButton');
    const checkH2 = document.getElementById('checkH2');

    chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
        if (!tabs.length) return;
        const url = tabs[0].url;

        // URL에 따라 로직 처리
        if (checkLink(url)) {
            measureButton.disabled = false;
            checkH2.innerHTML = '이 페이지는 측정 가능한<br>페이지입니다.<br><br>아래 버튼으로 측정할 수<br>있습니다.';
        } else {
            measureButton.disabled = true;
            checkH2.innerHTML = '이 페이지는 측정 불가능한<br>페이지입니다.<br><br>사용자 정의 측정 기능을<br>사용해주세요.';
        }

        measureButton.addEventListener('click', () => {
            // "측정하기" 버튼 클릭 시 background.js로 메시지 전송
            chrome.runtime.sendMessage({ action: 'Measure', tabId: tabs[0].id });
        });
    });
});