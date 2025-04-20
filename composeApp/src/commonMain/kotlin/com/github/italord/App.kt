package com.github.italord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.italord.components.CalendarComposable
import com.github.italord.components.TopSectionComposable

@Composable
fun App() {
    Column(modifier = Modifier.background(Color.White)) {
        TopSectionComposable()
        CalendarComposable { clickedDay ->
            println(clickedDay)
        }
    }
}