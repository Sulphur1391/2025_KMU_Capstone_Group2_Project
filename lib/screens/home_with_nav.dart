import 'package:flutter/material.dart';

//screens
import 'package:outfithub/screens/home_screen.dart';
import 'package:outfithub/screens/closet/closet_list.dart';
import 'package:outfithub/screens/community/community_main.dart';
import 'package:outfithub/screens/mypage/mypage_main.dart';
import 'package:outfithub/theme/app_color.dart';

class HomeWithNav extends StatefulWidget {
  const HomeWithNav({super.key});

  @override
  State<HomeWithNav> createState() => _HomeWithNavState();
}

class _HomeWithNavState extends State<HomeWithNav> {
  int _currentIndex = 0;

  final List<Widget> _screens = const [
    HomeScreen(),
    ClosetListScreen(),
    SizedBox(),
    CommunityMainScreen(),
    MyPageScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _screens[_currentIndex],

      bottomNavigationBar: Padding(
        padding: const EdgeInsets.only(left: 16, right: 16, bottom: 12),
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 10),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(28),
            boxShadow: [
              BoxShadow(
                color: Colors.black12.withOpacity(0.15),
                blurRadius: 12,
                offset: const Offset(0, 4),
              ),
            ],
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              _navItem(index: 0, icon: Icons.home_outlined, label: "홈"),
              _navItem(index: 1, icon: Icons.checkroom_outlined, label: "옷장"),

              //가운데 + 버튼
              GestureDetector(
                onTap: () {
                  Navigator.pushNamed(context, "/register/item");
                },
                child: Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: AppColors.primary.withOpacity(0.25),
                    shape: BoxShape.circle,
                  ),
                  child: Icon(
                    Icons.add,
                    size: 32,
                    color: AppColors.primaryDark,
                  ),
                ),
              ),

              _navItem(index: 3, icon: Icons.forum_outlined, label: "커뮤니티"),
              _navItem(index: 4, icon: Icons.person_outline, label: "마이페이지"),
            ],
          ),
        ),
      ),
    );
  }

  Widget _navItem({
    required int index,
    required IconData icon,
    required String label,
  }) {
    final bool selected = _currentIndex == index;

    return GestureDetector(
      onTap: () {
        setState(() => _currentIndex = index);
      },
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(
            icon,
            size: selected ? 28 : 26,
            color: selected ? AppColors.primaryDark : Colors.grey,
          ),
          const SizedBox(height: 3),
          Text(
            label,
            style: TextStyle(
              fontSize: 11,
              color: selected ? AppColors.primaryDark : Colors.grey,
              fontWeight: selected ? FontWeight.bold : FontWeight.normal,
            ),
          ),
        ],
      ),
    );
  }
}
