class CommunityPost {
  final String id;
  final String imageUrl;
  final int likes;
  final List<String> tags;

  CommunityPost({
    required this.id,
    required this.imageUrl,
    required this.likes,
    required this.tags,
  });
}
