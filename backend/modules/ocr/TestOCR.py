import urllib.request
import pytesseract
import cv2
import sys
import os

#이미지 주소
image_path = []

for i in range(1, len(sys.argv)):
    image_path.append(sys.argv[i])

#os lib를 사용한 TempImage 폴더 생성
temp_path = f'backend/modules/ocr/TempImage'

if not os.path.exists(temp_path):
    os.makedirs(temp_path)

#request를 이용한 이미지 받기
for i in range(len(image_path)):
    urllib.request.urlretrieve(image_path[i], f'backend/modules/ocr/TempImage/Temp{i:02d}.png')

#opencv lib 사용한 ocr
for i in range(len(image_path)):
    image_cv2 = cv2.imread(f'backend/modules/ocr/TempImage/Temp{i:02d}.png')

    text_cv2 = pytesseract.image_to_string(image_cv2, lang="kor+eng")

    #print("추출된 텍스트(PIL): ", text_PIL)
    print("추출된 텍스트(cv2): ", text_cv2)

#os lib를 이용한 TempImage에 생성된 png 파일 삭제
if os.path.exists(temp_path):
    for path in os.listdir(temp_path):
        os.remove('backend/modules/ocr/TempImage/' + path)

cv2.waitKey(0)