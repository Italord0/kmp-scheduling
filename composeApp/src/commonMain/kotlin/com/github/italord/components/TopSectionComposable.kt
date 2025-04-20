package com.github.italord.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp_scheduling.composeapp.generated.resources.Res
import kmp_scheduling.composeapp.generated.resources.arvind
import kmp_scheduling.composeapp.generated.resources.ic_clock
import kmp_scheduling.composeapp.generated.resources.ic_video
import kmp_scheduling.composeapp.generated.resources.offering_tree
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TopSectionComposable() {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(vertical = 20.dp).size(36.dp),
            painter = painterResource(resource = Res.drawable.offering_tree),
            contentDescription = null
        )
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        Image(
            modifier = Modifier.padding(top = 20.dp).size(64.dp).clip(CircleShape),
            painter = painterResource(resource = Res.drawable.arvind),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Arvind Menon")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "30 Minute Interview",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        CallInformation(icon = Res.drawable.ic_clock, "30 min")
        Spacer(modifier = Modifier.height(8.dp))
        CallInformation(
            video = true,
            icon = Res.drawable.ic_video,
            text = "Web conferencing details provided upon confirmation."
        )
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp).height(1.dp)
                .background(Color.LightGray)
        )
    }
}

@Composable
private fun CallInformation(icon: DrawableResource, text: String, video: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.padding(horizontal = 4.dp).size(if (video) 18.dp else 14.dp),
                painter = painterResource(resource = icon),
                colorFilter = ColorFilter.tint(color = Color.Gray),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
    }
}