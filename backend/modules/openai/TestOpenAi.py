import openai
import sys

parameter = []

for i in range(1, len(sys.argv)):
    parameter.append(sys.argv[i])

OPENAI_API_KEY = ""

openai.api_key = OPENAI_API_KEY

model = "gpt-4omini"

messages = [{"role":"system","content": parameter}]

response = openai.ChatCompletion.create(model = model, messages = messages)
answer = response['choices'][0]['message']['content']
print(answer)
