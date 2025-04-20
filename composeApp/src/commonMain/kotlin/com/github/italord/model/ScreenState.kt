package com.github.italord.model

import kotlinx.datetime.TimeZone

data class ScreenState(
    val timesResponse: AvailableTimesResponse? = null,
    val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    val isLoading: Boolean = true,
)
