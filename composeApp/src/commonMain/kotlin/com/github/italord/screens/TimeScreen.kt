package com.github.italord.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.italord.MainViewModel
import com.github.italord.components.TimeZoneSelector
import com.github.italord.model.ScreenState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimeScreen(
    mainViewModel: MainViewModel,
    screenState: ScreenState,
    onBack: () -> Unit = { },
    onTimeSelected: (Instant) -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                text = screenState.dateSelected?.date?.dayOfWeek?.name.orEmpty().lowercase()
                    .capitalize(
                        Locale.current
                    ),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${
                screenState.dateSelected?.date?.month?.name?.lowercase()?.capitalize(Locale.current)
            } ${screenState.dateSelected?.date?.dayOfMonth}, ${screenState.dateSelected?.date?.year}",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        TimeZoneSelector(screenState = screenState) {
            mainViewModel.changeTimeZone(TimeZone.of(it))
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp).height(1.dp)
                .background(Color.LightGray)
        )

        Text(
            text = "Select a Time",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Duration: 30 min",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(getTimeSlots(screenState) ?: listOf()) { time ->
                Button(
                    onClick = {
                        val utcTime =
                            LocalDateTime.parse(time.first).toInstant(screenState.timeZone)
                        onTimeSelected(utcTime)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color(0xFF0061F2)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF0061F2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = time.second,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

private fun getTimeSlots(screenState: ScreenState): List<Pair<String, String>>? {
    return screenState.timesResponse?.data?.availableTimes?.mapNotNull { utcString ->
        runCatching {
            val instant = Instant.parse(utcString)
            val localDateTime = instant.toLocalDateTime(screenState.timeZone)
            if (localDateTime.date == screenState.dateSelected?.date) {
                val time = localDateTime.time
                val hour = time.hour % 12
                val displayHour = if (hour == 0) 12 else hour
                val minute = time.minute.toString().padStart(2, '0')
                val amPm = if (time.hour < 12) "am" else "pm"
                val formattedTime = "$displayHour:$minute$amPm"

                val key = localDateTime.toString() // or format differently if you want
                key to formattedTime
            } else {
                null
            }
        }.getOrNull()
    }
}

