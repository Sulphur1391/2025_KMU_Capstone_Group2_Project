# clothes_analyzer.py
from inference_sdk import InferenceHTTPClient
from colors import get_dominate_color_name

client = InferenceHTTPClient(
    api_url="https://serverless.roboflow.com",
    api_key="rqEUCjFbHaJ6mOCbhOx2"
)

WORKSPACE_NAME = "grfgf"
WORKFLOW_ID = "custom-workflow-3"


def predict_clothes_type(image_path: str):
    result = client.run_workflow(
        workspace_name=WORKSPACE_NAME,
        workflow_id=WORKFLOW_ID,
        images={
            "image": image_path
        },
        use_cache=True,
    )

    print("Raw workflow result:", result)

    if not isinstance(result, list) or not result:
        return None, None

    first_item = result[0]
    preds_root = first_item.get("predictions")
    if not isinstance(preds_root, dict):
        return None, None

    cloth_type = preds_root.get("top")
    confidence = preds_root.get("confidence")

    return cloth_type, confidence


def analyze_clothes(image_path: str):
    cloth_type, confidence = predict_clothes_type(image_path)
    color_name = get_dominate_color_name(image_path)

    return {
        "type": cloth_type,
        "type_confidence": confidence,
        "color": color_name,
    }