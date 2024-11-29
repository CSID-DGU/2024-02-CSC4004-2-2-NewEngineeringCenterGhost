import openai
import sys

parameter = ""

parameter = sys.argv[1]

OPENAI_API_KEY = ""

openai.api_key = OPENAI_API_KEY

model = "gpt-4omini"

messages = [{"role":"system","content": "너는 낚시성 정보를 탐지하는 프로그램이야."},
            {"role":"system","content": "다음은 네가 판단해야 할 정보야."},
            {"role":"user","content": parameter},
            {"role":"system","content": "이 정보가 낚시성 정보인지 판단해줘."}]

response = openai.ChatCompletion.create(model = model, messages = messages)
answer = response['choices'][0]['message']['content']
print(answer)
