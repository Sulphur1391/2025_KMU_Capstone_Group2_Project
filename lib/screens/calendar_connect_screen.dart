import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

class CalendarConnectScreen extends StatelessWidget {
  const CalendarConnectScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final pastelGreen = AppColors.primary;
    final pastelGreenDark = AppColors.primaryDark;

    return Scaffold(
      appBar: AppBar(
        title: const Text("Google 캘린더 연동하기"),
        backgroundColor: pastelGreen,
        foregroundColor: Colors.white,
        elevation: 0,
      ),

      body: Center(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 30),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Icon(Icons.calendar_month, size: 90, color: pastelGreenDark),

              const SizedBox(height: 25),

              const Text(
                "Google Calendar와 연동하면\n"
                "일정 기반 코디 추천이 가능합니다",
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 18,
                  height: 1.5,
                  fontWeight: FontWeight.w600,
                ),
              ),

              const SizedBox(height: 40),

              //Google 로그인 버튼
              ElevatedButton.icon(
                onPressed: () {
                  // 나중에 Google 로그인 기능 병합해야함
                },
                icon: const Icon(Icons.login),
                label: const Text(
                  "Google 계정으로 로그인",
                  style: TextStyle(fontSize: 16),
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: pastelGreenDark,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(
                    horizontal: 40,
                    vertical: 15,
                  ),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
              ),

              const SizedBox(height: 20),

              Text(
                "구글 로그인 후 일정이 자동으로 연동됩니다.",
                textAlign: TextAlign.center,
                style: TextStyle(color: Colors.grey[600], fontSize: 14),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
