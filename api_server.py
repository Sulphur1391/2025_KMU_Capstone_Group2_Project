from flask import Flask, request, jsonify
from flask_cors import CORS
from fashion_ai import FashionRecommendationAI
import json

app = Flask(__name__)
CORS(app)  # 다른 앱에서 접근 가능하게

API_KEY = ""
ai = FashionRecommendationAI(api_key=API_KEY)

@app.route('/')
def home():
    return """
    <h1>패션 추천 AI 서버</h1>
    <p>서버가 정상 작동 중입니다!</p>
    <p>사용법: POST /api/recommend</p>
    """

@app.route('/api/recommend', methods=['POST'])
def recommend():
    """
    패션 추천 API
    
    요청 예시:
    {
        "clothes": [...],
        "weather": {"temp": 18, "condition": "맑음"},
        "schedule": "출근"
    }
    """
    try:
        # 요청 데이터 받기
        data = request.json
        
        # 필수 항목 확인
        if not data.get('clothes'):
            return jsonify({"error": "옷 데이터가 없습니다"}), 400
        if not data.get('weather'):
            return jsonify({"error": "날씨 데이터가 없습니다"}), 400
        if not data.get('schedule'):
            return jsonify({"error": "일정 데이터가 없습니다"}), 400
        
        # AI 추천 실행
        result = ai.recommend(
            clothes=data['clothes'],
            weather=data['weather'],
            schedule=data['schedule']
        )
        
        # 결과 반환
        return jsonify({
            "success": True,
            "recommendation": result
        })
    
    except Exception as e:
        return jsonify({
            "success": False,
            "error": str(e)
        }), 500

@app.route('/api/health', methods=['GET'])
def health():
    """서버 상태 체크"""
    return jsonify({"status": "ok", "message": "서버 정상 작동 중"})

if __name__ == '__main__':
    print("=" * 50)
    print("패션 추천 AI 서버 시작!")
    print("주소: http://localhost:5000")
    print("테스트: http://localhost:5000 를 브라우저에서 열어보세요")
    print("=" * 50)
    app.run(host='0.0.0.0', port=5000, debug=True)