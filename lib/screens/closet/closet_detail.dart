import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:outfithub/theme/app_color.dart';

class ClosetDetailScreen extends StatefulWidget {
  const ClosetDetailScreen({super.key});

  @override
  State<ClosetDetailScreen> createState() => _ClosetDetailScreenState();
}

class _ClosetDetailScreenState extends State<ClosetDetailScreen> {
  late Map<String, dynamic> item;

  //위시리스트 여부
  bool isWishlisted = false;

  //필터 Dropdown 값
  String? selectedSeason;
  String? selectedStyle;
  String? selectedColor;

  File? pickedImage;

  final seasons = ["봄", "여름", "가을", "겨울"];
  final styles = ["캐주얼", "베이직", "따뜻", "스포티"];
  final colors = ["화이트", "블랙", "블루"];

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    item = ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;

    //기본값 적용
    isWishlisted = item["wish"] == true;
    selectedSeason = item["season"];
    selectedStyle = item["style"];
    selectedColor = item["color"];
  }

  Future<void> pickImage() async {
    final picker = ImagePicker();
    final XFile? file = await picker.pickImage(source: ImageSource.gallery);

    if (file != null) {
      setState(() {
        pickedImage = File(file.path);
        item["image"] = file.path;
      });
    }
  }

  Widget _placeholderImage() {
    return Container(
      color: Colors.grey[300],
      width: double.infinity,
      height: 280,
      child: const Center(
        child: Text(
          "이미지 없음",
          style: TextStyle(color: Colors.black54, fontSize: 16),
        ),
      ),
    );
  }

  Future<void> launchBuyLink(String url) async {
    final uri = Uri.parse(url);
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri, mode: LaunchMode.externalApplication);
    }
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        Navigator.pop(context, item);
        return false;
      },
      child: Scaffold(
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back),
            onPressed: () => Navigator.pop(context, item),
          ),
          title: Text(item["name"]),
          backgroundColor: AppColors.primary,
          foregroundColor: Colors.white,

          actions: [
            IconButton(
              icon: Icon(
                isWishlisted ? Icons.favorite : Icons.favorite_border,
                color: isWishlisted ? Colors.red : Colors.white,
                size: 28,
              ),
              onPressed: () {
                setState(() {
                  isWishlisted = !isWishlisted;
                  item["wish"] = isWishlisted;
                });

                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(
                      isWishlisted ? "위시리스트에 추가 되었습니다." : "위시리스트에서 삭제 되었습니다.",
                    ),
                  ),
                );
              },
            ),
          ],
        ),

        body: SingleChildScrollView(
          padding: const EdgeInsets.only(bottom: 40),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              //이미지 영역
              Stack(
                children: [
                  SizedBox(
                    width: double.infinity,
                    height: 280,
                    child: pickedImage != null
                        ? Image.file(pickedImage!, fit: BoxFit.cover)
                        : (item["image"] != null &&
                              item["image"].toString().isNotEmpty &&
                              !item["image"].toString().startsWith('/'))
                        ? Image.network(
                            item["image"],
                            fit: BoxFit.cover,
                            errorBuilder: (c, e, s) => _placeholderImage(),
                          )
                        : (item["image"] != null &&
                              item["image"].toString().isNotEmpty)
                        ? Image.file(File(item["image"]), fit: BoxFit.cover)
                        : _placeholderImage(),
                  ),

                  Positioned(
                    right: 12,
                    bottom: 12,
                    child: ElevatedButton.icon(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.white70,
                        foregroundColor: Colors.black,
                      ),
                      onPressed: pickImage,
                      icon: const Icon(Icons.photo),
                      label: const Text("사진 변경"),
                    ),
                  ),
                ],
              ),

              const SizedBox(height: 20),

              //이름
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Text(
                  item["name"],
                  style: const TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),

              const SizedBox(height: 8),

              //카테고리 칩
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 10,
                    vertical: 5,
                  ),
                  decoration: BoxDecoration(
                    color: AppColors.primaryLight,
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Text(
                    item["category"],
                    style: TextStyle(
                      color: AppColors.primaryDark,
                      fontSize: 14,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ),
              ),

              const SizedBox(height: 20),

              //필터 (계절/스타일/색상)
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text("계절", style: TextStyle(fontSize: 16)),
                    DropdownButton<String>(
                      value: selectedSeason,
                      isExpanded: true,
                      items: seasons
                          .map(
                            (e) => DropdownMenuItem(value: e, child: Text(e)),
                          )
                          .toList(),
                      onChanged: (v) => setState(() => selectedSeason = v),
                    ),
                    const SizedBox(height: 12),

                    const Text("스타일", style: TextStyle(fontSize: 16)),
                    DropdownButton<String>(
                      value: selectedStyle,
                      isExpanded: true,
                      items: styles
                          .map(
                            (e) => DropdownMenuItem(value: e, child: Text(e)),
                          )
                          .toList(),
                      onChanged: (v) => setState(() => selectedStyle = v),
                    ),
                    const SizedBox(height: 12),

                    const Text("색상", style: TextStyle(fontSize: 16)),
                    DropdownButton<String>(
                      value: selectedColor,
                      isExpanded: true,
                      items: colors
                          .map(
                            (e) => DropdownMenuItem(value: e, child: Text(e)),
                          )
                          .toList(),
                      onChanged: (v) => setState(() => selectedColor = v),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 20),

              //필터 저장 버튼
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppColors.primaryDark,
                    foregroundColor: Colors.white,
                    padding: const EdgeInsets.symmetric(vertical: 14),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  onPressed: () {
                    item["season"] = selectedSeason;
                    item["style"] = selectedStyle;
                    item["color"] = selectedColor;

                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("필터가 저장되었습니다.")),
                    );
                  },
                  child: const Text(
                    "필터 저장",
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                ),
              ),

              const SizedBox(height: 30),

              //구매 링크
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.primary,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    onPressed: () {
                      final url = item["buyLink"] ?? "";
                      if (url.isEmpty) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text("구매 링크가 없습니다.")),
                        );
                        return;
                      }
                      launchBuyLink(url);
                    },
                    child: const Text(
                      "구매 링크 열기",
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
