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
for i, f in enumerate(file_list):
    print(f'{i+1}/{length}')
    zip_file = zipfile.ZipFile(zip_path + f)
    zip_file.extractall(unzip_path)
    zip_file.close()
    
    _len = len(os.listdir(unzip_path))
    for j, _f in enumerate(os.listdir(unzip_path)):
        print(f'\r{j+1}/{_len}', end='')
        _json = json.load(open(unzip_path + _f, 'r', encoding='utf-8'))
        if 'sourceDataInfo' not in _json.keys():
            print(_f)
            continue
        if 'labeledDataInfo' not in _json.keys():
            print(_f)
            continue
        if 'newsTitle' not in _json['sourceDataInfo'].keys():
            print(_f)
            continue
        if 'newsSubTitle' not in _json['sourceDataInfo'].keys():
            print(_f)
            continue
        if 'newsContent' not in _json['sourceDataInfo'].keys():
            print(_f)
            continue
        if 'clickbaitClass' not in _json['labeledDataInfo'].keys():
            print(_f)
            continue
        p = dict()
        p['sentence'] = _json['sourceDataInfo']['newsTitle'] + ' '
        if len(_json['sourceDataInfo']['newsSubTitle']) > 0:
            p['sentence'] += _json['sourceDataInfo']['newsSubTitle'] + ' '
        p['sentence'] += _json['sourceDataInfo']['newsContent']
        p['label'] = _json['labeledDataInfo']['clickbaitClass']
        
        jsonl.write(json.dumps(p, ensure_ascii=False) + '\n')

        os.remove(unzip_path + _f)
    print()

print('\nDone')