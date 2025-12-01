import 'package:flutter/material.dart';

Widget safeImage(String? url, {BoxFit fit = BoxFit.cover}) {
  return Image.network(
    url ?? "",
    fit: fit,
    errorBuilder: (context, error, stackTrace) {
      return Container(
        color: Colors.grey[300],
        child: const Center(
          child: Text(
            "이미지",
            style: TextStyle(
              color: Colors.black54,
              fontSize: 16,
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
      );
    },
  );
}
