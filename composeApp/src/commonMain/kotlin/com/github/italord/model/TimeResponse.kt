package com.github.italord.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableTimesResponse(
    val data: AvailableTimesData
)

@Serializable
data class AvailableTimesData(
    @SerialName("available_times")
    val availableTimes: List<String>
)
