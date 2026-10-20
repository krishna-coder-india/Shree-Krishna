package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Shloka
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BhagwatViewModel

@Composable
fun AdminScreen(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val isAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
  var pinInput by remember { mutableStateOf("") }
  var pinError by remember { mutableStateOf(false) }

  BackHandler { viewModel.handleBack() }

  if (!isAuthenticated) {
    // PIN Authentication Gate
    Column(
      modifier =
        modifier.fillMaxSize()
          .padding(24.dp)
          .testTag("admin_pin_screen"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Box(
        modifier =
          Modifier.size(72.dp)
            .clip(CircleShape)
            .background(SaffronPrimary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = null,
          tint = SaffronPrimary,
          modifier = Modifier.size(36.dp),
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "सुरक्षित एडमिन पैनल",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
      )

      Text(
        text = "कथा सामग्री और श्लोक प्रबंधन हेतु 4-अंकीय एडमिन पिन दर्ज करें।",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
      )

      OutlinedTextField(
        value = pinInput,
        onValueChange = {
          if (it.length <= 6) {
            pinInput = it
            pinError = false
          }
        },
        label = { Text("एडमिन पिन दर्ज करें") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        isError = pinError,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors =
          OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SaffronPrimary,
          ),
        modifier = Modifier.fillMaxWidth(0.7f).testTag("admin_pin_input"),
      )

      if (pinError) {
        Text(
          text = "अमान्य पिन! कृपया सही पिन दर्ज करें।",
          color = MaterialTheme.colorScheme.error,
          fontSize = 12.sp,
          modifier = Modifier.padding(top = 6.dp),
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "प्रारंभिक डिफ़ॉल्ट पिन: 1008",
        fontSize = 11.sp,
        color = SaffronPrimary,
        fontWeight = FontWeight.SemiBold,
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          val success = viewModel.verifyAdminPin(pinInput)
          if (!success) {
            pinError = true
            Toast.makeText(context, "गलत पिन!", Toast.LENGTH_SHORT).show()
          } else {
            pinInput = ""
          }
        },
        colors =
          ButtonDefaults.buttonColors(
            containerColor = SaffronPrimary,
            contentColor = Color.White,
          ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(0.7f).height(46.dp).testTag("admin_login_button"),
      ) {
        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("प्रवेश करें", fontWeight = FontWeight.Bold)
      }
    }
  } else {
    // Authenticated Admin Dashboard
    AdminDashboard(viewModel = viewModel, modifier = modifier)
  }
}

@Composable
fun AdminDashboard(
  viewModel: BhagwatViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val days = viewModel.getDays()
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("कथा संपादन", "नया श्लोक", "प्रवचन नोट्स", "सेटिंग्स")

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("admin_dashboard"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
  ) {
    // Top Status Header with Logout
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier.size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF2E7D32).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color(0xFF2E7D32),
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "अधिकृत एडमिन पैनल",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = "कथा सामग्री संपादन एवं प्रकाशन मोड",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF2E7D32),
              fontWeight = FontWeight.SemiBold,
            )
          }
        }

        OutlinedButton(
          onClick = { viewModel.logoutAdmin() },
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
        ) {
          Text("लॉगआउट", fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = SaffronPrimary,
        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Tab 1: Edit 7 Days Katha Content
    if (selectedTab == 0) {
      item {
        var editingDayNum by remember { mutableIntStateOf(1) }
        val currentEditingDay = days.find { it.dayNumber == editingDayNum } ?: days.first()
        var storyText by remember(editingDayNum) { mutableStateOf(currentEditingDay.fullStory) }

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "दिवस चुनें और कथा संपादित करें:",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              (1..7).forEach { d ->
                FilterChip(
                  selected = (editingDayNum == d),
                  onClick = { editingDayNum = d },
                  label = { Text("दिन $d", fontSize = 11.sp) },
                  colors =
                    FilterChipDefaults.filterChipColors(
                      selectedContainerColor = SaffronPrimary,
                      selectedLabelColor = Color.White,
                    ),
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = currentEditingDay.title,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = storyText,
              onValueChange = { storyText = it },
              label = { Text("कथा पाठ (Markdown / पैराग्राफ)") },
              modifier = Modifier.fillMaxWidth().height(260.dp),
              shape = RoundedCornerShape(12.dp),
              colors =
                OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = SaffronPrimary,
                ),
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                viewModel.saveKathaEdit(editingDayNum, storyText)
                Toast.makeText(context, "दिवस $editingDayNum की कथा सुरक्षित की गई!", Toast.LENGTH_SHORT).show()
              },
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = SaffronPrimary,
                  contentColor = Color.White,
                ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_katha_edit_button"),
            ) {
              Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("परिवर्तन सुरक्षित करें", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Tab 2: Add New Shloka
    if (selectedTab == 1) {
      item {
        var sanskritInput by remember { mutableStateOf("") }
        var meaningInput by remember { mutableStateOf("") }
        var referenceInput by remember { mutableStateOf("") }

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "नया संस्कृत श्लोक एवं भावार्थ जोड़ें",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = sanskritInput,
              onValueChange = { sanskritInput = it },
              label = { Text("संस्कृत श्लोक (देवनागरी)") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = meaningInput,
              onValueChange = { meaningInput = it },
              label = { Text("सरल हिंदी भावार्थ") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = referenceInput,
              onValueChange = { referenceInput = it },
              label = { Text("स्कंध / अध्याय संदर्भ (उदा. श्रीमद्भागवतम् १०.२९.१)") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                if (sanskritInput.isNotBlank() && meaningInput.isNotBlank()) {
                  val newShloka =
                    Shloka(
                      id = "custom_sh_${System.currentTimeMillis()}",
                      sanskritText = sanskritInput,
                      hindiMeaning = meaningInput,
                      reference = referenceInput.ifBlank { "श्रीमद्भागवतम्" },
                      category = "एडमिन श्लोक",
                    )
                  viewModel.addCustomShloka(newShloka)
                  sanskritInput = ""
                  meaningInput = ""
                  referenceInput = ""
                  Toast.makeText(context, "नया श्लोक सफलतापूर्वक जोड़ा गया!", Toast.LENGTH_SHORT).show()
                } else {
                  Toast.makeText(context, "कृपया श्लोक और अर्थ दोनों दर्ज करें", Toast.LENGTH_SHORT).show()
                }
              },
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = SaffronPrimary,
                  contentColor = Color.White,
                ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth().height(46.dp),
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("श्लोक संग्रह में जोड़ें", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Tab 3: Custom Pravachan Notes
    if (selectedTab == 2) {
      item {
        val notes by viewModel.customPravachanNotes.collectAsState()
        var newNote by remember { mutableStateOf("") }

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "कथावाचक प्रवचन बिंदु व विशेष टिप्पणियां",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = newNote,
              onValueChange = { newNote = it },
              label = { Text("नई व्याख्या / प्रवचन बिंदु...") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
              onClick = {
                if (newNote.isNotBlank()) {
                  viewModel.addPravachanNote(newNote)
                  newNote = ""
                  Toast.makeText(context, "प्रवचन बिंदु सुरक्षित!", Toast.LENGTH_SHORT).show()
                }
              },
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = SaffronPrimary,
                  contentColor = Color.White,
                ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.align(Alignment.End),
            ) {
              Text("नोट जोड़ें")
            }

            if (notes.isNotEmpty()) {
              HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
              Text(
                text = "सहेजे गए प्रवचन बिंदु:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
              )
              Spacer(modifier = Modifier.height(8.dp))
              notes.forEachIndexed { index, note ->
                Row(
                  modifier =
                    Modifier.fillMaxWidth()
                      .padding(vertical = 4.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(MaterialTheme.colorScheme.surfaceVariant)
                      .padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Text(
                    text = "${index + 1}. $note",
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f),
                  )
                  IconButton(
                    onClick = { viewModel.removePravachanNote(index) },
                    modifier = Modifier.size(28.dp),
                  ) {
                    Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "हटाएं",
                      tint = MaterialTheme.colorScheme.error,
                      modifier = Modifier.size(16.dp),
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Tab 4: Security & Reset
    if (selectedTab == 3) {
      item {
        var newPin by remember { mutableStateOf("") }

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "एडमिन सुरक्षा व डेटा रीसेट",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "नया एडमिन पिन बदलें:",
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
              value = newPin,
              onValueChange = { if (it.length <= 6) newPin = it },
              label = { Text("नया 4-6 अंकीय पिन") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
              onClick = {
                if (newPin.length >= 4) {
                  viewModel.changeAdminPin(newPin)
                  newPin = ""
                  Toast.makeText(context, "पिन सफलतापूर्वक बदल दिया गया!", Toast.LENGTH_SHORT).show()
                } else {
                  Toast.makeText(context, "पिन कम से कम 4 अंकों का होना चाहिए", Toast.LENGTH_SHORT).show()
                }
              },
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = SaffronPrimary,
                  contentColor = Color.White,
                ),
              shape = RoundedCornerShape(10.dp),
            ) {
              Text("पिन अपडेट करें")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
              text = "मूल शास्त्रीय स्वरूप में रीसेट करें:",
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
            )
            Text(
              text = "यदि आप सभी स्थानीय संशोधनों को हटाकर मूल प्रामाणिक भागवत पाठ पर लौटना चाहते हैं:",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
              onClick = {
                viewModel.resetToDefaultScriptures()
                Toast.makeText(context, "मूल प्रामाणिक पाठ बहाल किया गया!", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(10.dp),
            ) {
              Icon(imageVector = Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("मूल प्रामाणिक पाठ रीसेट करें")
            }
          }
        }
      }
    }
  }
}
