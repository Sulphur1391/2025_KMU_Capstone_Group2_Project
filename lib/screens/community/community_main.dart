import 'package:flutter/material.dart';
import 'package:outfithub/widgets/safe_image.dart';

class CommunityMainScreen extends StatelessWidget {
  const CommunityMainScreen({super.key});

  @override
  Widget build(BuildContext context) {
    //샘플 커뮤니티 게시글 데이터
    final List<Map<String, dynamic>> posts = [
      {
        "title": "겨울 데일리룩",
        "image": "https://via.placeholder.com/200",
        "likes": 58,
        "comments": 12,
        "user": "nnn",
      },
      {
        "title": "데이트룩",
        "image": null,
        "likes": 103,
        "comments": 27,
        "user": "zzz",
      },
      {
        "title": "스트릿 감성",
        "image": "",
        "likes": 74,
        "comments": 18,
        "user": "kkk",
      },
    ];

    return Scaffold(
      appBar: AppBar(title: const Text("커뮤니티")),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: posts.length,
        itemBuilder: (context, index) {
          final post = posts[index];

          return GestureDetector(
            onTap: () {
              ScaffoldMessenger.of(
                context,
              ).showSnackBar(const SnackBar(content: Text("상세 페이지는 병합 예정")));
            },
            child: Container(
              margin: const EdgeInsets.only(bottom: 16),
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(14),
                color: Colors.white,
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.08),
                    blurRadius: 6,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: Column(
                children: [
                  //게시글 이미지 — safeImage 적용
                  ClipRRect(
                    borderRadius: const BorderRadius.vertical(
                      top: Radius.circular(14),
                    ),
                    child: SizedBox(
                      height: 180,
                      width: double.infinity,
                      child: safeImage(post["image"], fit: BoxFit.cover),
                    ),
                  ),

                  //게시글 정보 영역
                  Padding(
                    padding: const EdgeInsets.all(12),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        //제목
                        Text(
                          post["title"],
                          style: const TextStyle(
                            fontSize: 17,
                            fontWeight: FontWeight.bold,
                          ),
                        ),

                        const SizedBox(height: 6),

                        Row(
                          children: [
                            Text(
                              "@${post["user"]}",
                              style: TextStyle(
                                color: Colors.grey.shade600,
                                fontSize: 13,
                              ),
                            ),

                            const Spacer(),

                            //좋아요
                            Row(
                              children: [
                                const Icon(
                                  Icons.favorite,
                                  color: Colors.red,
                                  size: 18,
                                ),
                                const SizedBox(width: 4),
                                Text("${post["likes"]}"),
                              ],
                            ),

                            const SizedBox(width: 14),

                            //댓글
                            Row(
                              children: [
                                Icon(
                                  Icons.chat_bubble,
                                  color: Colors.grey.shade700,
                                  size: 18,
                                ),
                                const SizedBox(width: 4),
                                Text("${post["comments"]}"),
                              ],
                            ),
                          ],
                        ),
                      ],
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
}
