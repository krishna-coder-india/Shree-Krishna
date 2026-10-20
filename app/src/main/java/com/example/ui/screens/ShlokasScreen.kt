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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.model.Shloka
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun ShlokasScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val shlokas = viewModel.getAllShlokas()
  var searchQuery by remember { mutableStateOf("") }
  val fontScale by viewModel.fontScale.collectAsState()

  BackHandler { viewModel.handleBack() }

  val filteredShlokas =
    remember(searchQuery, shlokas) {
      if (searchQuery.isBlank()) {
        shlokas
      } else {
        shlokas.filter {
          it.sanskritText.contains(searchQuery, ignoreCase = true) ||
            it.hindiMeaning.contains(searchQuery, ignoreCase = true) ||
            it.reference.contains(searchQuery, ignoreCase = true)
        }
      }
    }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("shlokas_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
  ) {
    // Header & Description
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier =
            Modifier.size(36.dp)
              .clip(CircleShape)
              .background(SaffronPrimary.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = null,
            tint = SaffronPrimary,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "श्रीमद्भागवत के प्रमुख श्लोक",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "संस्कृत श्लोक, स्कंध संदर्भ एवं सरल हिंदी भावार्थ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Search bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("श्लोक, भावार्थ या संदर्भ खोजें...", fontSize = 14.sp) },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Search, contentDescription = "खोजें")
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "मिटाएं")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors =
          OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SaffronPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
          ),
        modifier = Modifier.fillMaxWidth().testTag("shloka_search_field"),
      )

      Spacer(modifier = Modifier.height(14.dp))
    }

    if (filteredShlokas.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
          colors =
            CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Icon(
              imageVector = Icons.Default.MenuBook,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "कोई श्लोक नहीं मिला",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = "कृपया दूसरा शब्द लिखकर खोजें",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    } else {
      items(filteredShlokas) { shloka ->
        ShlokaDetailCard(
          shloka = shloka,
          fontScale = fontScale,
          isBookmarked = viewModel.isBookmarked("shloka_${shloka.id}"),
          onToggleBookmark = { viewModel.toggleBookmark("shloka_${shloka.id}") },
          onCopy = {
            viewModel.copyToClipboard(
              context,
              "श्लोक",
              "${shloka.sanskritText}\n\nभावार्थ: ${shloka.hindiMeaning}\n(${shloka.reference})",
            )
          },
          onShare = {
            viewModel.shareContent(
              context,
              "श्रीमद्भागवत श्लोक",
              "${shloka.sanskritText}\n\nभावार्थ: ${shloka.hindiMeaning}\n(${shloka.reference})",
            )
          },
        )
        Spacer(modifier = Modifier.height(10.dp))
      }
    }
  }
}

@Composable
fun ShlokaDetailCard(
  shloka: Shloka,
  fontScale: Float,
  isBookmarked: Boolean,
  onToggleBookmark: () -> Unit,
  onCopy: () -> Unit,
  onShare: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("shloka_card_${shloka.id}"),
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
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = GoldContainer,
        ) {
          Text(
            text = shloka.reference,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          )
        }

        Row {
          IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.size(32.dp),
          ) {
            Icon(
              imageVector =
                if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "बुकमार्क",
              tint = if (isBookmarked) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp),
            )
          }
          IconButton(
            onClick = onCopy,
            modifier = Modifier.size(32.dp),
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "कॉपी",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp),
            )
          }
          IconButton(
            onClick = onShare,
            modifier = Modifier.size(32.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "साझा करें",
              tint = SaffronPrimary,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = shloka.sanskritText,
        fontWeight = FontWeight.Bold,
        fontSize = (16 * fontScale).sp,
        lineHeight = (24 * fontScale).sp,
        color = MaterialTheme.colorScheme.onSurface,
      )

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
      )

      Text(
        text = "सरल हिंदी भावार्थ:",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = SaffronPrimary,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = shloka.hindiMeaning,
        fontSize = (14 * fontScale).sp,
        lineHeight = (22 * fontScale).sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
