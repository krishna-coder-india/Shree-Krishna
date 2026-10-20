package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.KathaDay
import com.example.model.Screen
import com.example.ui.components.SuvicharCard
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.PeacockTeal
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun HomeScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val suvichar by viewModel.currentSuvichar.collectAsState()
  val completedDays by viewModel.completedDays.collectAsState()
  val days = viewModel.getDays()

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("home_screen"),
    contentPadding = PaddingValues(bottom = 96.dp),
  ) {
    // 1. Divine Hero Banner
    item {
      Box(
        modifier =
          Modifier.fillMaxWidth()
            .height(200.dp)
            .background(SaffronPrimary),
      ) {
        Image(
          painter = painterResource(id = R.drawable.banner_krishna),
          contentDescription = "भगवान श्रीकृष्ण वृंदावन में",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )

        // Gradient overlay for sacred readability
        Box(
          modifier =
            Modifier.fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors =
                    listOf(
                      Color.Transparent,
                      Color.Black.copy(alpha = 0.7f),
                    )
                )
              )
        )

        Column(
          modifier =
            Modifier.align(Alignment.BottomStart)
              .padding(16.dp),
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = GoldAccent,
            modifier = Modifier.padding(bottom = 4.dp),
          ) {
            Text(
              text = "श्रीमद्भागवत महापुराण",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            )
          }
          Text(
            text = "सप्ताह ज्ञानयज्ञ परायण",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
          )
          Text(
            text = "सात दिनों में मुक्ति और दिव्य भगवद्-प्रेम का पावन मार्ग",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f),
          )
        }
      }
    }

    // 2. Parayan Progress Tracker Card
    item {
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("progress_card"),
        shape = RoundedCornerShape(16.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
          ),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (completedDays.size == 7) Color(0xFF2E7D32) else SaffronPrimary,
                modifier = Modifier.size(20.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "सप्ताह परायण प्रगति",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
              )
            }
            Text(
              text = "${completedDays.size} / 7 दिन पूर्ण",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          val progress = completedDays.size / 7f
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = SaffronPrimary,
            trackColor = MaterialTheme.colorScheme.surface,
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text =
              if (completedDays.size == 7) {
                "साधुवाद! आपका सात दिवसीय परायण सम्पन्न हुआ। पूर्णाहुति व फलश्रुति पाठ करें।"
              } else {
                "नित्य एक दिवस की कथा का नियमपूर्वक पाठ अथवा श्रवण करें।"
              },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    // 3. Daily Suvichar Card
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        SuvicharCard(
          suvichar = suvichar,
          onNextClick = { viewModel.nextSuvichar() },
          onCopyClick = { text -> viewModel.copyToClipboard(context, "सुविचार", text) },
          onShareClick = { text -> viewModel.shareContent(context, "दैनिक भागवत सुविचार", text) },
        )
      }
    }

    // 4. Quick Nav Features Grid
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
          text = "मुख्य धार्मिक अनुभाग",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          NavActionCard(
            title = "श्रीकृष्ण लीलाएं",
            subtitle = "दिव्य बाल व किशोर लीला",
            icon = Icons.Default.AutoAwesome,
            accentColor = PeacockTeal,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(Screen.LEELAS) },
          )
          NavActionCard(
            title = "प्रमुख श्लोक",
            subtitle = "संस्कृत व सरल भावार्थ",
            icon = Icons.Default.MenuBook,
            accentColor = SaffronPrimary,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(Screen.SHLOKAS) },
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          NavActionCard(
            title = "आरती व भजन",
            subtitle = "कुंजबिहारी, भागवत आरती",
            icon = Icons.Default.MusicNote,
            accentColor = GoldAccent,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(Screen.AARTI_BHAJAN) },
          )
          NavActionCard(
            title = "कथा श्रवण",
            subtitle = "ऑडियो व हिंदी वाचन",
            icon = Icons.Default.VolumeUp,
            accentColor = SaffronPrimary,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(Screen.AUDIO_PLAYER) },
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          NavActionCard(
            title = "बुकमार्क व प्रगति",
            subtitle = "सहेजे गए पाठ व विवरण",
            icon = Icons.Default.Bookmark,
            accentColor = PeacockTeal,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(Screen.BOOKMARKS_PROGRESS) },
          )
          NavActionCard(
            title = "एडमिन पैनल",
            subtitle = "कथा संपादन व नोट्स",
            icon = Icons.Default.AdminPanelSettings,
            accentColor = Color(0xFF6D4C41),
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(Screen.ADMIN) },
          )
        }
      }
    }

    // 5. Seven Days Katha List Section Header
    item {
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = "सात दिवसीय कथा परायण",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "क्रमबद्ध स्कंध एवं पावन प्रसंग",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    // Seven Days List Items
    items(days) { day ->
      KathaDayCard(
        day = day,
        isCompleted = completedDays.contains(day.dayNumber),
        isBookmarked = viewModel.isBookmarked("katha_${day.dayNumber}"),
        onCardClick = { viewModel.selectDay(day.dayNumber) },
        onToggleCompleted = { viewModel.toggleDayCompleted(day.dayNumber) },
        onPlayAudio = { viewModel.playDayAudio(day.dayNumber) },
      )
    }
  }
}

@Composable
fun NavActionCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.height(84.dp).clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxSize().padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.size(40.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(22.dp),
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
fun KathaDayCard(
  day: KathaDay,
  isCompleted: Boolean,
  isBookmarked: Boolean,
  onCardClick: () -> Unit,
  onToggleCompleted: () -> Unit,
  onPlayAudio: () -> Unit,
) {
  Card(
    modifier =
      Modifier.fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
        .clickable { onCardClick() }
        .testTag("katha_card_${day.dayNumber}"),
    shape = RoundedCornerShape(16.dp),
    border =
      if (isCompleted) {
        androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50))
      } else {
        null
      },
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
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isCompleted) Color(0xFFE8F5E9) else SaffronContainer,
          ) {
            Text(
              text = "दिवस ${day.dayNumber}",
              color = if (isCompleted) Color(0xFF2E7D32) else SaffronPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = day.skandhaReference,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        // Completion status toggle
        IconButton(
          onClick = onToggleCompleted,
          modifier = Modifier.size(32.dp).testTag("toggle_complete_${day.dayNumber}"),
        ) {
          Icon(
            imageVector =
              if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = if (isCompleted) "पूर्ण" else "अपूर्ण",
            tint = if (isCompleted) Color(0xFF4CAF50) else Color.Gray,
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = day.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
      )

      Text(
        text = day.subTitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        // Audio button
        Button(
          onClick = onPlayAudio,
          colors =
            ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
              contentColor = MaterialTheme.colorScheme.primary,
            ),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
          modifier = Modifier.testTag("listen_day_${day.dayNumber}"),
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "सुनें",
            modifier = Modifier.size(16.dp),
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "कथा सुनें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Read button
        Button(
          onClick = onCardClick,
          colors =
            ButtonDefaults.buttonColors(
              containerColor = SaffronPrimary,
              contentColor = Color.White,
            ),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
          modifier = Modifier.testTag("read_day_${day.dayNumber}"),
        ) {
          Text(
            text = if (isCompleted) "पुनः पढ़ें" else "कथा पढ़ें",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
          )
        }
      }
    }
  }
}
