package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.PeacockTeal
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun BookmarksScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val completedDays by viewModel.completedDays.collectAsState()
  val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
  val days = viewModel.getDays()
  val allShlokas = viewModel.getAllShlokas()
  val allAartis = viewModel.getAartis()

  BackHandler { viewModel.handleBack() }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("bookmarks_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
  ) {
    // Header
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier =
            Modifier.size(36.dp)
              .clip(CircleShape)
              .background(PeacockTeal.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Default.Bookmark,
            contentDescription = null,
            tint = PeacockTeal,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "बुकमार्क और पढ़ने की प्रगति",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "सप्ताह परायण का दैनिक विवरण एवं प्रिय संग्रह",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // 1. Reading Progress Detailed Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "सात दिवसीय परायण प्रगति विवरण",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
          )

          Spacer(modifier = Modifier.height(8.dp))

          val progress = completedDays.size / 7f
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
            color = SaffronPrimary,
            trackColor = MaterialTheme.colorScheme.surface,
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "${completedDays.size} / 7 दिवस पूर्ण",
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )
            Text(
              text = "${(progress * 100).toInt()}% सम्पन्न",
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 7-day checklist pills
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            (1..7).forEach { dayNum ->
              val isDone = completedDays.contains(dayNum)
              Surface(
                shape = CircleShape,
                color = if (isDone) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(36.dp),
              ) {
                IconButton(onClick = { viewModel.toggleDayCompleted(dayNum) }) {
                  Text(
                    text = "$dayNum",
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // 2. Bookmarked Items Section Header
    item {
      Text(
        text = "सहेजे गए बुकमार्क (${bookmarkedIds.size})",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    if (bookmarkedIds.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
          colors =
            CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Icon(
              imageVector = Icons.Default.Bookmark,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(40.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "कोई बुकमार्क नहीं है",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleMedium,
            )
            Text(
              text = "कथा, श्लोक या आरती पढ़ते समय बुकमार्क आइकन पर टैप करें।",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    } else {
      items(bookmarkedIds.toList()) { id ->
        when {
          id.startsWith("katha_") -> {
            val dayNum = id.removePrefix("katha_").toIntOrNull()
            val day = days.find { it.dayNumber == dayNum }
            if (day != null) {
              BookmarkItemCard(
                category = "कथा दिवस ${day.dayNumber}",
                title = day.title,
                subtitle = day.summary,
                icon = Icons.Default.MenuBook,
                onOpen = { viewModel.selectDay(day.dayNumber) },
                onRemove = { viewModel.toggleBookmark(id) },
              )
            }
          }

          id.startsWith("shloka_") -> {
            val shlokaId = id.removePrefix("shloka_")
            val shloka = allShlokas.find { it.id == shlokaId }
            if (shloka != null) {
              BookmarkItemCard(
                category = "श्लोक • ${shloka.reference}",
                title = shloka.sanskritText,
                subtitle = shloka.hindiMeaning,
                icon = Icons.Default.MenuBook,
                onOpen = { viewModel.navigateTo(Screen.SHLOKAS) },
                onRemove = { viewModel.toggleBookmark(id) },
              )
            }
          }

          id.startsWith("aarti_") -> {
            val aartiId = id.removePrefix("aarti_")
            val aarti = allAartis.find { it.id == aartiId }
            if (aarti != null) {
              BookmarkItemCard(
                category = aarti.category,
                title = aarti.title,
                subtitle = aarti.lyrics.take(80) + "...",
                icon = Icons.Default.MusicNote,
                onOpen = { viewModel.navigateTo(Screen.AARTI_BHAJAN) },
                onRemove = { viewModel.toggleBookmark(id) },
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }
    }
  }
}

@Composable
fun BookmarkItemCard(
  category: String,
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onOpen: () -> Unit,
  onRemove: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.size(36.dp)
            .clip(CircleShape)
            .background(GoldContainer),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Color(0xFF5D4037),
          modifier = Modifier.size(18.dp),
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = category,
          style = MaterialTheme.typography.labelSmall,
          color = SaffronPrimary,
          fontWeight = FontWeight.Bold,
        )
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
        )
      }

      IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
        Icon(
          imageVector = Icons.Default.BookmarkRemove,
          contentDescription = "हटाएं",
          tint = MaterialTheme.colorScheme.error,
          modifier = Modifier.size(18.dp),
        )
      }

      Button(
        onClick = onOpen,
        colors =
          ButtonDefaults.buttonColors(
            containerColor = SaffronPrimary,
            contentColor = Color.White,
          ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
      ) {
        Text("देखें", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
