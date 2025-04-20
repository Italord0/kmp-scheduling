package com.github.italord.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kmp_scheduling.composeapp.generated.resources.Res
import kmp_scheduling.composeapp.generated.resources.ic_globe
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun TimeZoneSelector(onTimeZoneSelected: (timeZoneId : String) -> Unit = { }) {
    var selectedTimeZone by remember { mutableStateOf(TimeZone.currentSystemDefault().id) }
    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Time zone",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF1A2D4A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isDialogOpen = true }
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_globe),
                contentDescription = "Globe icon",
                tint = Color(0xFF1A2D4A)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$selectedTimeZone (${currentTimeFor(selectedTimeZone)})",
                fontSize = 14.sp,
                color = Color(0xFF1A2D4A)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown icon",
                tint = Color(0xFF1A2D4A)
            )
        }
    }

    if (isDialogOpen) {
        TimeZoneDialog(
            onDismiss = { isDialogOpen = false },
            onTimeZoneSelected = {
                onTimeZoneSelected(it)
                selectedTimeZone = it
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun TimeZoneDialog(
    onDismiss: () -> Unit,
    onTimeZoneSelected: (String) -> Unit
) {
    val timeZones = remember { TimeZone.availableZoneIds.toList() }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("Select a Time Zone", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(timeZones) { timeZone ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onTimeZoneSelected(timeZone)
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(timeZone, fontSize = 14.sp)
                            Text(
                                "Current time: ${currentTimeFor(timeZone)}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

fun currentTimeFor(timeZoneId: String): String {
    val timeZone = TimeZone.of(timeZoneId)
    val now = Clock.System.now().toLocalDateTime(timeZone)
    val hour = if (now.hour % 12 == 0) 12 else now.hour % 12
    val amPm = if (now.hour < 12) "am" else "pm"
    val minute = "${now.minute}".padStart(2, '0')
    return "$hour:$minute$amPm"
}

@Preview
@Composable
fun TimeZoneSelectorPreview(){
    Surface(modifier = Modifier.background(Color.White)) {
        TimeZoneSelector {  }
    }
}

@Preview
@Composable
fun TimeZoneDialogPreview(){
    TimeZoneDialog(onDismiss = {}, onTimeZoneSelected = { })
}
