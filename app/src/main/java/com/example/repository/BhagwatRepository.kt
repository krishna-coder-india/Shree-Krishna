package com.example.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.BhagwatDataSource
import com.example.model.AartiItem
import com.example.model.KathaDay
import com.example.model.KrishnaLeela
import com.example.model.Shloka
import com.example.model.Suvichar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class BhagwatRepository(context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("bhagwat_katha_prefs", Context.MODE_PRIVATE)

  private val _completedDays = MutableStateFlow<Set<Int>>(emptySet())
  val completedDays: StateFlow<Set<Int>> = _completedDays.asStateFlow()

  private val _bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
  val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

  private val _fontScale = MutableStateFlow(1.0f)
  val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

  private val _customKathaEdits = MutableStateFlow<Map<Int, String>>(emptyMap())
  val customKathaEdits: StateFlow<Map<Int, String>> = _customKathaEdits.asStateFlow()

  private val _customPravachanNotes = MutableStateFlow<List<String>>(emptyList())
  val customPravachanNotes: StateFlow<List<String>> = _customPravachanNotes.asStateFlow()

  private val _customShlokas = MutableStateFlow<List<Shloka>>(emptyList())
  val customShlokas: StateFlow<List<Shloka>> = _customShlokas.asStateFlow()

  init {
    loadPreferences()
  }

  private fun loadPreferences() {
    // Completed Days
    val completedStr = prefs.getString("completed_days", "") ?: ""
    if (completedStr.isNotBlank()) {
      _completedDays.value = completedStr.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }

    // Bookmarks
    val bookmarksSet = prefs.getStringSet("bookmarks_set", emptySet()) ?: emptySet()
    _bookmarkedIds.value = bookmarksSet

    // Font Scale
    _fontScale.value = prefs.getFloat("font_scale", 1.0f)

    // Custom Katha Edits
    val editsJson = prefs.getString("katha_edits", "") ?: ""
    if (editsJson.isNotBlank()) {
      try {
        val jsonObj = JSONObject(editsJson)
        val map = mutableMapOf<Int, String>()
        jsonObj.keys().forEach { key ->
          val day = key.toIntOrNull()
          if (day != null) {
            map[day] = jsonObj.getString(key)
          }
        }
        _customKathaEdits.value = map
      } catch (_: Exception) {}
    }

    // Custom Pravachan Notes
    val notesJson = prefs.getString("pravachan_notes", "") ?: ""
    if (notesJson.isNotBlank()) {
      try {
        val arr = JSONArray(notesJson)
        val list = mutableListOf<String>()
        for (i in 0 until arr.length()) {
          list.add(arr.getString(i))
        }
        _customPravachanNotes.value = list
      } catch (_: Exception) {}
    }

    // Custom Shlokas
    val shlokasJson = prefs.getString("custom_shlokas", "") ?: ""
    if (shlokasJson.isNotBlank()) {
      try {
        val arr = JSONArray(shlokasJson)
        val list = mutableListOf<Shloka>()
        for (i in 0 until arr.length()) {
          val obj = arr.getJSONObject(i)
          list.add(
            Shloka(
              id = obj.getString("id"),
              sanskritText = obj.getString("sanskritText"),
              hindiMeaning = obj.getString("hindiMeaning"),
              reference = obj.getString("reference"),
              dayNumber = if (obj.has("dayNumber")) obj.getInt("dayNumber") else null,
              category = obj.optString("category", "कस्टम श्लोक"),
            )
          )
        }
        _customShlokas.value = list
      } catch (_: Exception) {}
    }
  }

  fun toggleDayCompleted(dayNumber: Int) {
    val current = _completedDays.value.toMutableSet()
    if (current.contains(dayNumber)) {
      current.remove(dayNumber)
    } else {
      current.add(dayNumber)
    }
    _completedDays.value = current
    prefs.edit().putString("completed_days", current.joinToString(",")).apply()
  }

  fun toggleBookmark(id: String) {
    val current = _bookmarkedIds.value.toMutableSet()
    if (current.contains(id)) {
      current.remove(id)
    } else {
      current.add(id)
    }
    _bookmarkedIds.value = current
    prefs.edit().putStringSet("bookmarks_set", current).apply()
  }

  fun setFontScale(scale: Float) {
    _fontScale.value = scale
    prefs.edit().putFloat("font_scale", scale).apply()
  }

  fun getAdminPin(): String {
    return prefs.getString("admin_pin", "1008") ?: "1008"
  }

  fun setAdminPin(newPin: String) {
    prefs.edit().putString("admin_pin", newPin).apply()
  }

  fun saveKathaEdit(dayNumber: Int, updatedFullStory: String) {
    val map = _customKathaEdits.value.toMutableMap()
    map[dayNumber] = updatedFullStory
    _customKathaEdits.value = map

    val jsonObj = JSONObject()
    map.forEach { (k, v) -> jsonObj.put(k.toString(), v) }
    prefs.edit().putString("katha_edits", jsonObj.toString()).apply()
  }

  fun addPravachanNote(note: String) {
    val list = _customPravachanNotes.value.toMutableList()
    list.add(0, note)
    _customPravachanNotes.value = list

    val arr = JSONArray(list)
    prefs.edit().putString("pravachan_notes", arr.toString()).apply()
  }

  fun removePravachanNote(index: Int) {
    val list = _customPravachanNotes.value.toMutableList()
    if (index in list.indices) {
      list.removeAt(index)
      _customPravachanNotes.value = list
      val arr = JSONArray(list)
      prefs.edit().putString("pravachan_notes", arr.toString()).apply()
    }
  }

  fun addCustomShloka(shloka: Shloka) {
    val list = _customShlokas.value.toMutableList()
    list.add(0, shloka)
    _customShlokas.value = list

    val arr = JSONArray()
    list.forEach { item ->
      val obj = JSONObject()
      obj.put("id", item.id)
      obj.put("sanskritText", item.sanskritText)
      obj.put("hindiMeaning", item.hindiMeaning)
      obj.put("reference", item.reference)
      item.dayNumber?.let { obj.put("dayNumber", it) }
      obj.put("category", item.category)
      arr.put(obj)
    }
    prefs.edit().putString("custom_shlokas", arr.toString()).apply()
  }

  fun resetToDefaultScriptures() {
    _customKathaEdits.value = emptyMap()
    _customPravachanNotes.value = emptyList()
    _customShlokas.value = emptyList()
    prefs
      .edit()
      .remove("katha_edits")
      .remove("pravachan_notes")
      .remove("custom_shlokas")
      .apply()
  }

  fun getDays(): List<KathaDay> {
    val edits = _customKathaEdits.value
    return BhagwatDataSource.sevenDaysKatha.map { day ->
      if (edits.containsKey(day.dayNumber)) {
        day.copy(fullStory = edits[day.dayNumber] ?: day.fullStory)
      } else {
        day
      }
    }
  }

  fun getAllShlokas(): List<Shloka> {
    return _customShlokas.value + BhagwatDataSource.shlokasList
  }

  fun getAartis(): List<AartiItem> = BhagwatDataSource.aartiList

  fun getLeelas(): List<KrishnaLeela> = BhagwatDataSource.leelasList

  fun getSuvichars(): List<Suvichar> = BhagwatDataSource.suvicharList
}
