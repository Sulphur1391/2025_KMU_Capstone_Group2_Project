# -*- coding: utf-8 -*-
from inference_sdk import InferenceHTTPClient
from colors import get_dominate_color_name

# 1. Roboflow Workflow 클라이언트 설정
client = InferenceHTTPClient(
    api_url="https://serverless.roboflow.com",
    api_key="rqEUCjFbHaJ6mOCbhOx2"  # 네가 Roboflow에서 받은 키
)

WORKSPACE_NAME = "grfgf"
WORKFLOW_ID = "custom-workflow-3"


def predict_clothes_type(image_path: str):
    """
    Roboflow Workflow를 호출해서 옷 종류(class)와 confidence를 가져오는 함수.
    """
    result = client.run_workflow(
        workspace_name=WORKSPACE_NAME,
        workflow_id=WORKFLOW_ID,
        images={
            "image": image_path  # 로컬 파일 경로 그대로 전달
        },
        use_cache=True,
    )

    print("Raw workflow result:", result)

    # result는 리스트 형태: [ { "predictions": { ... } } ]
    if not isinstance(result, list) or not result:
        return None, None

    first_item = result[0]
    preds_root = first_item.get("predictions")
    if not isinstance(preds_root, dict):
        return None, None

    # 여기에는 이미 'top' 과 'confidence'가 들어 있음
    cloth_type = preds_root.get("top")
    confidence = preds_root.get("confidence")

    # 또는 가장 첫 predictions 항목에서 class/confidence를 가져오고 싶으면:
    # inner_preds = preds_root.get("predictions", [])
    # if inner_preds:
    #     cloth_type = inner_preds[0].get("class")
    #     confidence = inner_preds[0].get("confidence")

    return cloth_type, confidence

def analyze_clothes(image_path: str):
    """
    1) 워크플로우로 옷 종류 예측
    2) 색상 분석
    """
    cloth_type, confidence = predict_clothes_type(image_path)
    color_name = get_dominate_color_name(image_path)

    return {
        "type": cloth_type,
        "type_confidence": confidence,
        "color": color_name,
    }


if __name__ == "__main__":
    # 테스트용 이미지 경로 (직접 맞게 수정해도 됨)
    test_image = "test_images/sample.jpg"
    result = analyze_clothes(test_image)
    print("최종 결과:", result)