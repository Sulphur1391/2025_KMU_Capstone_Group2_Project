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
  final List<Map<String, dynamic>> items = [
    {
      "name": "반팔티",
      "category": "상의",
      "image": "",
      "season": null,
      "style": null,
      "color": null,
    },
    {
      "name": "청바지",
      "category": "하의",
      "image": null,
      "season": null,
      "style": null,
      "color": null,
    },
    {
      "name": "패딩",
      "category": "아우터",
      "image": "https://via.placeholder.com/150",
      "season": null,
      "style": null,
      "color": null,
    },
    {
      "name": "운동화",
      "category": "신발",
      "image": "",
      "season": null,
      "style": null,
      "color": null,
    },
  ];

  String? filterSeason;
  String? filterStyle;
  String? filterColor;

  late TabController _tabController;

  final List<String> tabs = ["전체", "상의", "하의", "아우터", "신발", "위시"];

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: tabs.length, vsync: this);
  }

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

          // 탭바
          Container(
            padding: const EdgeInsets.only(left: 4),
            decoration: BoxDecoration(
              color: AppColors.primary,
              border: Border(
                bottom: BorderSide(
                  color: AppColors.primaryDark.withOpacity(0.5),
                  width: 1,
                ),
              ),
            ),
            child: TabBar(
              controller: _tabController,
              isScrollable: true,
              labelPadding: const EdgeInsets.symmetric(horizontal: 12),
              indicatorPadding: EdgeInsets.zero,
              tabAlignment: TabAlignment.start,
              indicatorColor: Colors.white,
              labelColor: Colors.white,
              unselectedLabelColor: Colors.white70,
              tabs: tabs.map((e) => Tab(text: e)).toList(),
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
