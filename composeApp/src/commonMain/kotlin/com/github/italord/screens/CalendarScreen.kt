package com.github.italord.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.github.italord.MainViewModel
import com.github.italord.components.CalendarComposable
import com.github.italord.components.TimeZoneSelector
import com.github.italord.components.TopSectionComposable
import com.github.italord.model.ScreenState
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.datetime.TimeZone

@Composable
fun CalendarScreen(
    mainViewModel: MainViewModel,
    screenState: ScreenState,
    onDateSelected: (calendarDay: CalendarDay) -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        TopSectionComposable()
        CalendarComposable(
            screenState = screenState,
            onMonthChanged = { yearMonth ->
                mainViewModel.getDatesFromMonthYear(
                    "${
                        yearMonth.month.name.lowercase().capitalize(Locale.current).take(3)
                    }${yearMonth.year}"
                )
            },
            onDateClick = { clickedDay ->
                onDateSelected(clickedDay)
            })
        TimeZoneSelector(
            screenState = screenState
        ) {
            mainViewModel.changeTimeZone(TimeZone.of(it))
        }
    }
}