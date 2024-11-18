
import time

#start = time.time()

import urllib.request
import pytesseract
import cv2
import sys

#이미지 주소
image_path = []

for i in range(1, len(sys.argv)):
    image_path.append(sys.argv[i])

"""
#pillow lib 사용한 ocr
image_PIL = Image.open(image_path)

image_PIL = image_PIL.convert("L")
image_PIL = image_PIL.point(lambda x: 0 if x < 128 else 255)

text_PIL = pytesseract.image_to_string(image_PIL, lang="kor+eng")
"""

#request를 이용한 이미지 받기
for i in range(len(image_path)):
    urllib.request.urlretrieve(image_path[i], f'backend/modules/ocr/TempImage/Temp{i:02d}.png')

#opencv lib 사용한 ocr
for i in range(len(image_path)):
    image_cv2 = cv2.imread(f'backend/modules/ocr/TempImage/Temp{i:02d}.png')

    text_cv2 = pytesseract.image_to_string(image_cv2, lang="kor+eng")

    #print("추출된 텍스트(PIL): ", text_PIL)
    print("추출된 텍스트(cv2): ", text_cv2)

cv2.waitKey(0)

#print(time.time() - start)
#print(1.172527551651001 - 0.7256977558135986)