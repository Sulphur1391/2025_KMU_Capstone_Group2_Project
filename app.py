# app.py
import os
import uuid
from flask import Flask, request, jsonify
from clothes_analyzer import analyze_clothes

app = Flask(__name__)

# 업로드 임시 폴더
UPLOAD_DIR = "uploaded_images"
os.makedirs(UPLOAD_DIR, exist_ok=True)


@app.route("/health", methods=["GET"])
def health_check():
    return jsonify({"status": "ok"})


@app.route("/api/analyze-clothes", methods=["POST"])
def analyze_clothes_api():
    """
    모바일 앱에서 보낸 이미지 파일을 받아서
    analyze_clothes로 분석하고 JSON으로 반환.
    """
    if "file" not in request.files:
        return jsonify({"detail": "file 필드가 필요합니다."}), 400

    file = request.files["file"]

    if file.filename == "":
        return jsonify({"detail": "파일 이름이 비어 있습니다."}), 400

    # 확장자 체크
    _, ext = os.path.splitext(file.filename)
    ext = ext.lower()
    if ext not in [".jpg", ".jpeg", ".png", ".webp"]:
        return jsonify({"detail": "지원하지 않는 이미지 형식입니다."}), 400

    # 임시 파일 경로 생성
    temp_filename = f"{uuid.uuid4()}{ext}"
    temp_path = os.path.join(UPLOAD_DIR, temp_filename)

    try:
        # 파일 저장
        file.save(temp_path)
    except Exception as e:
        return jsonify({"detail": f"파일 저장 중 오류: {str(e)}"}), 500

    # 분석 실행
    try:
        result = analyze_clothes(temp_path)
    except Exception as e:
        return jsonify({"detail": f"이미지 분석 중 오류: {str(e)}"}), 500
    finally:
        # 임시 파일 삭제 (원하면 주기적으로만 지우도록 바꿔도 됨)
        if os.path.exists(temp_path):
            os.remove(temp_path)

    # Flask는 dict를 자동으로 JSON 변환
    return jsonify(result)


if __name__ == "__main__":
    # 로컬 개발용
    app.run(host="0.0.0.0", port=8000, debug=True)