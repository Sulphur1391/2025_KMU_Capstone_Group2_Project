import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:outfithub/theme/app_color.dart';

class AddItemScreen extends StatefulWidget {
  const AddItemScreen({super.key});

  @override
  State<AddItemScreen> createState() => _AddItemScreenState();
}

class _AddItemScreenState extends State<AddItemScreen> {
  XFile? selectedImage;
  final picker = ImagePicker();

  final TextEditingController nameController = TextEditingController();
  final TextEditingController linkController = TextEditingController();

  //필터값
  String? selectedCategory;
  String? selectedType;
  String? selectedSeason;
  String? selectedStyle;
  String? selectedColor;
  String? selectedMaterial;

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

  //이미지 선택 함수
  Future<void> pickImage() async {
    final picked = await picker.pickImage(source: ImageSource.gallery);
    if (picked != null) setState(() => selectedImage = picked);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("옷 등록하기"),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
        elevation: 0,
      ),

      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ---------------------- 이미지 영역 ----------------------
            Container(
              height: 220,
              width: double.infinity,
              decoration: BoxDecoration(
                color: Colors.grey[300],
                borderRadius: BorderRadius.circular(16),
              ),
              child: selectedImage == null
                  ? const Center(
                      child: Text(
                        "사진을 선택해주세요",
                        style: TextStyle(color: Colors.black54),
                      ),
                    )
                  : ClipRRect(
                      borderRadius: BorderRadius.circular(16),
                      child: Image.file(
                        File(selectedImage!.path),
                        fit: BoxFit.cover,
                      ),
                    ),
            ),

            const SizedBox(height: 12),

            ElevatedButton(
              onPressed: pickImage,
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.primary,
                minimumSize: const Size(double.infinity, 48),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              child: const Text("사진 선택하기"),
            ),

            const SizedBox(height: 30),

            //옷 이름
            const Text(
              "옷 이름 (별명)",
              style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
            ),

            const SizedBox(height: 8),

            TextField(
              controller: nameController,
              decoration: InputDecoration(
                hintText: "예: 데일리 셔츠 / 검정 후드 / 하객룩 원피스",
                filled: true,
                fillColor: Colors.grey[100],
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
            ),

            const SizedBox(height: 30),

            //---------------------- 필터 섹션 ----------------------
            _chipSection(
              "계절",
              seasonList,
              selectedSeason,
              (v) => selectedSeason = v,
            ),

            _chipSection(
              "스타일",
              styleList,
              selectedStyle,
              (v) => selectedStyle = v,
            ),

            _chipSection(
              "옷 종류",
              typeList,
              selectedType,
              (v) => selectedType = v,
            ),

            _chipSection(
              "색상",
              colorList,
              selectedColor,
              (v) => selectedColor = v,
            ),

            _chipSection(
              "재질",
              materialList,
              selectedMaterial,
              (v) => selectedMaterial = v,
            ),

            const SizedBox(height: 20),

            //구매 링크
            const Text(
              "구매 링크 (선택)",
              style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),

            TextField(
              controller: linkController,
              decoration: InputDecoration(
                hintText: "URL 입력",
                filled: true,
                fillColor: Colors.grey[100],
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
            ),

            const SizedBox(height: 30),

            //등록 버튼
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: _submit,
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.primary,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
                child: const Text("등록 하기", style: TextStyle(fontSize: 16)),
              ),
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
    List<String> list,
    String? selectedValue,
    Function(String?) onSelect,
  ) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          title,
          style: const TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 12),

        Wrap(
          spacing: 10,
          runSpacing: 14,
          children: list.map((value) {
            final bool isSelected = selectedValue == value;

            return InkWell(
              onTap: () {
                onSelect(value);
                setState(() {});
              },
              borderRadius: BorderRadius.circular(10),
              child: Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 14,
                  vertical: 10,
                ),
                decoration: BoxDecoration(
                  color: isSelected
                      ? AppColors.primary.withOpacity(0.25)
                      : Colors.grey.shade200,
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(
                    color: isSelected
                        ? AppColors.primaryDark
                        : Colors.grey.shade300,
                    width: 1,
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

        const SizedBox(height: 26),
      ],
    );
  }

  //등록 처리
  void _submit() {
    if (selectedImage == null) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text("사진을 선택해주세요.")));
      return;
    }

    Navigator.pop(context, {
      "image": selectedImage!.path,
      "name": nameController.text,
      "category": selectedCategory,
      "type": selectedType,
      "season": selectedSeason,
      "style": selectedStyle,
      "color": selectedColor,
      "material": selectedMaterial,
      "link": linkController.text,
    });
  }
}
