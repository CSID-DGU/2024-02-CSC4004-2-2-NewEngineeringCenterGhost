import os
import socket
from module.Seeker import Seeker
import time

seeker = Seeker()
print("Model loaded")

socket_path = "/tmp/sock"
if os.path.exists(socket_path):
    os.remove(socket_path)

server = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
server.bind(socket_path)

server.listen(5)

while True:
    conn, addr = server.accept()
    data = conn.recv(8192 * 4)
    data = data.decode(encoding='utf-8')
    now = time.time()
    res = seeker.predict(data)
    print(f"Predicted in {time.time() - now:.4f} sec")
    conn.send(str(res).encode())
    conn.close()