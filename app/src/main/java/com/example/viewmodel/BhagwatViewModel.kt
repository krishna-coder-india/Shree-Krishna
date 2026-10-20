package com.example.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.KathaAudioEngine
import com.example.data.BhagwatDataSource
import com.example.model.AartiItem
import com.example.model.KathaDay
import com.example.model.KrishnaLeela
import com.example.model.Screen
import com.example.model.Shloka
import com.example.model.Suvichar
import com.example.repository.BhagwatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BhagwatViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = BhagwatRepository(application)
  val audioEngine = KathaAudioEngine(application)

  private val _currentScreen = MutableStateFlow(Screen.HOME)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  private val _screenHistory = mutableListOf<Screen>()

  private val _selectedDay = MutableStateFlow<KathaDay?>(null)
  val selectedDay: StateFlow<KathaDay?> = _selectedDay.asStateFlow()

  private val _selectedLeela = MutableStateFlow<KrishnaLeela?>(null)
  val selectedLeela: StateFlow<KrishnaLeela?> = _selectedLeela.asStateFlow()

  private val _selectedAarti = MutableStateFlow<AartiItem?>(null)
  val selectedAarti: StateFlow<AartiItem?> = _selectedAarti.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _currentSuvicharIndex = MutableStateFlow(0)
  val currentSuvichar: StateFlow<Suvichar> =
    _currentSuvicharIndex
      .combine(MutableStateFlow(BhagwatDataSource.suvicharList)) { idx, list ->
        list[idx % list.size]
      }
      .stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        BhagwatDataSource.suvicharList.first(),
      )

  val completedDays: StateFlow<Set<Int>> = repository.completedDays
  val bookmarkedIds: StateFlow<Set<String>> = repository.bookmarkedIds
  val fontScale: StateFlow<Float> = repository.fontScale
  val customPravachanNotes: StateFlow<List<String>> = repository.customPravachanNotes

  private val _isAdminAuthenticated = MutableStateFlow(false)
  val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

  init {
    val initialDay = repository.getDays().firstOrNull()
    _selectedDay.value = initialDay
  }

  fun getDays(): List<KathaDay> = repository.getDays()

  fun getAllShlokas(): List<Shloka> = repository.getAllShlokas()

  fun getAartis(): List<AartiItem> = repository.getAartis()

  fun getLeelas(): List<KrishnaLeela> = repository.getLeelas()

  fun navigateTo(screen: Screen) {
    if (_currentScreen.value != screen) {
      _screenHistory.add(_currentScreen.value)
      _currentScreen.value = screen
    }
  }

  fun handleBack(): Boolean {
    return if (_screenHistory.isNotEmpty()) {
      val prev = _screenHistory.removeAt(_screenHistory.size - 1)
      _currentScreen.value = prev
      true
    } else if (_currentScreen.value != Screen.HOME) {
      _currentScreen.value = Screen.HOME
      true
    } else {
      false
    }
  }

  fun selectDay(dayNumber: Int) {
    val day = repository.getDays().find { it.dayNumber == dayNumber }
    if (day != null) {
      _selectedDay.value = day
      navigateTo(Screen.KATHA_DETAIL)
    }
  }

  fun nextDay() {
    val cur = _selectedDay.value?.dayNumber ?: 1
    if (cur < 7) {
      selectDay(cur + 1)
    }
  }

  fun previousDay() {
    val cur = _selectedDay.value?.dayNumber ?: 1
    if (cur > 1) {
      selectDay(cur - 1)
    }
  }

  fun selectLeela(leela: KrishnaLeela) {
    _selectedLeela.value = leela
  }

  fun selectAarti(aarti: AartiItem) {
    _selectedAarti.value = aarti
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun nextSuvichar() {
    _currentSuvicharIndex.value =
      (_currentSuvicharIndex.value + 1) % BhagwatDataSource.suvicharList.size
  }

  fun toggleDayCompleted(dayNumber: Int) {
    repository.toggleDayCompleted(dayNumber)
  }

  fun toggleBookmark(id: String) {
    repository.toggleBookmark(id)
  }

  fun isBookmarked(id: String): Boolean {
    return bookmarkedIds.value.contains(id)
  }

  fun setFontScale(scale: Float) {
    repository.setFontScale(scale)
  }

  fun verifyAdminPin(enteredPin: String): Boolean {
    val actual = repository.getAdminPin()
    val isValid = (enteredPin.trim() == actual.trim())
    _isAdminAuthenticated.value = isValid
    return isValid
  }

  fun logoutAdmin() {
    _isAdminAuthenticated.value = false
  }

  fun saveKathaEdit(dayNumber: Int, updatedFullStory: String) {
    repository.saveKathaEdit(dayNumber, updatedFullStory)
    val day = repository.getDays().find { it.dayNumber == dayNumber }
    if (_selectedDay.value?.dayNumber == dayNumber) {
      _selectedDay.value = day
    }
  }

  fun addPravachanNote(note: String) {
    repository.addPravachanNote(note)
  }

  fun removePravachanNote(index: Int) {
    repository.removePravachanNote(index)
  }

  fun addCustomShloka(shloka: Shloka) {
    repository.addCustomShloka(shloka)
  }

  fun resetToDefaultScriptures() {
    repository.resetToDefaultScriptures()
    val day = repository.getDays().find { it.dayNumber == (_selectedDay.value?.dayNumber ?: 1) }
    _selectedDay.value = day
  }

  fun changeAdminPin(newPin: String) {
    repository.setAdminPin(newPin)
  }

  // Audio Playback shortcuts
  fun playCurrentKathaAudio() {
    val day = _selectedDay.value ?: return
    audioEngine.playKatha(day.dayNumber, day.title, day.fullStory)
  }

  fun playDayAudio(dayNumber: Int) {
    val day = repository.getDays().find { it.dayNumber == dayNumber } ?: return
    _selectedDay.value = day
    audioEngine.playKatha(day.dayNumber, day.title, day.fullStory)
    navigateTo(Screen.AUDIO_PLAYER)
  }

  fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "कॉपी किया गया", Toast.LENGTH_SHORT).show()
  }

  fun shareContent(context: Context, title: String, text: String) {
    val shareIntent =
      Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(
          Intent.EXTRA_TEXT,
          "॥ श्रीमद्भागवत कथा सप्ताह परायण ॥\n\n$title\n\n$text\n\nजय श्रीकृष्ण! 🙏",
        )
      }
    context.startActivity(Intent.createChooser(shareIntent, "साझा करें"))
  }

  override fun onCleared() {
    super.onCleared()
    audioEngine.shutdown()
  }
}
