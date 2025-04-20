package com.github.italord.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.italord.model.AvailableTimesResponse
import com.github.italord.model.ScreenState
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Suppress("MemberExtensionConflict")
@Composable
fun CalendarComposable(
    modifier: Modifier = Modifier,
    screenState: ScreenState,
    onDateClick: (calendarDay: CalendarDay) -> Unit = { },
    onMonthChanged: (yearMonth: YearMonth) -> Unit = { }
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = remember { mutableStateOf(state.firstVisibleMonth) }

    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent = 90f) }
            .filterNotNull()
            .collect { month ->
                if (visibleMonth.value.yearMonth != month.yearMonth) {
                    visibleMonth.value = month
                    onMonthChanged(month.yearMonth)
                }
            }
    }

    val availableDatesState = remember { mutableStateOf<Set<LocalDate>>(emptySet()) }

    LaunchedEffect(screenState.timesResponse) {
        getAvailableDays(screenState, availableDatesState)
    }

    LaunchedEffect(screenState.timeZone) {
        getAvailableDays(screenState, availableDatesState)
    }


    Column(modifier = modifier.background(Color.White)) {
        CalendarTitle(
            modifier = Modifier.fillMaxWidth(),
            currentMonth = visibleMonth.value.yearMonth,
            goToNext = {
                onMonthChanged(state.firstVisibleMonth.yearMonth.nextMonth)
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
            goToPrevious = {
                onMonthChanged(state.firstVisibleMonth.yearMonth.previousMonth)
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            })
        HorizontalCalendar(
            modifier = modifier.padding(horizontal = 32.dp),
            state = state,
            dayContent = { calendarDay ->
                Day(
                    calendarDay,
                    availableDatesState.value.contains(calendarDay.date)
                ) { day ->
                    onDateClick(day)
                }
            },
            monthHeader = {
                Row {
                    for (dayOfWeek in daysOfWeek) {
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                            color = Color.Gray,
                            text = dayOfWeek.name.take(3),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        )
    }
}

private suspend fun getAvailableDays(
    screenState: ScreenState,
    availableDatesState: MutableState<Set<LocalDate>>
) {
    val result = isDateInUtcStringList(
        screenState.timesResponse?.data?.availableTimes ?: emptyList(),
        timeZone = screenState.timeZone
    )
    availableDatesState.value = result
}

@Composable
private fun Day(day: CalendarDay, isAvailable: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = if (isAvailable && day.position == DayPosition.MonthDate) Color(
                    0xFFEBF5FF
                ) else Color.Transparent
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { if (isAvailable) onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor =
            if (isAvailable && day.position == DayPosition.MonthDate) Color(0xFF0763E7) else when (day.position) {
                DayPosition.MonthDate -> if (isAvailable) Color.White else Color.Gray
                DayPosition.InDate, DayPosition.OutDate -> Color.Transparent
            }
        Text(
            text = "${day.date.dayOfMonth}",
            color = textColor,
            fontWeight = if (isAvailable) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp,
        )
    }
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

suspend fun isDateInUtcStringList(
    utcDateTimes: List<String>,
    timeZone: TimeZone
): Set<LocalDate> = withContext(Dispatchers.Default) {
    utcDateTimes.mapNotNull { utcString ->
        runCatching {
            Instant.parse(utcString).toLocalDateTime(timeZone).date
        }.getOrNull()
    }.toSet()
}
