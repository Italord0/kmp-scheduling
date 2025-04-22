package com.github.italord.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.italord.components.CallInformation
import com.github.italord.model.ScreenState
import kmp_scheduling.composeapp.generated.resources.Res
import kmp_scheduling.composeapp.generated.resources.ic_calendar
import kmp_scheduling.composeapp.generated.resources.ic_clock
import kmp_scheduling.composeapp.generated.resources.ic_globe
import kmp_scheduling.composeapp.generated.resources.ic_video
import kmp_scheduling.composeapp.generated.resources.offering_tree
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.minutes

@Composable
fun DetailsScreen(
    state: ScreenState, onBack: () -> Unit = { }, onSubmit: (String) -> Unit = { }
) {

    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            IconButton(modifier = Modifier.padding(top = 8.dp), onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Image(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, end = 48.dp).fillMaxWidth()
                    .height(36.dp),
                painter = painterResource(resource = Res.drawable.offering_tree),
                contentDescription = "Back Button"
            )
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "30 Minute Interview",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        CallInformation(icon = Res.drawable.ic_clock, text = "30 min")
        Spacer(modifier = Modifier.height(8.dp))
        CallInformation(
            expanded = true,
            icon = Res.drawable.ic_video,
            text = "Web conferencing details provided upon confirmation."
        )
        Spacer(modifier = Modifier.height(8.dp))
        CallInformation(
            icon = Res.drawable.ic_calendar, text = formatInterval(
                start = Instant.parse(state.timeSelected.orEmpty()).toLocalDateTime(state.timeZone),
                end = Instant.parse(state.timeSelected.orEmpty()).plus(30.minutes)
                    .toLocalDateTime(state.timeZone)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        CallInformation(expanded = true, icon = Res.drawable.ic_globe, text = state.timeZone.id)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            text = "Enter Details",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            text = "Name *",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp)
                .height(56.dp),
            value = nameValue,
            singleLine = true,
            onValueChange = { nameValue = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences, autoCorrectEnabled = true
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF0061F2),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF0061F2)
            )
        )
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            text = "Email *",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp)
                .height(56.dp),
            value = emailValue,
            singleLine = true,
            onValueChange = { emailValue = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF0061F2),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF0061F2)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            text = "By proceeding, you confirm that you have read and agree to OfferingTree's Terms of Use and Privacy Notice."
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onSubmit("{\n" +
                        "    \"utcTime\":\"${state.timeSelected}\"\n" +
                        "    \"name\":\"${nameValue}\"\n" +
                        "    \"email\":\"${emailValue}\"\n" +
                        "}")
            },
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF0061F2), contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Schedule Event",
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatInterval(start: LocalDateTime, end: LocalDateTime): String {
    val dayOfWeek = start.date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = start.date.month.name.lowercase().replaceFirstChar { it.uppercase() }

    fun LocalDateTime.formatTime(): String {
        val hour = if (this.hour % 12 == 0) 12 else this.hour % 12
        val amPm = if (this.hour < 12) "am" else "pm"
        return "$hour:${this.minute.toString().padStart(2, '0')}$amPm"
    }

    return "${start.formatTime()} - ${end.formatTime()}, $dayOfWeek, $month ${start.dayOfMonth}, ${start.year}"
}