import os
import zipfile
import json

zip_path = './train/local/data/zip/'
unzip_path = './train/local/data/unzip/'
result_path = './train/local/data/data.jsonl'

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
        p['sentence'] = (_json['sourceDataInfo']['newsTitle'] + ' ') if _json['sourceDataInfo']['partNum'] == 'P2' else (_json['labeledDataInfo']['newTitle'] + ' ')

        if len(_json['sourceDataInfo']['newsSubTitle']) > 0:
            p['sentence'] += _json['sourceDataInfo']['newsSubTitle'] + ' '
        for k in sentences:
            p['sentence'] += k['sentenceContent'] + ' '

        p['label'] = _json['labeledDataInfo']['clickbaitClass']
        
        jsonl.write(json.dumps(p, ensure_ascii=False) + '\n')
        lens.append(len(p['sentence']))

        os.remove(unzip_path + _f)
    print()
jsonl.close()

print(f'average length: {sum(lens) / len(lens)}')
print(f'median length: {sorted(lens)[len(lens) // 2]}')
print(f'max length: {max(lens)}')
print(f'min length: {min(lens)}')