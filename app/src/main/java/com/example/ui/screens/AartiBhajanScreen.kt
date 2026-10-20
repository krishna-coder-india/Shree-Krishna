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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AartiItem
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun AartiBhajanScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val aartis = viewModel.getAartis()
  val fontScale by viewModel.fontScale.collectAsState()
  var selectedCategory by remember { mutableStateOf("सभी") }

  BackHandler { viewModel.handleBack() }

  val categories = listOf("सभी", "आरती", "भजन", "मंगलाचरण")

  val filteredList =
    remember(selectedCategory, aartis) {
      if (selectedCategory == "सभी") aartis
      else aartis.filter { it.category == selectedCategory }
    }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("aarti_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier =
            Modifier.size(36.dp)
              .clip(CircleShape)
              .background(GoldAccent.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = SaffronPrimary,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "आरती, भजन एवं मंगलाचरण",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "भागवत परायण एवं नित्य पूजा हेतु संकलित स्तुतियां",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Category filter chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        categories.forEach { cat ->
          FilterChip(
            selected = (selectedCategory == cat),
            onClick = { selectedCategory = cat },
            label = { Text(cat, fontWeight = FontWeight.Bold) },
            colors =
              FilterChipDefaults.filterChipColors(
                selectedContainerColor = SaffronPrimary,
                selectedLabelColor = Color.White,
              ),
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }

    items(filteredList) { aarti ->
      AartiCard(
        aarti = aarti,
        fontScale = fontScale,
        isBookmarked = viewModel.isBookmarked("aarti_${aarti.id}"),
        onToggleBookmark = { viewModel.toggleBookmark("aarti_${aarti.id}") },
        onCopy = {
          viewModel.copyToClipboard(
            context,
            aarti.title,
            "॥ ${aarti.title} ॥\n\n${aarti.lyrics}",
          )
        },
        onShare = {
          viewModel.shareContent(
            context,
            aarti.title,
            "॥ ${aarti.title} ॥\n\n${aarti.lyrics}",
          )
        },
      )
      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
fun AartiCard(
  aarti: AartiItem,
  fontScale: Float,
  isBookmarked: Boolean,
  onToggleBookmark: () -> Unit,
  onCopy: () -> Unit,
  onShare: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("aarti_card_${aarti.id}"),
    shape = RoundedCornerShape(16.dp),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = GoldContainer,
          ) {
            Text(
              text = aarti.category,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF5D4037),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = aarti.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }

        Row {
          IconButton(onClick = onToggleBookmark, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector =
                if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "बुकमार्क",
              tint = if (isBookmarked) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp),
            )
          }
          IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "कॉपी",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp),
            )
          }
          IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "साझा करें",
              tint = SaffronPrimary,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      }

      if (aarti.significance.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = aarti.significance,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
      )

      Text(
        text = aarti.lyrics,
        fontSize = (15 * fontScale).sp,
        lineHeight = (26 * fontScale).sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}
