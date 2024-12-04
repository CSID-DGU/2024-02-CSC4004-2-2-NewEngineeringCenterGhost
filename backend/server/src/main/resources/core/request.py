import socket
import sys
import time

socket_path = "/tmp/sock"

client = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
while True:
    try:
        client.connect(socket_path)
        break
    except:
        time.sleep(1)

if __name__ == '__main__':
    if len(sys.argv)  < 3:
        print("매개변수가 2개 필요합니다.")
        sys.exit(1)
    
    text = (sys.argv[1] + " #SEP.T.C# " + sys.argv[2]).encode()
    client.sendall(str(len(text)).encode())
    client.recv(2)
    client.sendall(text)
    data = client.recv(8192)
    print(data.decode())
    client.close()