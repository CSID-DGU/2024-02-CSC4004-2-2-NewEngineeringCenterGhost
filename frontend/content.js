// content.js
function showProbability(event) {
    const link = event.target;
    const probability = Math.floor(Math.random() * 100); // 0-100 사이의 확률 생성

    const tooltip = document.createElement('div');
    tooltip.style.position = 'absolute';
    tooltip.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    tooltip.style.color = 'white';
    tooltip.style.padding = '5px';
    tooltip.style.borderRadius = '5px';
    tooltip.innerText = `거짓일 확률: ${probability}%`;

    document.body.appendChild(tooltip);

    const rect = link.getBoundingClientRect();
    tooltip.style.left = `${rect.left + window.scrollX}px`;
    tooltip.style.top = `${rect.top + window.scrollY - tooltip.offsetHeight}px`;

    link.addEventListener('mouseout', () => {
        tooltip.remove();
    }, { once: true });
}

document.querySelectorAll('a').forEach(link => {
    link.addEventListener('mouseover', showProbability);
});

document.querySelectorAll('p').forEach(link => {
  link.addEventListener('mouseover', showProbability);
});