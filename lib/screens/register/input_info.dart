import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

class InputInfoScreen extends StatefulWidget {
  const InputInfoScreen({super.key});

  @override
  State<InputInfoScreen> createState() => _InputInfoScreenState();
}

class _InputInfoScreenState extends State<InputInfoScreen> {
  //선택값들
  String? category;
  String? size;
  String? season;
  String? color;

  final priceController = TextEditingController();
  final linkController = TextEditingController();

  final Color lime = AppColors.primary;
  final Color limeDark = AppColors.primaryDark;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("옷 정보 입력"),
        backgroundColor: lime,
        foregroundColor: Colors.white,
      ),

      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            //카테고리 선택
            _buildDropdown(
              title: "카테고리",
              value: category,
              items: ["상의", "하의", "아우터", "신발", "악세사리"],
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
              items: ["봄", "여름", "가을", "겨울", "올시즌"],
              onChanged: (v) => setState(() => season = v),
            ),

            const SizedBox(height: 20),

            //가격
            _buildTextField("가격 (숫자만 입력)", priceController),

            const SizedBox(height: 20),

            //구매 링크
            _buildTextField("구매 링크 (선택사항)", linkController),

            const SizedBox(height: 30),

            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: limeDark,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                onPressed: () {
                  Navigator.pushNamed(context, '/register/complete');
                },
                child: const Text("저장"),
              ),
            ),
          ],
        ),
      ),
    );
  }

  //Dropdown builder
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
            border: Border.all(color: limeDark),
            borderRadius: BorderRadius.circular(8),
            color: AppColors.greyLight,
          ),
          child: DropdownButtonHideUnderline(
            child: DropdownButton<String>(
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

  // TextField builder
  Widget _buildTextField(String title, TextEditingController controller) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(title, style: const TextStyle(fontSize: 16)),
        const SizedBox(height: 8),
        TextField(
          controller: controller,
          decoration: InputDecoration(
            filled: true,
            fillColor: AppColors.greyLight,
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide(color: limeDark, width: 2),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide(color: limeDark),
            ),
          ),
        ),
      ],
    );
  }
}
