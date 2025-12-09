# -*- coding: utf-8 -*-
from PIL import Image
import numpy as np

# 1) 기준 색상 정의 (RGB)
#    각 색 이름을 대표하는 RGB를 하나씩 정해줍니다.
BASE_COLORS = {
    "white":  (245, 245, 245),
    "black":  (15, 15, 15),
    "pink":   (255, 182, 193),
    "navy":   (0, 0, 60),
    "red":    (220, 20, 60),
    "purple": (128, 0, 128),
    "beige":  (222, 209, 175),
    "blue":   (65, 105, 225),  # royal blue 계열
}


def _resize_image(img: Image.Image, max_size: int = 200) -> Image.Image:
    """
    너무 큰 이미지는 축소해서 연산량을 줄입니다.
    """
    w, h = img.size
    scale = min(max_size / float(max(w, h)), 1.0)
    if scale < 1.0:
        new_w = int(w * scale)
        new_h = int(h * scale)
        img = img.resize((new_w, new_h), Image.LANCZOS)
    return img


def _crop_center(img: Image.Image, ratio: float = 0.6) -> Image.Image:
    """
    이미지 중앙을 기준으로 가로/세로 비율(ratio)의 영역만 잘라냅니다.
    ratio=0.6 이면, 가로/세로 60% 크기의 중앙 박스를 사용.
    """
    w, h = img.size
    ratio = max(0.1, min(ratio, 1.0))  # 0.1 ~ 1.0 사이로 제한

    new_w = int(w * ratio)
    new_h = int(h * ratio)

    left = (w - new_w) // 2
    top = (h - new_h) // 2
    right = left + new_w
    bottom = top + new_h

    return img.crop((left, top, right, bottom))


def _rgb_distance(c1, c2):
    """
    두 RGB 색상 간의 유클리드 거리(거리가 짧을수록 비슷한 색).
    """
    return np.sqrt(
        (c1[0] - c2[0]) ** 2 +
        (c1[1] - c2[1]) ** 2 +
        (c1[2] - c2[2]) ** 2
    )


def get_dominate_color_name(image_path: str) -> str:
    """
    이미지 '중앙 부분'만 잘라 평균색을 구한 뒤,
    그 평균색에 가장 가까운 색을
    [white, black, pink, navy, red, purple, beige, blue]
    중 하나로 매핑해서 반환합니다.
    """
    try:
        img = Image.open(image_path).convert("RGB")
    except Exception as e:
        print(f"이미지 열기 실패: {e}")
        # 에러 시 기본값으로 beige 반환
        return "beige"

    # 1) 먼저 크기 축소
    img = _resize_image(img, max_size=200)
    # 2) 중앙 부분만 사용 (예: 60% 영역)
    img = _crop_center(img, ratio=0.6)

    # numpy 배열로 변환 후 평균색 계산
    arr = np.array(img)
    pixels = arr.reshape(-1, 3)
    mean_color = pixels.mean(axis=0)  # (3,) float

    # 평균색을 기준으로 가장 가까운 기준 색 찾기
    mean_rgb = (mean_color[0], mean_color[1], mean_color[2])

    best_name = None
    best_dist = float("inf")

    for name, base_rgb in BASE_COLORS.items():
        dist = _rgb_distance(mean_rgb, base_rgb)
        if dist < best_dist:
            best_dist = dist
            best_name = name

    return best_name