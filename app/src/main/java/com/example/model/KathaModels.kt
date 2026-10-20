package com.example.model

data class KathaDay(
  val dayNumber: Int,
  val title: String,
  val subTitle: String,
  val skandhaReference: String,
  val summary: String,
  val fullStory: String,
  val keyCharacters: List<String>,
  val spiritualMessage: String,
  val keyShlokas: List<Shloka>,
  val estimatedReadTimeMinutes: Int = 15,
)

data class Shloka(
  val id: String,
  val sanskritText: String,
  val hindiMeaning: String,
  val reference: String,
  val dayNumber: Int? = null,
  val category: String = "प्रमुख श्लोक",
)

data class AartiItem(
  val id: String,
  val title: String,
  val category: String, // "आरती", "भजन", "मंगलाचरण", "स्तुति"
  val lyrics: String,
  val significance: String = "",
)

data class Suvichar(
  val id: String,
  val quote: String,
  val source: String,
)

data class KrishnaLeela(
  val id: String,
  val title: String,
  val shortDesc: String,
  val fullStory: String,
  val dayNumber: Int,
  val moralTeaching: String,
)

enum class Screen {
  HOME,
  KATHA_DETAIL,
  SHLOKAS,
  AARTI_BHAJAN,
  LEELAS,
  AUDIO_PLAYER,
  BOOKMARKS_PROGRESS,
  ADMIN,
  ABOUT,
}
