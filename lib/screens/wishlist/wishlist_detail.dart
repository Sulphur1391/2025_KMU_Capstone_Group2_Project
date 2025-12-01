import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

class WishlistDetailScreen extends StatelessWidget {
  const WishlistDetailScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> item =
        ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;

    final bool hasImage =
        item["image"] != null && (item["image"] as String).isNotEmpty;

    return Scaffold(
      appBar: AppBar(
        title: const Text("위시리스트 상세정보"),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 12),
            child: Icon(Icons.favorite, color: AppColors.wishlistDark),
          ),
        ],
      ),

      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ClipRRect(
              borderRadius: const BorderRadius.only(
                bottomLeft: Radius.circular(16),
                bottomRight: Radius.circular(16),
              ),
              child: hasImage
                  ? Image.network(
                      item["image"],
                      height: 300,
                      width: double.infinity,
                      fit: BoxFit.cover,
                      errorBuilder: (c, e, s) => _ph(),
                    )
                  : _ph(),
            ),

            const SizedBox(height: 20),

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

            const SizedBox(height: 12),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 10,
                  vertical: 5,
                ),
                decoration: BoxDecoration(
                  color: AppColors.wishlist.withOpacity(0.15),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  item["category"],
                  style: TextStyle(
                    color: AppColors.wishlistDark,
                    fontSize: 14,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ),

            const SizedBox(height: 30),

            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 20),
              child: Text(
                "상세 설명\n\n"
                "이 옷은 위시리스트에 저장된 아이템입니다.\n"
                "추후 DB 연동 시 옷의 정보가 표시됩니다.",
                style: TextStyle(fontSize: 16, height: 1.5),
              ),
            ),

            const SizedBox(height: 40),
          ],
        ),
      ),
    );
  }

  Widget _ph() {
    return Container(
      width: double.infinity,
      height: 300,
      color: Colors.grey[300],
      child: const Center(
        child: Text(
          "이미지",
          style: TextStyle(
            color: Colors.black54,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
