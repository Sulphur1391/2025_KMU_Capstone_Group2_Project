import 'package:flutter/material.dart';

class OutfitResultScreen extends StatelessWidget {
  const OutfitResultScreen({super.key});

  @override
  Widget build(BuildContext context) {
    //HomeScreen에서 전달된 event (없으면 null)
    final event = ModalRoute.of(context)!.settings.arguments;

    //일정 기반인지 / 날씨 기반인지
    final bool hasEvent = event != null;

    //코디 3개 + 태그
    final List<Map<String, dynamic>> outfits = [
      {
        "name": "코디1",
        "tags": ["내옷장", "따뜻", "단정"],
      },
      {
        "name": "코디2",
        "tags": ["위시리스트", "포근", "세미포멀"],
      },
      {
        "name": "코디3",
        "tags": ["커뮤니티인기", "활동적", "방풍필수"],
      },
    ];

    //아이템 목록
    final List<Map<String, String>> wishlistItems = [
      {"name": "롱패딩", "usedIn": "코디1"},
      {"name": "머플러", "usedIn": "코디2"},
    ];

    final List<Map<String, String>> closetItems = [
      {"name": "라운드 니트", "usedIn": "코디1"},
      {"name": "울코트", "usedIn": "코디3"},
    ];

    final List<Map<String, String>> communityItems = [
      {"name": "카고팬츠", "usedIn": "코디3"},
    ];

    //컬러
    const pastelGreen = Color(0xFFA1CE5D);

    return Scaffold(
      appBar: AppBar(
        title: Text(hasEvent ? "오늘의 일정 코디" : "오늘의 날씨 코디"),
        backgroundColor: pastelGreen,
        foregroundColor: Colors.white,
        elevation: 0,
      ),

      body: SingleChildScrollView(
        // 🔥 전체 스크롤 적용
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            //일정 기반 헤더 OR 날씨 기반 헤더
            hasEvent
                ? _eventHeader(event as Map<String, dynamic>)
                : _weatherHeader(),

            const SizedBox(height: 25),

            //추천 코디 제목
            const Text(
              "추천 코디",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 15),

            //코디 3개 → 세로 스크롤 (전체 스크롤 안에서 shrinkWrap)
            ListView.separated(
              physics: const NeverScrollableScrollPhysics(), // 자체 스크롤 없음
              shrinkWrap: true,
              padding: EdgeInsets.zero,
              itemCount: outfits.length,
              separatorBuilder: (c, i) => const SizedBox(height: 18),
              itemBuilder: (context, index) {
                final outfit = outfits[index];
                return _outfitCard(outfit["name"], outfit["tags"]);
              },
            ),

            const SizedBox(height: 30),

            //위시리스트 아이템
            const Text(
              "위시리스트 아이템",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            ...wishlistItems.map(
              (i) => Text("- ${i['name']} (${i['usedIn']})"),
            ),

            const SizedBox(height: 22),

            //내 옷장
            const Text(
              "내 옷장 아이템",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            ...closetItems.map((i) => Text("- ${i['name']} (${i['usedIn']})")),

            const SizedBox(height: 22),

            //커뮤니티 아이템
            const Text(
              "커뮤니티 아이템",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            ...communityItems.map(
              (i) => Text("- ${i['name']} (${i['usedIn']})"),
            ),
          ],
        ),
      ),
    );
  }

  //일정 기반 헤더 (세로 스크롤)
  Widget _eventHeader(Map<String, dynamic> e) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          "${e["time"]}  ${e["title"]}",
          style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 10),

        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Row(
            children: [
              _tag("#${e["weather"]}"),
              const SizedBox(width: 8),
              _tag("#${e["tag"]}"),
              const SizedBox(width: 8),
              _tag("#${e["style"]} 스타일"),
            ],
          ),
        ),
      ],
    );
  }

  //날씨 기반 헤더
  Widget _weatherHeader() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: const [
        Text(
          "오늘의 날씨 기반 코디",
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        SizedBox(height: 8),
        Text("기온 5℃ / 맑음"),
      ],
    );
  }

  //코디 카드
  Widget _outfitCard(String name, List<String> tags) {
    return SizedBox(
      width: 210,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          //이미지
          Container(
            height: 220,
            decoration: BoxDecoration(
              color: Colors.grey[300],
              borderRadius: BorderRadius.circular(14),
            ),
            child: const Center(
              child: Text(
                "이미지",
                style: TextStyle(
                  fontSize: 20,
                  color: Colors.black54,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
          ),

          const SizedBox(height: 10),

          //태그
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Row(
              children: [
                ...tags.map(
                  (t) => Padding(
                    padding: const EdgeInsets.only(right: 6),
                    child: _tag("#$t"),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  //태그 스타일
  Widget _tag(String text) {
    const pastelGreen = Color(0xFFA1CE5D);
    const pastelGreenDark = Color(0xFF79B93F);

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: pastelGreen.withOpacity(0.20),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: pastelGreenDark.withOpacity(0.5), width: 1),
      ),
      child: Text(
        text,
        style: TextStyle(
          fontSize: 13,
          fontWeight: FontWeight.w600,
          color: pastelGreenDark,
        ),
      ),
    );
  }
}
