import 'package:flutter/material.dart';
import 'package:outfithub/theme/app_color.dart';

class AppTextStyles {
  static const title = TextStyle(
    fontSize: 20,
    fontWeight: FontWeight.bold,
    color: AppColors.primaryDark,
  );

  static const itemName = TextStyle(fontSize: 16, fontWeight: FontWeight.bold);

  static const tag = TextStyle(fontSize: 12, color: AppColors.primaryDark);

  static const smallGrey = TextStyle(fontSize: 12, color: AppColors.greyText);
}
