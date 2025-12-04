import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/theme.dart';
import '../../../core/widgets/widgets.dart';
import '../../../core/error/error_handler.dart';
import '../../../providers/user_provider.dart';
import '../../../main.dart';
import '../logic/logic.dart';
import '../widgets/widgets.dart';
import '../../closet/pages/closet_add_page.dart';
import '../../virtual_fitting/pages/virtual_fitting_page.dart';

/// ============================================
/// 홈 페이지
/// ============================================

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  Map<String, dynamic>? weather;
  List<Map<String, dynamic>> schedules = [];
  List<Map<String, dynamic>> recentOutfits = [];
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => isLoading = true);

    try {
      final results = await Future.wait([
        HomeLogic.getTodayWeather(),
        HomeLogic.getTodaySchedules(),
        HomeLogic.getRecentOutfits(limit: 3),
      ]);

      setState(() {
        weather = results[0] as Map<String, dynamic>?;
        schedules = results[1] as List<Map<String, dynamic>>;
        recentOutfits = results[2] as List<Map<String, dynamic>>;
        isLoading = false;
      });
    } catch (e) {
      setState(() => isLoading = false);
      if (mounted) {
        await ErrorHandler.showError(context, e, onRetry: _loadData);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        leading: Text(
          'Outfit Hub',
          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
        ),
      ),
      child: SafeArea(
        child: isLoading
            ? const AppLoadingIndicator()
            : CustomScrollView(
                slivers: [
                  CupertinoSliverRefreshControl(
                    onRefresh: _loadData,
                  ),
                  SliverToBoxAdapter(
                    child: Padding(
                      padding: const EdgeInsets.all(AppSpacing.lg),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          _buildDateHeader(),
                          const SizedBox(height: AppSpacing.lg),
                          WeatherCard(weather: weather),
                          const SizedBox(height: AppSpacing.lg),
                          _buildQuickActions(),
                          const SizedBox(height: AppSpacing.lg),
                          ScheduleSummaryCard(
                            schedules: schedules,
                            onTap: () {
                              mainTabKey.currentState?.changeTab(2);
                            },
                          ),
                          const SizedBox(height: AppSpacing.lg),
                          RecentOutfitCard(
                            outfits: recentOutfits,
                            onTap: () {
                              mainTabKey.currentState?.changeTab(2);
                            },
                          ),
                          const SizedBox(height: AppSpacing.xxl),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
      ),
    );
  }

  Widget _buildDateHeader() {
    return Consumer<UserProvider>(
      builder: (context, userProvider, child) {
        final username = userProvider.username;
        final now = DateTime.now();
        final dateStr = '${now.year}년 ${now.month}월 ${now.day}일';

        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              dateStr,
              style: const TextStyle(
                fontSize: 16,
                color: AppColors.textSecondary,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              '$username님 👋',
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        );
      },
    );
  }

  Widget _buildQuickActions() {
    return Row(
      children: [
        Expanded(
          child: QuickActionButton(
            icon: CupertinoIcons.add_circled_solid,
            label: '옷 추가',
            onTap: () async {
              final result = await Navigator.push(
                context,
                CupertinoPageRoute(
                  builder: (_) => const ClosetAddPage(),
                ),
              );
              if (result == true) {
                _loadData();
              }
            },
          ),
        ),
        const SizedBox(width: AppSpacing.md),
        Expanded(
          child: QuickActionButton(
            icon: CupertinoIcons.person_crop_square,
            label: '가상 피팅',
            color: AppColors.primaryDark,
            onTap: () {
              Navigator.push(
                context,
                CupertinoPageRoute(
                  builder: (_) => const VirtualFittingPage(),
                ),
              );
            },
          ),
        ),
      ],
    );
  }
}
