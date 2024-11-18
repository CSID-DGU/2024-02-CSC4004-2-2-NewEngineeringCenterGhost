from PIL import Image
import pytesseract
import cv2

#이미지 주소
image_path = "/home/chosungwon/Downloads/test_card_news.png"

#pillow lib 사용한 ocr
image_PIL = Image.open(image_path)

image_PIL = image_PIL.convert("L")
image_PIL = image_PIL.point(lambda x: 0 if x < 128 else 255)

text_PIL = pytesseract.image_to_string(image_PIL, lang="kor+eng")

#opencv lib 사용한 ocr
image_cv2 = cv2.imread(image_path)

text_cv2 = pytesseract.image_to_string(image_cv2, lang="kor+eng")

cv2.waitKey(0)

print("추출된 텍스트(PIL): ", text_PIL)
print("추출된 텍스트(cv2): ", text_cv2)