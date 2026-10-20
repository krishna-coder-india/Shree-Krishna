package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.KathaAudioEngine
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.PeacockTeal
import com.example.ui.theme.SaffronPrimary

@Composable
fun AudioPlayerBar(
  audioEngine: KathaAudioEngine,
  title: String,
  onPlayClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth().testTag("audio_player_bar"),
    shape = RoundedCornerShape(16.dp),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f),
        ) {
          Surface(
            shape = CircleShape,
            color = if (audioEngine.isSpeaking) GoldAccent else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp),
          ) {
            Icon(
              imageVector = Icons.Default.MusicNote,
              contentDescription = "कथा ऑडियो",
              tint = if (audioEngine.isSpeaking) Color.Black else Color.White,
              modifier = Modifier.padding(8.dp),
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "कथा श्रवण (ऑडियो व वाचन)",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = title,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Playback Controls Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        // Devotional Ambient Flute/Drone Toggle
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier =
            Modifier.clip(RoundedCornerShape(20.dp))
              .background(
                if (audioEngine.isAmbientMusicOn) PeacockTeal.copy(alpha = 0.15f)
                else Color.Gray.copy(alpha = 0.15f)
              )
              .clickable { audioEngine.toggleAmbientMusic() }
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("toggle_ambient_music"),
        ) {
          Icon(
            imageVector =
              if (audioEngine.isAmbientMusicOn) Icons.Default.MusicNote
              else Icons.Default.MusicOff,
            contentDescription = "भक्ति धुन",
            tint = if (audioEngine.isAmbientMusicOn) PeacockTeal else Color.Gray,
            modifier = Modifier.size(16.dp),
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (audioEngine.isAmbientMusicOn) "भक्ति नाद: चालू" else "भक्ति नाद: बंद",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (audioEngine.isAmbientMusicOn) PeacockTeal else Color.Gray,
          )
        }

        // Speed chips
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

        // Primary Play / Pause / Stop buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (audioEngine.isSpeaking || audioEngine.isPaused) {
            IconButton(
              onClick = { audioEngine.stop() },
              modifier = Modifier.size(38.dp).testTag("audio_stop_button"),
            ) {
              Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "रोकें",
                tint = MaterialTheme.colorScheme.error,
              )
            }
          }

          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(44.dp),
          ) {
            IconButton(
              onClick = {
                when {
                  audioEngine.isSpeaking -> audioEngine.pause()
                  audioEngine.isPaused -> audioEngine.resume()
                  else -> onPlayClick()
                }
              },
              modifier = Modifier.testTag("audio_play_pause_button"),
            ) {
              Icon(
                imageVector =
                  if (audioEngine.isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (audioEngine.isSpeaking) "विराम दें" else "सुनें",
                tint = Color.White,
              )
            }
          }
        }
      }
    }
  }
}
