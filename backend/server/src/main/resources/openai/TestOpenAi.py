import openai
import os
import sys
from dotenv import load_dotenv

# .env 파일 로드
load_dotenv()

content = sys.argv[1]
important = sys.argv[2]

openai.api_key = os.getenv('OPENAI_API_KEY')

model = "gpt-4o-mini"

messages = [
    {"role":"system","content": "당신은 낚시성 정보로 판단된 글에 대해 해설하는 AI입니다."},
    {"role":"system","content": "다음은 본문 내용입니다:"},
    {"role":"system","content": content},
    {"role":"system","content": "다음은 낚시성 정보라고 판단하는 데 중요한 역할을 한 문장들입니다:"},
    {"role":"system","content": important},
    {"role":"system","content": "이에 대한 해석을 Markdown 형식으로 헤더를 작성하면서 간결하게 작성해주세요:"}
]

response = openai.chat.completions.create(model = model, messages = messages)
answer = response.choices[0].message.content
print(answer)