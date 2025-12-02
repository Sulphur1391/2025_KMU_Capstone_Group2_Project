import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';
import 'package:outfithub/widgets/safe_image.dart';
import 'package:outfithub/screens/wishlist/wishlist_list.dart';

class ClosetListScreen extends StatefulWidget {
  const ClosetListScreen({super.key});

  @override
  State<ClosetListScreen> createState() => _ClosetListScreenState();
}

class _ClosetListScreenState extends State<ClosetListScreen>
    with SingleTickerProviderStateMixin {
  //---------------------- 더미 데이터 ----------------------
  final List<Map<String, dynamic>> items = [
    //상의
    {
      "category": "상의",
      "type": "셔츠",
      "name": "깔끔한 기본 셔츠",
      "season": "봄",
      "style": "포멀",
      "color": "화이트",
      "material": "면",
      "image": "",
    },
    {
      "category": "상의",
      "type": "티셔츠",
      "name": "얇은 이너 티셔츠",
      "season": "여름",
      "style": "캐주얼",
      "color": "카키",
      "material": "면",
      "image": "",
    },
    {
      "category": "상의",
      "type": "맨투맨",
      "name": "오버핏 맨투맨",
      "season": "가을",
      "style": "데일리",
      "color": "그레이",
      "material": "니트",
      "image": "",
    },
    {
      "category": "상의",
      "type": "후드",
      "name": "두꺼운 후드티",
      "season": "겨울",
      "style": "스트릿",
      "color": "네이비",
      "material": "면",
      "image": "",
    },

    //하의
    {
      "category": "하의",
      "type": "바지",
      "name": "슬림핏 청바지",
      "season": "사계절",
      "style": "캐주얼",
      "color": "블루",
      "material": "데님",
      "image": "",
    },
    {
      "category": "하의",
      "type": "치마",
      "name": "미니 스커트",
      "season": "여름",
      "style": "미니멀",
      "color": "블랙",
      "material": "폴리",
      "image": "",
    },
    {
      "category": "하의",
      "type": "치마",
      "name": "플레어 롱 스커트",
      "season": "봄",
      "style": "러블리",
      "color": "베이지",
      "material": "린넨",
      "image": "",
    },
    {
      "category": "하의",
      "type": "반바지",
      "name": "시원한 반바지",
      "season": "여름",
      "style": "캐주얼",
      "color": "베이지",
      "material": "면",
      "image": "",
    },

    //아우터
    {
      "category": "아우터",
      "type": "패딩",
      "name": "겨울 패딩",
      "season": "겨울",
      "style": "데일리",
      "color": "블랙",
      "material": "패딩",
      "image": "",
    },
    {
      "category": "아우터",
      "type": "자켓",
      "name": "깔쌈한 자켓",
      "season": "봄",
      "style": "포멀",
      "color": "블랙",
      "material": "폴리",
      "image": "",
    },

    //원피스
    {
      "category": "원피스",
      "type": "원피스",
      "name": "플라워 원피스",
      "season": "여름",
      "style": "러블리",
      "color": "핑크",
      "material": "폴리",
      "image": "",
    },
    {
      "category": "원피스",
      "type": "원피스",
      "name": "슬림 원피스",
      "season": "가을",
      "style": "포멀",
      "color": "블랙",
      "material": "니트",
      "image": "",
    },

    //신발
    {
      "category": "신발",
      "type": "스니커즈",
      "name": "포인트 스니커즈",
      "season": "사계절",
      "style": "캐주얼",
      "color": "레드",
      "material": "레더",
      "image": "",
    },
    {
      "category": "신발",
      "type": "구두",
      "name": "하이힐 구두",
      "season": "봄",
      "style": "포멀",
      "color": "퍼플",
      "material": "스웨이드",
      "image": "",
    },
    {
      "category": "신발",
      "type": "부츠",
      "name": "블랙 앵클부츠",
      "season": "겨울",
      "style": "미니멀",
      "color": "블랙",
      "material": "레더",
      "image": "",
    },
  ];

  //필터값
  String? filterSeason;
  String? filterStyle;
  String? filterColor;

  late TabController _tabController;

  final List<String> tabs = ["전체", "상의", "하의", "아우터", "원피스", "신발", "위시"];

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: tabs.length, vsync: this);
  }

  //필터 태그
  List<Widget> _buildFilterTags() {
    List<Widget> tags = [];

    if (filterSeason != null) {
      tags.add(_tag(filterSeason!, () => setState(() => filterSeason = null)));
    }
    if (filterStyle != null) {
      tags.add(_tag(filterStyle!, () => setState(() => filterStyle = null)));
    }
    if (filterColor != null) {
      tags.add(_tag(filterColor!, () => setState(() => filterColor = null)));
    }

    if (tags.isEmpty) return [];

    tags.add(
      GestureDetector(
        onTap: () {
          setState(() {
            filterSeason = null;
            filterStyle = null;
            filterColor = null;
          });
        },
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
          decoration: BoxDecoration(
            color: Colors.grey[200],
            borderRadius: BorderRadius.circular(20),
          ),
          child: const Text("전체 초기화", style: TextStyle(fontSize: 12)),
        ),
      ),
    );

    return tags;
  }

  Widget _tag(String label, VoidCallback onRemove) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      margin: const EdgeInsets.only(right: 6),
      decoration: BoxDecoration(
        color: AppColors.primary.withOpacity(0.25),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(
          color: AppColors.primaryDark.withOpacity(0.4),
          width: 1,
        ),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(label, style: const TextStyle(fontSize: 12)),
          const SizedBox(width: 4),
          GestureDetector(
            onTap: onRemove,
            child: const Icon(Icons.close, size: 16),
          ),
        ],
      ),
    );
  }

  //---------------------- 화면 ----------------------
  @override
  Widget build(BuildContext context) {
    final filterTags = _buildFilterTags();

    return Scaffold(
      appBar: AppBar(
        title: const Text("옷장"),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.filter_alt),
            onPressed: () async {
              final result = await Navigator.pushNamed(
                context,
                '/filter',
                arguments: {
                  "season": filterSeason,
                  "style": filterStyle,
                  "color": filterColor,
                },
              );

              if (result != null) {
                final r = result as Map<String, dynamic>;
                setState(() {
                  filterSeason = r["season"];
                  filterStyle = r["style"];
                  filterColor = r["color"];
                });
              }
            },
          ),
        ],
      ),

      body: Column(
        children: [
          if (filterTags.isNotEmpty)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(children: filterTags),
              ),
            ),

          //TabBar
          Container(
            width: double.infinity,
            color: AppColors.primary,
            padding: const EdgeInsets.only(left: 12),
            child: Row(
              children: [
                Expanded(
                  child: TabBar(
                    controller: _tabController,
                    isScrollable: true,

                    padding: EdgeInsets.zero,
                    tabAlignment: TabAlignment.start,
                    labelPadding: const EdgeInsets.only(right: 20),

                    indicatorColor: Colors.white,
                    labelColor: Colors.white,
                    unselectedLabelColor: Colors.white70,
                    tabs: tabs.map((e) => Tab(text: e)).toList(),
                  ),
                ),
              ],
            ),
          ),

          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: tabs.map((tabName) {
                if (tabName == "위시") {
                  return const WishlistListScreen();
                }
                return _buildCategoryView(tabName);
              }).toList(),
            ),
          ),
        ],
      ),
    );
  }

  //카테고리별 렌더링
  Widget _buildCategoryView(String category) {
    var filtered = category == "전체"
        ? items
        : items.where((i) => i["category"] == category).toList();

    if (filterSeason != null) {
      filtered = filtered.where((i) => i["season"] == filterSeason).toList();
    }
    if (filterStyle != null) {
      filtered = filtered.where((i) => i["style"] == filterStyle).toList();
    }
    if (filterColor != null) {
      filtered = filtered.where((i) => i["color"] == filterColor).toList();
    }

    return GridView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: filtered.length,
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        mainAxisExtent: 210,
        crossAxisSpacing: 12,
        mainAxisSpacing: 12,
      ),
      itemBuilder: (context, index) {
        final item = filtered[index];

        return GestureDetector(
          onTap: () async {
            final result = await Navigator.pushNamed(
              context,
              '/closet/detail',
              arguments: item,
            );

            if (result != null) {
              //삭제
              if (result is Map && result["delete"] == true) {
                final deletedItem = result["item"];
                setState(() {
                  items.removeWhere((i) => i["name"] == deletedItem["name"]);
                });
                return;
              }

              //수정
              final updatedItem = result as Map<String, dynamic>;
              setState(() {
                final idx = items.indexWhere(
                  (i) => i["name"] == updatedItem["name"],
                );
                if (idx != -1) items[idx] = updatedItem;
              });
            }
          },
          child: Container(
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(12),
              color: Colors.white,
              border: Border.all(
                color: AppColors.primaryDark.withOpacity(0.4),
                width: 1,
              ),
              boxShadow: [
                BoxShadow(
                  color: Colors.black12.withOpacity(0.07),
                  blurRadius: 6,
                ),
              ],
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                ClipRRect(
                  borderRadius: const BorderRadius.vertical(
                    top: Radius.circular(12),
                  ),
                  child: SizedBox(
                    height: 130,
                    width: double.infinity,
                    child: safeImage(item["image"], fit: BoxFit.cover),
                  ),
                ),

                const SizedBox(height: 10),

                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  child: Text(
                    item["name"],
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  child: Text(
                    item["category"],
                    style: TextStyle(
                      fontSize: 14,
                      color: AppColors.primaryDark,
                    ),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
