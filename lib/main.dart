import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

//navigation
import 'screens/home_with_nav.dart';

//register
import 'screens/register/add_item.dart';

//closet
import 'screens/closet/closet_list.dart';
import 'screens/closet/closet_detail.dart';

//outfit recommend
import 'screens/outfit_recommend/outfit_result.dart';

//wishlist
import 'screens/wishlist/wishlist_list.dart';
import 'screens/wishlist/wishlist_detail.dart';

//community
import 'screens/community/community_main.dart';

//filter (통합)
import 'screens/filter/filter_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'OutfitHub',
      debugShowCheckedModeBanner: false,

      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,

        appBarTheme: const AppBarTheme(
          backgroundColor: AppColors.primary,
          foregroundColor: Colors.white,
          elevation: 0,
        ),

        colorScheme: ColorScheme.fromSeed(
          seedColor: AppColors.primary,
          primary: AppColors.primary,
          secondary: AppColors.primaryDark,
          surface: Colors.white,
        ),

        bottomNavigationBarTheme: const BottomNavigationBarThemeData(
          selectedItemColor: AppColors.primaryDark,
          unselectedItemColor: Colors.grey,
          backgroundColor: Colors.white,
          elevation: 10,
        ),

        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ElevatedButton.styleFrom(
            backgroundColor: AppColors.primaryDark,
            foregroundColor: Colors.white,
            textStyle: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 15,
            ),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
            padding: const EdgeInsets.symmetric(vertical: 14),
          ),
        ),
      ),

      home: const HomeWithNav(),

      routes: {
        '/register/item': (context) => const AddItemScreen(),

        //closet
        '/closet': (context) => const ClosetListScreen(),
        '/closet/detail': (context) => const ClosetDetailScreen(),

        //outfit recommend
        '/outfit/result': (context) => const OutfitResultScreen(),

        //wishlist
        '/wishlist': (context) => const WishlistListScreen(),
        '/wishlist/detail': (context) => const WishlistDetailScreen(),

        //community
        '/community': (context) => const CommunityMainScreen(),

        //통합 필터 화면
        '/filter': (context) => const FilterScreen(),
      },
    );
  }
}
