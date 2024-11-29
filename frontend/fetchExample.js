async function fetchPOSTExample(url, data) {
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