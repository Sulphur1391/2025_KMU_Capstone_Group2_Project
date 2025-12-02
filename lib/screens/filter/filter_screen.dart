import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

class FilterScreen extends StatefulWidget {
  final String? category;
  final String? type;
  final String? season;
  final String? style;
  final String? color;
  final String? material;

  const FilterScreen({
    super.key,
    this.category,
    this.type,
    this.season,
    this.style,
    this.color,
    this.material,
  });

  @override
  State<FilterScreen> createState() => _FilterScreenState();
}

class _FilterScreenState extends State<FilterScreen> {
  //필터 세부 항목
  final categories = ["상의", "하의", "아우터", "원피스", "신발"];

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

  final seasonList = ["봄", "여름", "가을", "겨울", "사계절"];

  final styleList = ["포멀", "캐주얼", "데일리", "스트릿", "러블리", "미니멀"];

  final colorList = ["화이트", "블랙", "블루", "네이비", "핑크", "레드", "퍼플", "베이지"];

  final materialList = ["면", "니트", "데님", "폴리", "린넨", "패딩", "스웨이드", "레더"];

  String? selectedCategory;
  String? selectedType;
  String? selectedSeason;
  String? selectedStyle;
  String? selectedColor;
  String? selectedMaterial;

  @override
  void initState() {
    super.initState();
    selectedCategory = widget.category;
    selectedType = widget.type;
    selectedSeason = widget.season;
    selectedStyle = widget.style;
    selectedColor = widget.color;
    selectedMaterial = widget.material;
  }

  // --------------------------
  Widget unifiedChip(
    String value,
    String? selected,
    void Function(String?) onSelect,
  ) {
    final bool isSelected = selected == value;

    return InkWell(
      onTap: () => onSelect(value),
      borderRadius: BorderRadius.circular(14),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
        decoration: BoxDecoration(
          color: isSelected
              ? AppColors.primary.withOpacity(0.25)
              : Colors.grey.shade200,
          borderRadius: BorderRadius.circular(14),
          border: Border.all(
            color: isSelected ? AppColors.primaryDark : Colors.grey.shade300,
          ),
        ),
        child: Text(
          value,
          style: TextStyle(
            fontSize: 13,
            fontWeight: isSelected ? FontWeight.bold : FontWeight.w400,
            color: isSelected ? AppColors.primaryDark : Colors.black87,
          ),
        ),
      ),
    );
  }

  //Chip UI
  Widget buildSection(
    String title,
    List<String> values,
    String? selected,
    void Function(String?) onSelect,
  ) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(bottom: 12, top: 28),
          child: Text(
            title,
            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w700),
          ),
        ),

        Align(
          alignment: Alignment.centerLeft,
          child: Wrap(
            alignment: WrapAlignment.start,
            crossAxisAlignment: WrapCrossAlignment.start,
            spacing: 8,
            runSpacing: 10,
            children: values
                .map((value) => unifiedChip(value, selected, onSelect))
                .toList(),
          ),
        ),
      ],
    );
  }

  //전체 화면
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("필터 설정"),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
      ),

      body: Padding(
        padding: const EdgeInsets.all(20),
        child: SingleChildScrollView(
          child: Column(
            children: [
              buildSection(
                "계절",
                seasonList,
                selectedSeason,
                (v) => setState(() => selectedSeason = v),
              ),

              buildSection(
                "스타일",
                styleList,
                selectedStyle,
                (v) => setState(() => selectedStyle = v),
              ),

              buildSection(
                "옷 종류",
                typeList,
                selectedType,
                (v) => setState(() => selectedType = v),
              ),

              buildSection(
                "색상",
                colorList,
                selectedColor,
                (v) => setState(() => selectedColor = v),
              ),

              buildSection(
                "재질",
                materialList,
                selectedMaterial,
                (v) => setState(() => selectedMaterial = v),
              ),

              const SizedBox(height: 40),

              Row(
                children: [
                  //초기화 버튼
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () {
                        setState(() {
                          selectedCategory = null;
                          selectedType = null;
                          selectedSeason = null;
                          selectedStyle = null;
                          selectedColor = null;
                          selectedMaterial = null;
                        });
                      },
                      style: OutlinedButton.styleFrom(
                        side: BorderSide(
                          color: AppColors.primaryDark,
                          width: 1.5,
                        ),
                        foregroundColor: AppColors.primaryDark,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                      ),
                      child: const Text(
                        "초기화",
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                  ),

                  const SizedBox(width: 12),

                  //적용하기 버튼
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () {
                        Navigator.pop(context, {
                          "category": selectedCategory,
                          "type": selectedType,
                          "season": selectedSeason,
                          "style": selectedStyle,
                          "color": selectedColor,
                          "material": selectedMaterial,
                        });
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppColors.primary,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                      child: const Text(
                        "적용하기",
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                ],
              ),

              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}
