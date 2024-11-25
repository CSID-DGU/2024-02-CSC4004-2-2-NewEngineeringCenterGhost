import socket
import sys

socket_path = "/tmp/sock"

client = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
client.connect(socket_path)

# test text
text = "CLS 쿠팡, 1분기 매출 6조5000억원 돌파…전년 比 22% ↑  SEP 쿠팡은 올해 1분기 실적을 집계한 결과 매출 51억1668만6000달러를 기록했다고 12일 밝혔다. SEP 한화로는 6조5212억원이다. SEP 이는 전년 대비 22% 증가한 수치다. SEP 순손실 규모는 2억929만4000달러를 기록했다. SEP 한화로 2667억4500만원이다. SEP 지난해 같은 기간보다 29% 감소했다. SEP"

if __name__ == '__main__':
    if len(sys.argv) > 1:
        text = " ".join(sys.argv[1:])
    client.sendall(text.encode())
    data = client.recv(8192)
    print(data.decode())
    client.close()