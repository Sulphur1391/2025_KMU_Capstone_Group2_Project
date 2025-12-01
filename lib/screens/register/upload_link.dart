import 'package:flutter/material.dart';

class UploadLinkScreen extends StatefulWidget {
  const UploadLinkScreen({super.key});

  @override
  State<UploadLinkScreen> createState() => _UploadLinkScreenState();
}

class _UploadLinkScreenState extends State<UploadLinkScreen> {
  final TextEditingController linkController = TextEditingController();

  final Color pastelBlue = const Color(0xFFA7D8FF);
  final Color pastelBlueDark = const Color(0xFF6FB7FF);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("링크로 등록하기"),
        backgroundColor: pastelBlue,
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "판매 링크를 입력해주세요",
              style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
            ),

            const SizedBox(height: 15),

            TextField(
              controller: linkController,
              decoration: InputDecoration(
                hintText: "예: https://store.com/item/123",
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                filled: true,
                fillColor: Colors.grey[100],
              ),
            ),

            const SizedBox(height: 30),

            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, "/register/photo");
                },
                child: const Text("다음"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
