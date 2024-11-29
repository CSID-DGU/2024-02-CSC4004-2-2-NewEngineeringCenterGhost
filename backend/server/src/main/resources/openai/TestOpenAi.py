import openai
import os
import sys

parameter = ""

parameter = sys.argv[1]

openai.api_key = ""

model = "gpt-4o-mini"

messages = [{"role":"system","content": "당신은 낚시성 정보로 판단된 기사에 대해 해설하는 AI입니다."},
            {"role":"system","content": "다음은 네가 판단해야 할 정보야."},
            {"role":"user","content": parameter},
            {"role":"system","content": "이 정보가 낚시성 정보인지 판단해줘."}]

response = openai.chat.completions.create(model = model, messages = messages)
answer = response.choices[0].message.content
print(answer)
