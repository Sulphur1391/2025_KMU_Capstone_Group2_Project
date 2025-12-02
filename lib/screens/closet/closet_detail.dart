import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';
import 'package:outfithub/widgets/safe_image.dart';

class ClosetDetailScreen extends StatefulWidget {
  const ClosetDetailScreen({super.key});

  @override
  State<ClosetDetailScreen> createState() => _ClosetDetailScreenState();
}

class _ClosetDetailScreenState extends State<ClosetDetailScreen> {
  late Map<String, dynamic> item;

  //필터값
  String? selectedSeason;
  String? selectedStyle;
  String? selectedType;
  String? selectedColor;
  String? selectedMaterial;

  //필터 세부 항목
  final seasonList = ["봄", "여름", "가을", "겨울", "사계절"];
  final styleList = ["포멀", "캐주얼", "데일리", "스트릿", "러블리", "미니멀"];
  final typeList = [
    "셔츠",
    "티셔츠",
    "맨투맨",
    "후드",
    "바지",
    "치마",
    "반바지",
    "패딩",
    "자켓",
    "원피스",
    "스니커즈",
    "구두",
    "부츠",
  ];
  final colorList = ["화이트", "블랙", "블루", "네이비", "핑크", "레드", "퍼플", "베이지"];
  final materialList = ["면", "니트", "데님", "폴리", "린넨", "패딩", "스웨이드", "레더"];

  TextEditingController nameController = TextEditingController();

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    item = ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;

    nameController.text = item["name"];

    selectedSeason = item["season"];
    selectedStyle = item["style"];
    selectedType = item["type"];
    selectedColor = item["color"];
    selectedMaterial = item["material"];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: AppBar(
        title: Text(item["name"]),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
        elevation: 0,
        actions: [
          IconButton(icon: const Icon(Icons.favorite_border), onPressed: () {}),
        ],
      ),

      body: SingleChildScrollView(
        padding: const EdgeInsets.all(18),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            //------------------ 이미지 영역 ------------------
            Stack(
              children: [
                Container(
                  height: 260,
                  width: double.infinity,
                  decoration: BoxDecoration(
                    color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: item["image"] == ""
                      ? const Center(
                          child: Text(
                            "이미지 없음",
                            style: TextStyle(color: Colors.black54),
                          ),
                        )
                      : ClipRRect(
                          borderRadius: BorderRadius.circular(14),
                          child: safeImage(item["image"], fit: BoxFit.cover),
                        ),
                ),

                Positioned(
                  right: 12,
                  bottom: 12,
                  child: ElevatedButton(
                    onPressed: () {},
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.primary,
                      padding: const EdgeInsets.symmetric(
                        horizontal: 16,
                        vertical: 6,
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                      ),
                      elevation: 2,
                    ),
                    child: const Text("사진 변경", style: TextStyle(fontSize: 13)),
                  ),
                ),
              ],
            ),

            const SizedBox(height: 28),

            //------------------ 이름 영역 ------------------
            Text(
              nameController.text,
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),

            const SizedBox(height: 18),

            const Text("옷 이름(별명) 수정"),
            const SizedBox(height: 6),

            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14),
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.circular(12),
              ),
              child: TextField(
                controller: nameController,
                decoration: const InputDecoration(border: InputBorder.none),
                onChanged: (v) => setState(() {}),
              ),
            ),

            const SizedBox(height: 22),

            //------------------ 카테고리 태그 ------------------
            Container(
              padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 12),
              decoration: BoxDecoration(
                color: AppColors.primary.withOpacity(0.3),
                borderRadius: BorderRadius.circular(20),
              ),
              child: Text(
                item["category"],
                style: TextStyle(fontSize: 14, color: AppColors.primaryDark),
              ),
            ),

            const SizedBox(height: 30),

            //------------------ 필터 ------------------
            _chipSection(
              "계절",
              seasonList,
              selectedSeason,
              (v) => setState(() => selectedSeason = v),
            ),

            _chipSection(
              "스타일",
              styleList,
              selectedStyle,
              (v) => setState(() => selectedStyle = v),
            ),

            _chipSection(
              "옷 종류",
              typeList,
              selectedType,
              (v) => setState(() => selectedType = v),
            ),

            _chipSection(
              "색상",
              colorList,
              selectedColor,
              (v) => setState(() => selectedColor = v),
            ),

            _chipSection(
              "재질",
              materialList,
              selectedMaterial,
              (v) => setState(() => selectedMaterial = v),
            ),

            const SizedBox(height: 30),

            //------------------ 하단 버튼 ------------------
            Row(
              children: [
                Expanded(
                  child: ElevatedButton(
                    onPressed: _confirmDelete,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.primary,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: const Text("삭제", style: TextStyle(fontSize: 16)),
                  ),
                ),
                const SizedBox(width: 12),

                //저장
                Expanded(
                  child: ElevatedButton(
                    onPressed: () => _saveItem(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.primary,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: const Text("저장", style: TextStyle(fontSize: 16)),
                  ),
                ),
                const SizedBox(width: 12),

                Expanded(
                  child: ElevatedButton(
                    onPressed: () {},
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.primary,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: const Text("구매링크", style: TextStyle(fontSize: 16)),
                  ),
                ),
              ],
            ),

            const SizedBox(height: 40),
          ],
        ),
      ),
    );
  }

  //Chip UI
  Widget _chipSection(
    String title,
    List<String> items,
    String? selectedValue,
    Function(String?) onSelect,
  ) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SizedBox(height: 20),
        Text(
          title,
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        ),
        const SizedBox(height: 12),

        Wrap(
          spacing: 8,
          runSpacing: 12,
          children: items.map((value) {
            final isSelected = selectedValue == value;

            return InkWell(
              onTap: () => onSelect(value),
              borderRadius: BorderRadius.circular(14),
              child: Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 10,
                  vertical: 6,
                ),
                decoration: BoxDecoration(
                  color: isSelected
                      ? AppColors.primary.withOpacity(0.25)
                      : Colors.grey.shade200,
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(
                    color: isSelected
                        ? AppColors.primaryDark
                        : Colors.grey.shade300,
                  ),
                ),
                child: Text(
                  value,
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: isSelected ? FontWeight.bold : FontWeight.w400,
                    color: isSelected ? AppColors.primaryDark : Colors.black87,
                  ),
                ),
              ),
            );
          }).toList(),
        ),
      ],
    );
  }

  //저장
  void _saveItem(BuildContext context) {
    item["name"] = nameController.text;
    item["season"] = selectedSeason;
    item["style"] = selectedStyle;
    item["type"] = selectedType;
    item["color"] = selectedColor;
    item["material"] = selectedMaterial;

    Navigator.pop(context, item);
  }

  //삭제
  void _confirmDelete() {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text("삭제하시겠습니까?"),
        content: const Text("옷장에서 이 아이템을 삭제합니다."),
        actions: [
          TextButton(
            child: const Text("취소"),
            onPressed: () => Navigator.pop(context),
          ),
          TextButton(
            child: const Text("삭제", style: TextStyle(color: Colors.red)),
            onPressed: () {
              Navigator.pop(context);
              Navigator.pop(context, {"delete": true, "item": item});
            },
          ),
        ],
      ),
    );
  }
}
