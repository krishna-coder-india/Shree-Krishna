package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KathaDay
import com.example.ui.components.AudioPlayerBar
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.PeacockTeal
import com.example.ui.theme.PeacockTealContainer
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun KathaDetailScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val day = viewModel.selectedDay.collectAsState().value ?: return
  val completedDays by viewModel.completedDays.collectAsState()
  val fontScale by viewModel.fontScale.collectAsState()
  val isCompleted = completedDays.contains(day.dayNumber)
  val isBookmarked = viewModel.isBookmarked("katha_${day.dayNumber}")
  var showFontSelector by remember { mutableStateOf(false) }

  BackHandler { viewModel.handleBack() }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("katha_detail_screen"),
    contentPadding = PaddingValues(bottom = 96.dp),
  ) {
    // Top Banner & Header info
    item {
      Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = GoldAccent,
            ) {
              Text(
                text = "सप्ताह परायण • दिवस ${day.dayNumber}",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              )
            }

            Row {
              // Font toggle
              IconButton(
                onClick = { showFontSelector = !showFontSelector },
                modifier = Modifier.size(36.dp).testTag("font_resizer_button"),
              ) {
                Icon(
                  imageVector = Icons.Default.FormatSize,
                  contentDescription = "फॉन्ट साइज",
                  tint = Color.White,
                )
              }

              // Bookmark
              IconButton(
                onClick = { viewModel.toggleBookmark("katha_${day.dayNumber}") },
                modifier = Modifier.size(36.dp).testTag("detail_bookmark_button"),
              ) {
                Icon(
                  imageVector =
                    if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                  contentDescription = "बुकमार्क",
                  tint = if (isBookmarked) GoldAccent else Color.White,
                )
              }

              // Share
              IconButton(
                onClick = {
                  viewModel.shareContent(
                    context,
                    day.title,
                    "${day.summary}\n\n${day.spiritualMessage}",
                  )
                },
                modifier = Modifier.size(36.dp).testTag("detail_share_button"),
              ) {
                Icon(
                  imageVector = Icons.Default.Share,
                  contentDescription = "साझा करें",
                  tint = Color.White,
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = day.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = day.skandhaReference,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f),
          )
        }
      }
    }

    // Font Size Quick Selector bar (if expanded)
    item {
      AnimatedVisibility(visible = showFontSelector) {
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "फॉन्ट आकार:",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf(0.9f to "छोटा", 1.0f to "सामान्य", 1.2f to "बड़ा").forEach { (scale, label) ->
                FilterChip(
                  selected = (fontScale == scale),
                  onClick = { viewModel.setFontScale(scale) },
                  label = { Text(label, fontSize = 12.sp) },
                  colors =
                    FilterChipDefaults.filterChipColors(
                      selectedContainerColor = SaffronPrimary,
                      selectedLabelColor = Color.White,
                    ),
                )
              }
            }
          }
        }
      }
    }

    // Audio Player Bar
    item {
      Box(modifier = Modifier.padding(16.dp)) {
        AudioPlayerBar(
          audioEngine = viewModel.audioEngine,
          title = day.title,
          onPlayClick = { viewModel.playCurrentKathaAudio() },
        )
      }
    }

    // 1. भूमिका एवं महात्म्य (Summary Box)
    item {
      Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
          ),
        border = BorderStroke(1.dp, SaffronContainer),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "॥ भूमिका एवं प्रसंग परिचय ॥",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SaffronPrimary,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = day.summary,
            fontSize = (15 * fontScale).sp,
            lineHeight = (22 * fontScale).sp,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }
    }

    // 2. प्रमुख पात्र (Key Characters)
    item {
      Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
          ),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.People,
              contentDescription = null,
              tint = PeacockTeal,
              modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "प्रमुख दिव्य पात्र",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = PeacockTeal,
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            day.keyCharacters.forEach { character ->
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = PeacockTealContainer.copy(alpha = 0.5f),
              ) {
                Text(
                  text = character,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                )
              }
            }
          }
        }
      }
    }

    // 3. विस्तृत कथा (Full Katha Content)
    item {
      Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
          ),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "॥ विस्तृत कथा अमृत ॥",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SaffronPrimary,
          )

          Spacer(modifier = Modifier.height(12.dp))

          val paragraphs = day.fullStory.split("\n\n")
          paragraphs.forEach { para ->
            if (para.startsWith("###")) {
              Text(
                text = para.replace("###", "").trim(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
              )
            } else {
              Text(
                text = para.trim(),
                fontSize = (16 * fontScale).sp,
                lineHeight = (26 * fontScale).sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 4.dp),
              )
            }
          }
        }
      }
    }

    // 4. मुख्य श्लोक एवं भावार्थ
    if (day.keyShlokas.isNotEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
          shape = RoundedCornerShape(14.dp),
          colors =
            CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "॥ दिवस के प्रमुख श्लोक ॥",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            day.keyShlokas.forEach { shloka ->
              Column(
                modifier =
                  Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Text(
                    text = shloka.reference,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                  )
                  IconButton(
                    onClick = {
                      viewModel.copyToClipboard(
                        context,
                        "श्लोक",
                        "${shloka.sanskritText}\n\nअर्थ: ${shloka.hindiMeaning}\n(${shloka.reference})",
                      )
                    },
                    modifier = Modifier.size(24.dp),
                  ) {
                    Icon(
                      imageVector = Icons.Default.ContentCopy,
                      contentDescription = "कॉपी करें",
                      tint = MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(16.dp),
                    )
                  }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = shloka.sanskritText,
                  fontWeight = FontWeight.Bold,
                  fontSize = (15 * fontScale).sp,
                  lineHeight = (22 * fontScale).sp,
                  color = MaterialTheme.colorScheme.onSurface,
                )

                HorizontalDivider(
                  modifier = Modifier.padding(vertical = 8.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant,
                )

                Text(
                  text = "भावार्थ: ${shloka.hindiMeaning}",
                  fontSize = (14 * fontScale).sp,
                  lineHeight = (20 * fontScale).sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
            }
          }
        }
      }
    }

    // 5. आध्यात्मिक संदेश व जीवनोपदेश
    item {
      Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = GoldContainer.copy(alpha = 0.4f),
          ),
        border = BorderStroke(1.dp, GoldAccent),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.SelfImprovement,
              contentDescription = null,
              tint = SaffronPrimary,
              modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "आध्यात्मिक संदेश व जीवनोपदेश",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = day.spiritualMessage,
            fontSize = (15 * fontScale).sp,
            lineHeight = (22 * fontScale).sp,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }
    }

    // 6. Complete Toggle & Next/Previous Days Actions
    item {
      Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // Toggle Completed Button
        Button(
          onClick = { viewModel.toggleDayCompleted(day.dayNumber) },
          colors =
            ButtonDefaults.buttonColors(
              containerColor = if (isCompleted) Color(0xFF2E7D32) else SaffronPrimary,
              contentColor = Color.White,
            ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp).testTag("mark_completed_button"),
        ) {
          Icon(
            imageVector =
              if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isCompleted) "दिवस ${day.dayNumber} पूर्ण (क्लिक कर बदलें)" else "दिवस ${day.dayNumber} पूर्ण चिह्नित करें",
            fontWeight = FontWeight.Bold,
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Previous and Next day navigation
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          if (day.dayNumber > 1) {
            OutlinedButton(
              onClick = { viewModel.previousDay() },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f).testTag("previous_day_button"),
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "पिछला दिन",
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("पिछला दिन")
            }
          } else {
            Spacer(modifier = Modifier.weight(1f))
          }

          Spacer(modifier = Modifier.width(12.dp))

          if (day.dayNumber < 7) {
            Button(
              onClick = { viewModel.nextDay() },
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = SaffronPrimary,
                  contentColor = Color.White,
                ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f).testTag("next_day_button"),
            ) {
              Text("अगला दिन")
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "अगला दिन",
                modifier = Modifier.size(16.dp),
              )
            }
          } else {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }
    }
  }
}
