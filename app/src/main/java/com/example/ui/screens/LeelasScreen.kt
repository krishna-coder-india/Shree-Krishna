package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.model.KrishnaLeela
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PeacockTeal
import com.example.ui.theme.PeacockTealContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun LeelasScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val leelas = viewModel.getLeelas()
  val fontScale by viewModel.fontScale.collectAsState()

  BackHandler { viewModel.handleBack() }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("leelas_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
  ) {
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
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = PeacockTeal,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "श्रीकृष्ण की दिव्य लीलाएं",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "बाल चरित्र, गोवर्धन धारण, महारास एवं कृपा प्रसंग",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    items(leelas) { leela ->
      KrishnaLeelaCard(
        leela = leela,
        fontScale = fontScale,
        onGoToDay = { viewModel.selectDay(leela.dayNumber) },
        onShare = {
          viewModel.shareContent(
            context,
            leela.title,
            "${leela.shortDesc}\n\n${leela.fullStory}\n\nआध्यात्मिक सीख: ${leela.moralTeaching}",
          )
        },
      )
      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
fun KrishnaLeelaCard(
  leela: KrishnaLeela,
  fontScale: Float,
  onGoToDay: () -> Unit,
  onShare: () -> Unit,
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier =
      Modifier.fillMaxWidth()
        .clickable { isExpanded = !isExpanded }
        .testTag("leela_card_${leela.id}"),
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
          color = PeacockTealContainer,
        ) {
          Text(
            text = "दिवस ${leela.dayNumber} की लीला",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003730),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          )
        }

        Row {
          IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "साझा करें",
              tint = SaffronPrimary,
              modifier = Modifier.size(18.dp),
            )
          }
          IconButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.size(32.dp),
          ) {
            Icon(
              imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = if (isExpanded) "कम करें" else "विस्तार करें",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = leela.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = leela.shortDesc,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp),
          )

          Text(
            text = leela.fullStory,
            fontSize = (15 * fontScale).sp,
            lineHeight = (23 * fontScale).sp,
            color = MaterialTheme.colorScheme.onSurface,
          )

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "आध्यात्मिक रहस्य एवं सीख:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = SaffronPrimary,
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = leela.moralTeaching,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = onGoToDay,
            colors =
              ButtonDefaults.buttonColors(
                containerColor = SaffronPrimary,
                contentColor = Color.White,
              ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.align(Alignment.End),
          ) {
            Text("दिवस ${leela.dayNumber} की सम्पूर्ण कथा पढ़ें", fontSize = 12.sp)
          }
        }
      }
    }
  }
}
