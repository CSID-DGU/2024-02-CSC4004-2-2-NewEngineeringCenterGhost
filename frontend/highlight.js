
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