import 'package:flutter/material.dart';

class SaveCompleteScreen extends StatelessWidget {
  const SaveCompleteScreen({super.key});

  final Color pastelBlue = const Color(0xFFA7D8FF);
  final Color pastelBlueDark = const Color(0xFF6FB7FF);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("등록 완료"),
        backgroundColor: pastelBlue,
        foregroundColor: Colors.white,
      ),

      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(30),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.check_circle, color: pastelBlueDark, size: 80),

              const SizedBox(height: 20),

              const Text(
                "옷이 성공적으로 저장되었습니다!",
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),

              const SizedBox(height: 40),

              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.popUntil(context, ModalRoute.withName("/"));
                  },
                  child: const Text("홈으로 돌아가기"),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
