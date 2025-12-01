import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';
import 'package:outfithub/screens/calendar_connect_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final Map<String, List<Map<String, dynamic>>> weeklyEvents = {
    "월": [
      {
        "time": "14:00",
        "title": "팀 프로젝트 회의",
        "weather": "3℃",
        "tag": "강풍",
        "style": "깔끔",
      },
    ],
    "화": [],
    "수": [
      {
        "time": "17:00",
        "title": "데이트",
        "weather": "5℃",
        "tag": "맑음",
        "style": "청순",
      },
    ],
    "목": [],
    "금": [],
    "토": [],
    "일": [],
  };

  final List<String> dayOrder = ["월", "화", "수", "목", "금", "토", "일"];
  bool showAllDays = false;

  final List<Map<String, String>> recentOutfits = [
    {"name": "니트 가디건"},
    {"name": "롱패딩"},
    {"name": "청바지"},
  ];

  //이번 주 요일별 날짜 계산
  Map<String, DateTime> getWeekDates() {
    DateTime now = DateTime.now();
    int weekday = now.weekday;

    Map<String, DateTime> result = {};
    for (int i = 0; i < 7; i++) {
      DateTime date = now.subtract(Duration(days: weekday - 1 - i));
      result[dayOrder[i]] = date;
    }
    return result;
  }

  @override
  Widget build(BuildContext context) {
    final weekDates = getWeekDates();

    return SafeArea(
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "안녕하세요! OO님",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),

            //캘린더 연동 버튼
            TextButton.icon(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => const CalendarConnectScreen(),
                  ),
                );
              },
              icon: const Icon(Icons.link, color: AppColors.primaryDark),
              label: const Text(
                "구글 캘린더와 연동하기",
                style: TextStyle(
                  fontSize: 16,
                  color: AppColors.primaryDark,
                  fontWeight: FontWeight.w600,
                ),
              ),
              style: TextButton.styleFrom(
                padding: EdgeInsets.zero,
                minimumSize: Size.zero,
                tapTargetSize: MaterialTapTargetSize.shrinkWrap,
              ),
            ),

            const SizedBox(height: 30),

            const Text(
              "이번 주 일정",
              style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),

            //일정 리스트
            ListView(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              children: dayOrder
                  .asMap()
                  .entries
                  .where((entry) => showAllDays || entry.key < 3)
                  .map((entry) {
                    String day = entry.value;
                    DateTime date = weekDates[day]!;
                    List<Map<String, dynamic>> events = weeklyEvents[day]!;
                    return _daySchedule(context, day, date, events);
                  })
                  .toList(),
            ),

            //더보기
            Center(
              child: TextButton(
                onPressed: () => setState(() => showAllDays = !showAllDays),
                child: Text(
                  showAllDays ? "접기 ▲" : "더보기 ▼",
                  style: const TextStyle(
                    color: Colors.black,
                    fontSize: 15,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
            ),

            const SizedBox(height: 35),

            const Text(
              "최근 입은 옷",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),

            //최근 입은 옷
            SizedBox(
              height: 150,
              child: ListView.separated(
                scrollDirection: Axis.horizontal,
                itemCount: recentOutfits.length,
                separatorBuilder: (context, index) => const SizedBox(width: 16),
                itemBuilder: (context, index) {
                  final item = recentOutfits[index];
                  return Column(
                    children: [
                      Container(
                        width: 100,
                        height: 100,
                        decoration: BoxDecoration(
                          color: Colors.grey[300],
                          borderRadius: BorderRadius.circular(14),
                        ),
                        child: const Center(
                          child: Text(
                            "이미지",
                            style: TextStyle(
                              fontSize: 14,
                              color: Colors.black54,
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(height: 8),
                      SizedBox(
                        width: 100,
                        child: Text(
                          item["name"]!,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          textAlign: TextAlign.center,
                          style: const TextStyle(
                            fontSize: 13,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ),
                    ],
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  //날짜별 일정 카드
  Widget _daySchedule(
    BuildContext context,
    String day,
    DateTime date,
    List<Map<String, dynamic>> events,
  ) {
    String formatted = "${date.month}월 ${date.day}일 ($day)";

    return Column(
      children: [
        if (events.isNotEmpty)
          ...events.map((e) {
            return _eventCard(
              context: context,
              dateLabel: formatted,
              title: e["title"],
              time: e["time"],
              tags: [e["style"], e["tag"], e["weather"]],
              onTap: () {
                Navigator.pushNamed(context, "/outfit/result", arguments: e);
              },
            );
          }),
        if (events.isEmpty)
          _emptyDayCard(context: context, dateLabel: formatted),
      ],
    );
  }

  //일정 있는 날
  Widget _eventCard({
    required BuildContext context,
    required String dateLabel,
    required String title,
    required String time,
    required List<String> tags,
    required VoidCallback onTap,
  }) {
    return Container(
      margin: const EdgeInsets.only(bottom: 14),
      padding: const EdgeInsets.all(16),
      decoration: _cardStyle(),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          //제목 + 버튼
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                title,
                style: const TextStyle(
                  fontSize: 17,
                  fontWeight: FontWeight.bold,
                ),
              ),
              TextButton(
                onPressed: onTap,
                style: _textBtn(),
                child: const Text(
                  "코디 생성 >",
                  style: TextStyle(
                    color: AppColors.primaryDark,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
            ],
          ),

          const SizedBox(height: 6),

          Text(
            "$dateLabel  ·  $time",
            style: const TextStyle(fontSize: 14, color: Colors.black54),
          ),

          const SizedBox(height: 12),

          Wrap(
            spacing: 8,
            runSpacing: 8,
            children: tags.map((t) => _tagChip(t)).toList(),
          ),

          const SizedBox(height: 6),
        ],
      ),
    );
  }

  //일정 없는 날
  Widget _emptyDayCard({
    required BuildContext context,
    required String dateLabel,
  }) {
    return Container(
      margin: const EdgeInsets.only(bottom: 14),
      padding: const EdgeInsets.all(16),
      decoration: _cardStyle(),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          //날짜 + 버튼
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                dateLabel,
                style: const TextStyle(
                  fontSize: 17,
                  fontWeight: FontWeight.bold,
                ),
              ),
              TextButton(
                onPressed: () {
                  Navigator.pushNamed(
                    context,
                    "/outfit/result",
                    arguments: null,
                  );
                },
                style: _textBtn(),
                child: const Text(
                  "코디 생성 >",
                  style: TextStyle(
                    color: AppColors.primaryDark,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
            ],
          ),

          const SizedBox(height: 6),
          const Text("등록된 일정이 없습니다.", style: TextStyle(color: Colors.grey)),

          const SizedBox(height: 12),
          const SizedBox(height: 26),
        ],
      ),
    );
  }

  //카드 스타일
  BoxDecoration _cardStyle() {
    return BoxDecoration(
      color: Colors.white,
      borderRadius: BorderRadius.circular(14),
      border: Border.all(color: Colors.black12.withOpacity(0.1)),
      boxShadow: [
        BoxShadow(
          color: Colors.black.withOpacity(0.04),
          blurRadius: 6,
          offset: const Offset(0, 3),
        ),
      ],
    );
  }

  //TextButton
  ButtonStyle _textBtn() {
    return TextButton.styleFrom(
      padding: EdgeInsets.zero,
      minimumSize: Size.zero,
      tapTargetSize: MaterialTapTargetSize.shrinkWrap,
    );
  }

  //태그 칩
  Widget _tagChip(String text) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
      decoration: BoxDecoration(
        color: AppColors.primary.withOpacity(0.18),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: AppColors.primaryDark.withOpacity(0.5),
          width: 1,
        ),
      ),
      child: Text(
        "#$text",
        style: const TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w500,
          color: AppColors.primaryDark,
        ),
      ),
    );
  }
}
