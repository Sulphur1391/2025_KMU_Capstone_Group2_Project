import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

class FilterScreen extends StatefulWidget {
  final String? season;
  final String? style;
  final String? color;

  const FilterScreen({super.key, this.season, this.style, this.color});

  @override
  State<FilterScreen> createState() => _FilterScreenState();
}

class _FilterScreenState extends State<FilterScreen> {
  final seasons = ["봄", "여름", "가을", "겨울"];
  final styles = ["캐주얼", "베이직", "따뜻", "스포티", "포멀", "스트릿", "댄디"];
  final colors = ["화이트", "블랙", "그레이", "네이비", "블루", "브라운", "핑크"];

  String? selectedSeason;
  String? selectedStyle;
  String? selectedColor;

  @override
  void initState() {
    super.initState();
    selectedSeason = widget.season;
    selectedStyle = widget.style;
    selectedColor = widget.color;
  }

  Widget buildSection(
    String title,
    List<String> values,
    String? selected,
    void Function(String?) onTap,
  ) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Align(
          alignment: Alignment.centerLeft,
          child: Text(
            title,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
          ),
        ),
        const SizedBox(height: 12),

        Align(
          alignment: Alignment.centerLeft,
          child: Wrap(
            spacing: 10,
            runSpacing: 10,
            children: values.map((value) {
              final bool isSelected = selected == value;
              return ChoiceChip(
                label: Text(
                  value,
                  style: TextStyle(
                    color: isSelected ? AppColors.primaryDark : Colors.black87,
                    fontWeight: isSelected
                        ? FontWeight.bold
                        : FontWeight.normal,
                  ),
                ),
                selected: isSelected,
                selectedColor: AppColors.primary.withOpacity(0.25),
                backgroundColor: Colors.grey.shade200,
                side: BorderSide(
                  color: isSelected
                      ? AppColors.primaryDark
                      : Colors.grey.shade300,
                  width: 1,
                ),
                onSelected: (_) => onTap(value),
              );
            }).toList(),
          ),
        ),

        const SizedBox(height: 24),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("필터 설정")),

      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            //계절
            buildSection("계절", seasons, selectedSeason, (v) {
              setState(() => selectedSeason = v);
            }),

            //스타일
            buildSection("스타일", styles, selectedStyle, (v) {
              setState(() => selectedStyle = v);
            }),

            //색상
            buildSection("색상", colors, selectedColor, (v) {
              setState(() => selectedColor = v);
            }),

            const Spacer(),

            Row(
              children: [
                //초기화
                Expanded(
                  child: OutlinedButton(
                    onPressed: () {
                      setState(() {
                        selectedSeason = null;
                        selectedStyle = null;
                        selectedColor = null;
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

                //적용하기
                Expanded(
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pop(context, {
                        "season": selectedSeason,
                        "style": selectedStyle,
                        "color": selectedColor,
                      });
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.primaryDark,
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
          ],
        ),
      ),
    );
  }
}
