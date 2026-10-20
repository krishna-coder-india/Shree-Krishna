package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun AboutScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  BackHandler { viewModel.handleBack() }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("about_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
  ) {
    // Header
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
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = SaffronPrimary,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "श्रीमद्भागवत सप्ताह परायण",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "ग्रंथ परिचय, परायण नियम एवं शास्त्रीय विधि",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // App Emblem & Holy scripture card
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
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = R.drawable.ic_bhagwat_logo),
              contentDescription = "श्रीमद्भागवत महापुराण",
              modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp)),
              contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "श्रीमद्भागवत महापुराण",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary,
              )
              Text(
                text = "अष्टादश पुराणेषु भागवतं परम्",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Text(
                text = "१२ स्कंध • ३३५ अध्याय • १८,००० श्लोक",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037),
              )
            }
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

          Text(
            text =
              "श्रीमद्भागवत महापुराण हिंदू धर्म के १८ प्रमुख पुराणों में शिरोमणि माना गया है। यह भगवान श्रीकृष्ण का साक्षात वाङ्मय (शब्दावतार) स्वरूप है। महर्षि वेदव्यास जी द्वारा प्रणीत और परमहंस श्री शुकदेव जी द्वारा राजा परीक्षित को गंगा तट पर सुनाया गया यह ज्ञानयज्ञ कलियुग में समस्त तापों से मुक्ति और परम भगवद्-प्रेम की प्राप्ति का सर्वोच्च साधन है।",
            fontSize = 14.sp,
            lineHeight = (22).sp,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Saptah Parayan Niyam (Rules and Guidelines)
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = SaffronPrimary,
              modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "सप्ताह परायण विधि व नियम",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          listOf(
            "१. संकल्प व शुचिता: कथा प्रारम्भ करने से पूर्व पवित्र भाव से हाथ में जल और अक्षत लेकर सात दिवसीय परायण का संकल्प करें।",
            "२. दीप प्रज्वलन: कथा स्थल या पूजा घर में अखण्ड अथवा नित्य घृत का दीपक प्रज्वलित रखें।",
            "३. मंगलाचरण: प्रतिदिन कथा श्रवण अथवा वाचन से पूर्व मंगलाचरण व श्री शुकदेव जी तथा व्यास जी का ध्यान करें।",
            "४. एकाग्र श्रवण: सातों दिनों की कथा को निष्कपट व भक्तिभाव से सुनें। सांसारिक प्रपंचों से दूर रहकर मन को केवल श्रीकृष्ण के चरणों में लगाएं।",
            "५. नित्य आरती व संकीर्तन: प्रतिदिन कथा विश्राम पर 'श्री भागवत भगवान की आरती' गाएं और 'हरे कृष्ण' अथवा 'जय कन्हैया लाल की' का संकीर्तन करें।",
            "६. पूर्णाहुति एवं प्रसाद: सातवें दिवस कथा की पूर्णता पर हवन, ब्राह्मण-भोजन, संकीर्तन और प्रसाद वितरण कर परायण सम्पन्न करें।",
          ).forEach { rule ->
            Text(
              text = rule,
              fontSize = 13.sp,
              lineHeight = 20.sp,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(vertical = 3.dp),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Phala Shruti (Spiritual Fruits)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = SaffronContainer.copy(alpha = 0.4f),
          ),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "॥ फलश्रुति ॥",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SaffronPrimary,
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text =
              "पद्मपुराण के अनुसार, जो श्रद्धापूर्वक श्रीमद्भागवत सप्ताह ज्ञानयज्ञ का अनुष्ठान करता है, उसके पूर्वजों को मोक्ष प्राप्त होता है, गृह में सुख-शांति एवं ऐश्वर्य की वृद्धि होती है, और अंतकाल में भगवान के नित्य गोलोक-वैकुंठ धाम में स्थान मिलता है।",
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }
    }
  }
}
