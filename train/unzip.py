import os
import zipfile
import json

# zip 폴더에는 AI_HUB에서 다운로드한 zip 파일들이 들어있어야 합니다.
zip_path = './train/data/zip/'
unzip_path = './train/data/unzip/'
result_path = './train/data/data.jsonl'

if not os.path.exists(unzip_path):
    os.makedirs(unzip_path)

file_list = os.listdir(zip_path)
length = len(file_list)
print(f'file count: {length}')

jsonl = open(result_path, 'a', encoding='utf-8')
lens = []
for i, f in enumerate(file_list):
    print(f'{i+1}/{length}')
    zip_file = zipfile.ZipFile(zip_path + f)
    zip_file.extractall(unzip_path)
    zip_file.close()
    
    _len = len(os.listdir(unzip_path))
    for j, _f in enumerate(os.listdir(unzip_path)):
        print(f'\r{j+1}/{_len}', end='')
        _json = json.load(open(unzip_path + _f, 'r', encoding='utf-8'))
        sentences = _json['labeledDataInfo']['processSentenceInfo'] if _json['sourceDataInfo']['partNum'] == 'P2' else _json['sourceDataInfo']['sentenceInfo']

        p = dict()
        p['sentence'] = 'CLS '
        p['sentence'] += (_json['sourceDataInfo']['newsTitle'] + ' ') if _json['sourceDataInfo']['partNum'] == 'P2' else (_json['labeledDataInfo']['newTitle'] + ' ')
        p['sentence'] += 'SEP '

        if len(_json['sourceDataInfo']['newsSubTitle']) > 0:
            p['sentence'] += _json['sourceDataInfo']['newsSubTitle'] + 'SEP '
        for k in sentences:
            p['sentence'] += k['sentenceContent'] + 'SEP '

        p['label'] = _json['labeledDataInfo']['clickbaitClass']
        
        jsonl.write(json.dumps(p, ensure_ascii=False) + '\n')
        lens.append(len(p['sentence']))

        os.remove(unzip_path + _f)
    print()
jsonl.close()

lens = sorted(lens)
print(f'average length: {sum(lens) / len(lens)}')
print(f'median length: {lens[len(lens) // 2]}')
print(f'max length: {lens[-1]}')
print(f'min length: {lens[0]}')