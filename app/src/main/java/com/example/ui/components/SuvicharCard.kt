package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Suvichar
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary

@Composable
fun SuvicharCard(
  suvichar: Suvichar,
  onNextClick: () -> Unit,
  onCopyClick: (String) -> Unit,
  onShareClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth().testTag("suvichar_card"),
    shape = RoundedCornerShape(18.dp),
    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f)),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(
      modifier =
        Modifier.fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  SaffronContainer.copy(alpha = 0.35f),
                  MaterialTheme.colorScheme.surface,
                )
            )
          )
          .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier.size(28.dp)
                .clip(CircleShape)
                .background(SaffronPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = SaffronPrimary,
              modifier = Modifier.size(16.dp),
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "दैनिक भक्ति अमृत व सुविचार",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = SaffronPrimary,
          )
        }

        IconButton(
          onClick = onNextClick,
          modifier = Modifier.size(32.dp).testTag("next_suvichar_button"),
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "अगला विचार",
            tint = SaffronPrimary,
            modifier = Modifier.size(18.dp),
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
          imageVector = Icons.Default.FormatQuote,
          contentDescription = null,
          tint = GoldAccent,
          modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = suvichar.quote,
          style = MaterialTheme.typography.bodyLarge,
          fontStyle = FontStyle.Italic,
          lineHeight = 24.sp,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f),
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "— ${suvichar.source}",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row {
          IconButton(
            onClick = { onCopyClick("${suvichar.quote}\n— ${suvichar.source}") },
            modifier = Modifier.size(32.dp).testTag("copy_suvichar_button"),
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "कॉपी करें",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp),
            )
          }
          IconButton(
            onClick = { onShareClick("${suvichar.quote}\n— ${suvichar.source}") },
            modifier = Modifier.size(32.dp).testTag("share_suvichar_button"),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "साझा करें",
              tint = SaffronPrimary,
              modifier = Modifier.size(16.dp),
            )
          }
        }
      }
    }
  }
}
