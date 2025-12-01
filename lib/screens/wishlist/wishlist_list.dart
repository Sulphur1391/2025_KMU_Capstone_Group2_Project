import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';
import 'package:outfithub/widgets/safe_image.dart';

class WishlistListScreen extends StatefulWidget {
  const WishlistListScreen({super.key});

  @override
  State<WishlistListScreen> createState() => _WishlistListScreenState();
}

class _WishlistListScreenState extends State<WishlistListScreen> {
  List<Map<String, dynamic>> wishlist = [
    {
      "name": "숏패딩",
      "image": "",
      "category": "아우터",
      "season": null,
      "style": null,
      "color": null,
    },
    {
      "name": "슬랙스",
      "image": null,
      "category": "하의",
      "season": null,
      "style": null,
      "color": null,
    },
    {
      "name": "후드티",
      "image": "https://via.placeholder.com/150",
      "category": "상의",
      "season": null,
      "style": null,
      "color": null,
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(12),
      child: GridView.builder(
        itemCount: wishlist.length,
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          mainAxisSpacing: 12,
          crossAxisSpacing: 12,
          childAspectRatio: 0.75,
        ),
        itemBuilder: (context, index) {
          final item = wishlist[index];
          final hasImage =
              item["image"] != null && (item["image"] as String).isNotEmpty;

          return GestureDetector(
            onTap: () async {
              final result = await Navigator.pushNamed(
                context,
                "/wishlist/detail",
                arguments: item,
              );

              if (result != null) {
                final updated = result as Map<String, dynamic>;
                final idx = wishlist.indexWhere(
                  (i) => i["name"] == updated["name"],
                );
                if (idx != -1) setState(() => wishlist[idx] = updated);
              }
            },
            child: Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: Colors.grey.withOpacity(0.25),
                    blurRadius: 6,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),

              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Stack(
                    children: [
                      ClipRRect(
                        borderRadius: const BorderRadius.vertical(
                          top: Radius.circular(12),
                        ),
                        child: SizedBox(
                          height: 140,
                          width: double.infinity,
                          child: hasImage
                              ? safeImage(item["image"], fit: BoxFit.cover)
                              : _placeholder(),
                        ),
                      ),

                      Positioned(
                        right: 8,
                        top: 8,
                        child: Container(
                          padding: const EdgeInsets.all(6),
                          decoration: BoxDecoration(
                            color: Colors.white.withOpacity(0.9),
                            shape: BoxShape.circle,
                          ),
                          child: const Icon(
                            Icons.favorite,
                            color: Colors.red,
                            size: 20,
                          ),
                        ),
                      ),
                    ],
                  ),

                  Padding(
                    padding: const EdgeInsets.only(
                      left: 10,
                      top: 12,
                      right: 10,
                    ),
                    child: Text(
                      item["name"],
                      style: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                        color: Colors.black,
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),

                  //카테고리 칩
                  Padding(
                    padding: const EdgeInsets.only(left: 10, top: 6),
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 4,
                      ),
                      decoration: BoxDecoration(
                        color: AppColors.wishlist.withOpacity(0.15),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Text(
                        item["category"],
                        style: TextStyle(
                          fontSize: 12,
                          color: AppColors.wishlistDark,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _placeholder() {
    return Container(
      color: Colors.grey[300],
      child: const Center(
        child: Text(
          "이미지",
          style: TextStyle(color: Colors.black54, fontWeight: FontWeight.w600),
        ),
      ),
    );
  }
}
