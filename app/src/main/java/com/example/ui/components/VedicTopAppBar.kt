package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VedicTopAppBar(
  title: String,
  showBack: Boolean = false,
  onBackClick: () -> Unit = {},
  isAudioPlaying: Boolean = false,
  onAudioBarClick: () -> Unit = {},
  isBookmarked: Boolean? = null,
  onBookmarkClick: (() -> Unit)? = null,
  onShareClick: (() -> Unit)? = null,
  onFontSizeClick: (() -> Unit)? = null,
) {
  Surface(
    tonalElevation = 4.dp,
    shadowElevation = 4.dp,
    color = MaterialTheme.colorScheme.primary,
  ) {
    TopAppBar(
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier.size(32.dp)
                .clip(CircleShape)
                .background(GoldAccent.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = "ॐ",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = title,
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
              ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag("app_bar_title"),
          )
        }
      },
      navigationIcon = {
        if (showBack) {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("back_button"),
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "पीछे जाएं",
              tint = Color.White,
            )
          }
        }
      },
      actions = {
        AnimatedVisibility(visible = isAudioPlaying) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
              Modifier.padding(end = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(GoldAccent)
                .clickable { onAudioBarClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .testTag("active_audio_badge"),
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = "कथा ऑडियो चल रहा है",
              tint = Color.Black,
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "सुन रहे हैं",
              color = Color.Black,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
            )
          }
        }

        if (onFontSizeClick != null) {
          IconButton(
            onClick = onFontSizeClick,
            modifier = Modifier.testTag("font_size_button"),
          ) {
            Icon(
              imageVector = Icons.Default.FormatSize,
              contentDescription = "फॉन्ट आकार बदलें",
              tint = Color.White,
            )
          }
        }

        if (isBookmarked != null && onBookmarkClick != null) {
          IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier.testTag("bookmark_button"),
          ) {
            Icon(
              imageVector =
                if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = if (isBookmarked) "बुकमार्क हटाएं" else "बुकमार्क करें",
              tint = if (isBookmarked) GoldAccent else Color.White,
            )
          }
        }

        if (onShareClick != null) {
          IconButton(
            onClick = onShareClick,
            modifier = Modifier.testTag("share_button"),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "साझा करें",
              tint = Color.White,
            )
          }
        }
      },
      colors =
        TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary,
          titleContentColor = Color.White,
          navigationIconContentColor = Color.White,
          actionIconContentColor = Color.White,
        ),
    )
  }
}
