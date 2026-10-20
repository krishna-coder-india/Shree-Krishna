package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.components.VedicTopAppBar
import com.example.ui.screens.AartiBhajanScreen
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AudioPlayerScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KathaDetailScreen
import com.example.ui.screens.LeelasScreen
import com.example.ui.screens.ShlokasScreen
import com.example.ui.theme.BhagwatTheme
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: BhagwatViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      BhagwatTheme {
        BhagwatApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun BhagwatApp(viewModel: BhagwatViewModel) {
  val context = LocalContext.current
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedDay by viewModel.selectedDay.collectAsState()
  val audioEngine = viewModel.audioEngine

  BackHandler {
    if (!viewModel.handleBack()) {
      (context as? ComponentActivity)?.finish()
    }
  }

  val appBarTitle =
    when (currentScreen) {
      Screen.HOME -> "श्रीमद्भागवत सप्ताह परायण"
      Screen.KATHA_DETAIL -> selectedDay?.let { "दिवस ${it.dayNumber}: कथा" } ?: "कथा परायण"
      Screen.SHLOKAS -> "भागवत प्रमुख श्लोक"
      Screen.AARTI_BHAJAN -> "आरती एवं भजन"
      Screen.LEELAS -> "श्रीकृष्ण दिव्य लीलाएं"
      Screen.AUDIO_PLAYER -> "कथा श्रवण"
      Screen.BOOKMARKS_PROGRESS -> "बुकमार्क व प्रगति"
      Screen.ADMIN -> "एडमिन पैनल"
      Screen.ABOUT -> "ग्रंथ परिचय एवं नियम"
    }

  Scaffold(
    modifier = Modifier.fillMaxSize().testTag("main_scaffold"),
    topBar = {
      VedicTopAppBar(
        title = appBarTitle,
        showBack = (currentScreen != Screen.HOME),
        onBackClick = { viewModel.handleBack() },
        isAudioPlaying = audioEngine.isSpeaking,
        onAudioBarClick = { viewModel.navigateTo(Screen.AUDIO_PLAYER) },
        isBookmarked =
          if (currentScreen == Screen.KATHA_DETAIL && selectedDay != null) {
            viewModel.isBookmarked("katha_${selectedDay!!.dayNumber}")
          } else {
            null
          },
        onBookmarkClick =
          if (currentScreen == Screen.KATHA_DETAIL && selectedDay != null) {
            { viewModel.toggleBookmark("katha_${selectedDay!!.dayNumber}") }
          } else {
            null
          },
        onShareClick =
          if (currentScreen == Screen.KATHA_DETAIL && selectedDay != null) {
            {
              viewModel.shareContent(
                context,
                selectedDay!!.title,
                "${selectedDay!!.summary}\n\n${selectedDay!!.spiritualMessage}",
              )
            }
          } else {
            null
          },
      )
    },
    bottomBar = {
      Column(modifier = Modifier.navigationBarsPadding()) {
        // Mini Audio Dock (visible when audio is speaking or paused and user is on another screen)
        AnimatedVisibility(
          visible = (audioEngine.isSpeaking || audioEngine.isPaused) && currentScreen != Screen.AUDIO_PLAYER,
          enter = fadeIn(),
          exit = fadeOut(),
        ) {
          Surface(
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 6.dp,
            modifier =
              Modifier.fillMaxWidth()
                .clickable { viewModel.navigateTo(Screen.AUDIO_PLAYER) }
                .testTag("mini_audio_dock"),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Box(
                modifier =
                  Modifier.size(32.dp)
                    .clip(CircleShape)
                    .background(GoldAccent),
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  imageVector = Icons.Default.GraphicEq,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(18.dp),
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "सुन रहे हैं: दिवस ${audioEngine.currentDayPlaying ?: ""}",
                  style = MaterialTheme.typography.labelSmall,
                  color = GoldAccent,
                  fontWeight = FontWeight.Bold,
                )
                Text(
                  text = audioEngine.currentSpeechTitle.ifBlank { "कथा श्रवण" },
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }

              IconButton(
                onClick = {
                  if (audioEngine.isSpeaking) audioEngine.pause() else audioEngine.resume()
                },
                modifier = Modifier.size(36.dp),
              ) {
                Icon(
                  imageVector =
                    if (audioEngine.isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                  contentDescription = if (audioEngine.isSpeaking) "रोकें" else "चलाएं",
                  tint = Color.White,
                )
              }

              IconButton(
                onClick = { audioEngine.stop() },
                modifier = Modifier.size(36.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.Stop,
                  contentDescription = "समाप्त करें",
                  tint = MaterialTheme.colorScheme.errorContainer,
                )
              }
            }
          }
        }

        // Bottom Navigation Bar
        BhagwatBottomNavigation(
          currentScreen = currentScreen,
          onSelectScreen = { screen -> viewModel.navigateTo(screen) },
        )
      }
    },
  ) { paddingValues ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .background(MaterialTheme.colorScheme.background)
          .padding(paddingValues),
    ) {
      when (currentScreen) {
        Screen.HOME -> HomeScreen(viewModel = viewModel)
        Screen.KATHA_DETAIL -> KathaDetailScreen(viewModel = viewModel)
        Screen.SHLOKAS -> ShlokasScreen(viewModel = viewModel)
        Screen.AARTI_BHAJAN -> AartiBhajanScreen(viewModel = viewModel)
        Screen.LEELAS -> LeelasScreen(viewModel = viewModel)
        Screen.AUDIO_PLAYER -> AudioPlayerScreen(viewModel = viewModel)
        Screen.BOOKMARKS_PROGRESS -> BookmarksScreen(viewModel = viewModel)
        Screen.ADMIN -> AdminScreen(viewModel = viewModel)
        Screen.ABOUT -> AboutScreen(viewModel = viewModel)
      }
    }
  }
}

data class BottomNavItem(
  val screen: Screen,
  val label: String,
  val activeIcon: ImageVector,
  val inactiveIcon: ImageVector,
)

@Composable
fun BhagwatBottomNavigation(
  currentScreen: Screen,
  onSelectScreen: (Screen) -> Unit,
) {
  val items =
    listOf(
      BottomNavItem(Screen.HOME, "होम", Icons.Filled.Home, Icons.Outlined.Home),
      BottomNavItem(Screen.KATHA_DETAIL, "कथा", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
      BottomNavItem(Screen.SHLOKAS, "श्लोक", Icons.Filled.AutoAwesome, Icons.Filled.AutoAwesome),
      BottomNavItem(Screen.AARTI_BHAJAN, "आरती", Icons.Filled.MusicNote, Icons.Outlined.MusicNote),
      BottomNavItem(Screen.AUDIO_PLAYER, "श्रवण", Icons.Filled.VolumeUp, Icons.Outlined.VolumeUp),
      BottomNavItem(Screen.BOOKMARKS_PROGRESS, "प्रगति", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    )

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp,
    modifier = Modifier.testTag("bottom_nav_bar"),
  ) {
    items.forEach { item ->
      val selected = (currentScreen == item.screen)
      NavigationBarItem(
        selected = selected,
        onClick = { onSelectScreen(item.screen) },
        icon = {
          Icon(
            imageVector = if (selected) item.activeIcon else item.inactiveIcon,
            contentDescription = item.label,
          )
        },
        label = {
          Text(
            text = item.label,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 11.sp,
          )
        },
        colors =
          NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            selectedTextColor = SaffronPrimary,
            indicatorColor = SaffronPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
          ),
      )
    }
  }
}
