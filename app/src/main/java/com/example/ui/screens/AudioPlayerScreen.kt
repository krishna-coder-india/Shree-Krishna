package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.PeacockTeal
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun AudioPlayerScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val days = viewModel.getDays()
  val selectedDay by viewModel.selectedDay.collectAsState()
  val currentDay = selectedDay ?: days.first()
  val audioEngine = viewModel.audioEngine

  BackHandler { viewModel.handleBack() }

  // Rotating animation when speaking
  val infiniteTransition = rememberInfiniteTransition(label = "disc_rotate")
  val rotation by
    infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 360f,
      animationSpec =
        infiniteRepeatable(
          animation = tween(durationMillis = 8000, easing = LinearEasing),
          repeatMode = RepeatMode.Restart,
        ),
      label = "rotation",
    )

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("audio_player_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier =
            Modifier.size(36.dp)
              .clip(CircleShape)
              .background(SaffronPrimary.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = null,
            tint = SaffronPrimary,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "श्रीमद्भागवत कथा श्रवण",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "प्रामाणिक हिंदी वाचन एवं शांतिप्रद ध्यान धुन",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Day Selection Carousel
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
      ) {
        items(days) { day ->
          val isSelected = (day.dayNumber == currentDay.dayNumber)
          FilterChip(
            selected = isSelected,
            onClick = {
              if (audioEngine.isSpeaking) audioEngine.stop()
              viewModel.selectDay(day.dayNumber)
            },
            label = {
              Text(
                text = "दिन ${day.dayNumber}",
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              )
            },
            colors =
              FilterChipDefaults.filterChipColors(
                selectedContainerColor = SaffronPrimary,
                selectedLabelColor = Color.White,
              ),
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // Sacred Rotating Aura Disc
    item {
      Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center,
      ) {
        // Outer glowing ring
        Box(
          modifier =
            Modifier.size(190.dp)
              .rotate(if (audioEngine.isSpeaking) rotation else 0f)
              .clip(CircleShape)
              .background(
                Brush.sweepGradient(
                  colors =
                    listOf(
                      SaffronPrimary,
                      GoldAccent,
                      PeacockTeal,
                      GoldAccent,
                      SaffronPrimary,
                    )
                )
              )
        )

        // Center sacred disc
        Box(
          modifier =
            Modifier.size(170.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surface),
          contentAlignment = Alignment.Center,
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "ॐ",
              fontSize = 36.sp,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "दिवस ${currentDay.dayNumber}",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = if (audioEngine.isSpeaking) "श्रवण चालू" else if (audioEngine.isPaused) "विराम" else "तैयार",
              fontSize = 11.sp,
              color = if (audioEngine.isSpeaking) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // Katha Title & Skandha
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
          ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = GoldContainer,
          ) {
            Text(
              text = currentDay.skandhaReference,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF5D4037),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = currentDay.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = currentDay.subTitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Real Engine Status
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
              Modifier.clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 10.dp, vertical = 4.dp),
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = SaffronPrimary,
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "नेटिव हिंदी टेक्स्ट-टू-स्पीच वाचक",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Ambient Tone and Speed Controls
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            // Ambient flute/tanpura toggle
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier =
                Modifier.clip(RoundedCornerShape(16.dp))
                  .background(
                    if (audioEngine.isAmbientMusicOn) PeacockTeal.copy(alpha = 0.15f)
                    else Color.Gray.copy(alpha = 0.15f)
                  )
                  .clickable { audioEngine.toggleAmbientMusic() }
                  .padding(horizontal = 10.dp, vertical = 6.dp),
            ) {
              Icon(
                imageVector =
                  if (audioEngine.isAmbientMusicOn) Icons.Default.MusicNote
                  else Icons.Default.MusicOff,
                contentDescription = null,
                tint = if (audioEngine.isAmbientMusicOn) PeacockTeal else Color.Gray,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (audioEngine.isAmbientMusicOn) "भक्ति नाद: ऑन" else "नाद: ऑफ",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (audioEngine.isAmbientMusicOn) PeacockTeal else Color.Gray,
              )
            }

            // Speed selectors
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              listOf(0.8f to "0.8x", 1.0f to "1.0x", 1.2f to "1.2x").forEach { (rate, label) ->
                val isSelected = (audioEngine.speechRate == rate)
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (isSelected) SaffronPrimary else Color.Transparent,
                  modifier =
                    Modifier.clip(RoundedCornerShape(12.dp))
                      .clickable { audioEngine.setRate(rate) }
                      .padding(2.dp),
                ) {
                  Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Primary Player Controls: Stop, Play/Pause
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            // Stop button
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.size(50.dp),
            ) {
              IconButton(
                onClick = { audioEngine.stop() },
                enabled = audioEngine.isSpeaking || audioEngine.isPaused,
              ) {
                Icon(
                  imageVector = Icons.Default.Stop,
                  contentDescription = "रोकें",
                  tint = if (audioEngine.isSpeaking || audioEngine.isPaused) MaterialTheme.colorScheme.error else Color.Gray,
                )
              }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Play / Pause / Resume
            Surface(
              shape = CircleShape,
              color = SaffronPrimary,
              modifier = Modifier.size(64.dp),
              shadowElevation = 4.dp,
            ) {
              IconButton(
                onClick = {
                  when {
                    audioEngine.isSpeaking -> audioEngine.pause()
                    audioEngine.isPaused -> audioEngine.resume()
                    else -> audioEngine.playKatha(currentDay.dayNumber, currentDay.title, currentDay.fullStory)
                  }
                },
                modifier = Modifier.testTag("full_player_play_pause"),
              ) {
                Icon(
                  imageVector =
                    if (audioEngine.isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                  contentDescription = if (audioEngine.isSpeaking) "विराम दें" else "कथा सुनें",
                  tint = Color.White,
                  modifier = Modifier.size(32.dp),
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Read full text shortcut
    item {
      Button(
        onClick = { viewModel.navigateTo(Screen.KATHA_DETAIL) },
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary,
          ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp),
      ) {
        Icon(
          imageVector = Icons.Default.MenuBook,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "इस दिवस की सम्पूर्ण कथा और श्लोक पढ़ें",
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}
