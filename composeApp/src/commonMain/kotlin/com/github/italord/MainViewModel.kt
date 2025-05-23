package com.github.italord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.italord.model.AvailableTimesResponse
import com.github.italord.model.ScreenState
import com.kizitonwose.calendar.core.CalendarDay
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MainViewModel : ViewModel() {
    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val month = currentDateTime.month.name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() }
    val formattedCurrentMonth = "$month${currentDateTime.year}"

    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    fun getDatesFromMonthYear(monthYear: String = formattedCurrentMonth) {
        viewModelScope.launch {
            _screenState.update { screenState ->
                screenState.copy(isLoading = true)
            }
            try {
                val response =
                    httpClient.get("https://5b94bbb0-4b84-4173-8753-c9b46c84fc76.mock.pstmn.io/appointment_availabilities/available_times?start_date_time=2025-05-01T07:00:00&end_date_time=2025-05-31T06:59:59") {
                        headers {
                            append("x-mock-response-name", monthYear)
                        }
                    }.body<AvailableTimesResponse>()
                _screenState.update { screenState ->
                    screenState.copy(timesResponse = response, isLoading = false)
                }
            } catch (e: Exception) {
                println(e)
                _screenState.update { screenState ->
                    screenState.copy(isLoading = false)
                }
            }
        }
    }

    fun changeTimeZone(timeZone: TimeZone) {
        viewModelScope.launch {
            _screenState.update { screenState ->
                screenState.copy(timeZone = timeZone)
            }
        }
    }

    fun selectDate(calendarDay: CalendarDay) {
        viewModelScope.launch {
            _screenState.update { screenState ->
                screenState.copy(dateSelected = calendarDay)
            }
        }
    }

    fun selectTime(instant: Instant) {
        viewModelScope.launch {
            _screenState.update { screenState ->
                screenState.copy(timeSelected = "$instant")
            }
        }
    }
}