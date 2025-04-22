package com.github.italord.model

import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.datetime.TimeZone

data class ScreenState(
    val timesResponse: AvailableTimesResponse? = null,
    val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    val isLoading: Boolean = true,
    val dateSelected: CalendarDay? = null,
    val timeSelected: String? = null
)
