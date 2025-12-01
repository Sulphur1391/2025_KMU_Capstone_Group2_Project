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

  String? category;
  String? size;
  String? season;
  String? color;

  final linkController = TextEditingController();
  final picker = ImagePicker();

  //이미지 선택 함수
  Future<void> pickImage() async {
    final picked = await picker.pickImage(source: ImageSource.gallery);
    if (picked != null) {
      setState(() {
        selectedImage = picked;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("옷 등록")),

      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            //이미지 미리보기
            Container(
              height: 200,
              width: double.infinity,
              decoration: BoxDecoration(
                color: Colors.grey.shade200,
                borderRadius: BorderRadius.circular(12),
              ),
              child: selectedImage == null
                  ? const Center(
                      child: Text(
                        "사진을 선택해주세요",
                        style: TextStyle(color: Colors.grey),
                      ),
                    )
                  : ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: Image.file(
                        File(selectedImage!.path),
                        fit: BoxFit.cover,
                      ),
                    ),
            ),

            const SizedBox(height: 12),

            //사진 선택 버튼
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: pickImage,
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.primaryDark,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                ),
                child: const Text("사진 선택하기"),
              ),
            ),

            const SizedBox(height: 20),

            //카테고리
            _buildDropdown(
              title: "카테고리",
              value: category,
              items: ["상의", "하의", "아우터", "신발"],
              onChanged: (v) => setState(() => category = v),
            ),
            const SizedBox(height: 16),

            //사이즈
            _buildDropdown(
              title: "사이즈",
              value: size,
              items: ["Free", "S", "M", "L", "XL"],
              onChanged: (v) => setState(() => size = v),
            ),
            const SizedBox(height: 16),

            //색상
            _buildDropdown(
              title: "색상",
              value: color,
              items: ["블랙", "화이트", "그레이", "네이비", "베이지"],
              onChanged: (v) => setState(() => color = v),
            ),
            const SizedBox(height: 16),

            //계절
            _buildDropdown(
              title: "계절",
              value: season,
              items: ["봄", "여름", "가을", "겨울", "사계절"],
              onChanged: (v) => setState(() => season = v),
            ),

            const SizedBox(height: 20),

            //구매 링크
            _buildTextField("구매 링크 (선택)", linkController),

            const SizedBox(height: 30),

            //등록 완료 버튼
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.primaryDark,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                ),
                onPressed: () {
                  if (selectedImage == null) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("사진을 선택해주세요.")),
                    );
                    return;
                  }

                  Navigator.pop(context);
                },
                child: const Text(
                  "등록 완료",
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  //공통 Dropdown UI
  Widget _buildDropdown({
    required String title,
    required String? value,
    required List<String> items,
    required Function(String?) onChanged,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(title, style: const TextStyle(fontSize: 16)),
        const SizedBox(height: 8),
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 12),
          decoration: BoxDecoration(
            border: Border.all(color: Colors.grey.shade300),
            borderRadius: BorderRadius.circular(8),
          ),
          child: DropdownButtonHideUnderline(
            child: DropdownButton(
              value: value,
              hint: Text("$title 선택"),
              items: items
                  .map((e) => DropdownMenuItem(value: e, child: Text(e)))
                  .toList(),
              onChanged: onChanged,
            ),
          ),
        ),
      ],
    );
  }

  //공통 TextField UI
  Widget _buildTextField(String title, TextEditingController controller) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(title, style: const TextStyle(fontSize: 16)),
        const SizedBox(height: 8),
        TextField(
          controller: controller,
          decoration: InputDecoration(
            border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
            hintText: title,
          ),
        ),
      ],
    );
  }
}
