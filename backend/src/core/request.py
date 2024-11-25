import socket

socket_path = "/tmp/sock"

client = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
client.connect(socket_path)

text = "CLS 멜론, 음원사용료 月 4000원 인상  SEP 새해부터 적용되는 음원 사용료 징수 개정에 따라 멜론의 음원 서비스 가격이 올랐다. SEP 멜론은 '프리클럽' 이용료를 월 1만900원에서 14900원(부가세 별도)으로 4000원 인상했다. SEP 프리클럽은 무제한 듣기가 가능하고 모바일에서는 무제한으로 PC에서는 100곡 다운로드가 가능한 상품이다. SEP 모바일과 PC에서 무제한 듣기, 30곡 다운로드가 가능한 'MP3 30 플러스'는 1만3000원에서 1만6000원, 'MP3 50 플러스'는1만5000원에서 1만9000원으로 인상했다. SEP 이는 올해 1월 1일부터 음원 사용료 징수규정 개정안이 적용되기 때문이다. SEP 문화체육관광부는 30곡 이상 묶음(스트리밍+다운로드)상품 할인율을 단계적으로 폐지해 곡당 사용료를 490원으로 통합하고자 한다. SEP 문체부는 2019년부터 30곡 상품의 할인율을 50%에서 40%로, 50곡 상품의 할인율을 59.1%에서 50.9%로 낮췄다. SEP 2021년에는 묶음상품 할인율이 폐지된다. SEP 음원 서비스 업체는 문체부가 정한 할인율 이상의 할인을 제공할 수 없다. SEP 멜론 관계자는 2일 미디어SR에 \"이번 멜론 묶음상품의 가격 인상은 문체부 징수규정 개정에 따른 인상\"이라 밝혔다. SEP 스트리밍 음원 징수규정도 이용료 중 창작자가 65%의 수익을 가져가도록 개정됐다. SEP 개정 전 창작자가 가져가는 비중은 60%였다. SEP 멜론 관계자는 \"이번 개정에 따른 스트리밍 서비스 가격 인상은 없다\"고 밝혔다. SEP 작년까지 정기결제 이용권을 보유하고 있는 회원이라면 가격인상 요금을 적용받지 않는다. SEP 멜론은 초기 가격 인상의 충격을 완화하기 위해 할인 프로모션 등을 진행하고 있다. SEP 한편, 지니뮤직은 스마트폰 전용 스트리밍 상품 '스마트 음악감상'을 월 7400원으로, '무제한 스트리밍 음악감상'은 8400원으로 각각 600원씩 올렸다. SEP "
client.sendall(text.encode())
print(client.recv(1024).decode())