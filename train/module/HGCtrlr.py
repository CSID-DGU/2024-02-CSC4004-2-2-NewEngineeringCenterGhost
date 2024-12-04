'''
Hangul Controller : 한글 조합 및 분해 관련 함수들을 제공하는 모듈
    - decomposeHangul(syllable: str) -> tuple : 한글 음절을 자모 단위로 분해
    - decomposeHangulText(text: str) -> str : 전체 텍스트에 대해 자모 분해를 수행
    - composeHangul(jamos: list) -> str : 자모를 합쳐서 한글 음절로 조합
    - composeHangulText(text: str) -> str : 전체 텍스트에 대해 자모 조합을 수행
'''

HG_BASE = 0xAC00
HG_CHO = 588
HG_JUNG = 28

HG_DICT = {
    'cho' : ['ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'],
    'jung' : ['ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'],
    'jong' : ['', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ']
}

def decomposeHangul(syllable: str) -> tuple:
    code = ord(syllable) - HG_BASE
    cho = code // HG_CHO
    jung = (code - HG_CHO * cho) // HG_JUNG
    jong = (code - HG_CHO * cho - HG_JUNG * jung)

    return (
        HG_DICT['cho'][cho],
        HG_DICT['jung'][jung],
        HG_DICT['jong'][jong]
    )

def decomposeHangulText(text: str) -> str:
    result = []
    for char in text:
        if 0xAC00 <= ord(char) <= 0xD7A3:
            decomposed = decomposeHangul(char)
            result.extend([jamo for jamo in decomposed if jamo])
        else:
            result.append(char)
    return ''.join(result)

def composeHangul(jamos: list) -> str:
    cho = ''
    jung = ''
    jong = ''

    if len(jamos) == 3:
        cho, jung, jong = jamos
    elif len(jamos) == 2:
        cho, jung = jamos
    elif len(jamos) == 1:
        return jamos[0]
    else:
        raise ValueError('Too many jamos')
    
    cho_idx = HG_DICT['cho'].index(cho)
    jung_idx = HG_DICT['jung'].index(jung)
    jong_idx = HG_DICT['jong'].index(jong)

    code = HG_BASE + HG_CHO * cho_idx + HG_JUNG * jung_idx + jong_idx
    return chr(code)

def composeHangulText(text: str) -> str:
    comb = []
    for i, c in enumerate(text):
        if c not in HG_DICT['jung']:
            continue
        if i == 0:
            continue
        if text[i-1] not in HG_DICT['cho']:
            continue
        if i == len(text) - 1:
            comb.append((i-1, 2)) # 초성 시작과 조합 총 길이
            continue
        if text[i+1] not in HG_DICT['jong']:
            comb.append((i-1, 2))
            continue
        if i == len(text) - 2:
            comb.append((i-1, 3))
            continue
        if text[i+2] in HG_DICT['jung'] and text[i+1] in HG_DICT['cho']:
            comb.append((i-1, 2))
            continue
        comb.append((i-1, 3))

    comb.reverse()
    for begin, length in comb:
        text = text[:begin] + composeHangul(text[begin:begin+length]) + text[begin+length:]

    return text
        

if __name__ == '__main__':
    s = 'ㅏ안녕하세 ㅇ요 ㅇ. 저는 한ㄱ글을 ㅈ조합하 ㄱ고 부ㅜㄴ해할 수 있는 프ㅡ로ㅗ그램ㅁ입니 ㄷ ㅏ. 이용해주셔서 감사합니다. 뷁뷁 Thank you.'
    s = decomposeHangulText(s)
    print(s)
    s = composeHangulText(s)
    print(s)