import requests
import json

# API 주소
API_URL = ""

# 테스트 데이터
test_data = {
    "clothes": [
        {
            "id": "cloth_001",
            "type": "셔츠",
            "color": "화이트",
            "style": "포멀",
            "material": "면",
            "season": "사계절"
        },
        {
            "id": "cloth_002",
            "type": "바지",
            "color": "네이비",
            "style": "포멀",
            "material": "데님",
            "season": "사계절"
        },
        {
            "id": "cloth_003",
            "type": "맨투맨",
            "color": "베이지",
            "style": "캐쥬얼",
            "material": "면",
            "season": "봄"
        },
        {
            "id": "cloth_004",
            "type": "티셔츠",
            "color": "블랙",
            "style": "미니멀",
            "material": "면",
            "season": "여름"
        },
        {
            "id": "cloth_005",
            "type": "치마",
            "color": "핑크",
            "style": "러블리",
            "material": "폴리",
            "season": "봄"
        }
    ],
    "weather": {
        "temp": 18,
        "condition": "맑음"
    },
    "schedule": "데이트"
}

print("API 테스트 시작...")
print("-" * 50)

try:
    # API 호출
    response = requests.post(API_URL, json=test_data)
    
    # 결과 출력
    if response.status_code == 200:
        result = response.json()
        print("✅ 성공!")
        print("\n추천 결과:")
        print(json.dumps(result, ensure_ascii=False, indent=2))
    else:
        print(f"❌ 실패: {response.status_code}")
        print(response.text)

except Exception as e:
    print(f"❌ 오류: {e}")
    print("\n💡 api_server.py가 실행 중인지 확인하세요!")