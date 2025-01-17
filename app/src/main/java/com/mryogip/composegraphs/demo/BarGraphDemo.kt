package com.mryogip.composegraphs.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mryogip.compose_graphs.graphs.bar.model.BarGraphModel
import com.mryogip.compose_graphs.graphs.bar.model.BarModel
import com.mryogip.compose_graphs.graphs.bar.model.BarStyle
import com.mryogip.compose_graphs.graphs.bar.ui.BarGraph
import com.mryogip.compose_graphs.models.AxisModel
import com.mryogip.compose_graphs.models.Point

@Composable
fun BarGraphDemo() {
    val maxRange = 100
    val yStepSize = 5

    val barItems = listOf(
        BarModel(
            color = Color(0xFF007AFF),
            point = Point(0f, 8f),
        ),
        BarModel(
            color = Color(0xFF007AFF),
            point = Point(1f, 4f),
        ),
        BarModel(
            color = Color(0xFF007AFF),
            point = Point(2f, 2f),
        ),
        BarModel(
            color = Color(0xFF007AFF),
            point = Point(3f, 6f),
        ),
        BarModel(
            color = Color(0xFF007AFF),
            point = Point(4f, 10f),
        )
    )

    val xAxisModel = AxisModel.Builder()
        .axisStepSize(30.dp)
        .steps(barItems.size)
        .bottomPadding(20.dp)
        .firstItemOffset(40.dp)
        .axisOccupiesFullWidth(true)
        .labelData { idx -> "T$idx" }
        .build()

    val yAxisModel = AxisModel.Builder()
        .steps(barItems.size)
        .labelPadding(20.dp)
        .backgroundColor(Color.White)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    val barGraph = BarGraphModel(
        items = barItems,
        xAxisModel = xAxisModel,
        yAxisModel = yAxisModel,
        barStyle = BarStyle(
            barWidth = 50.dp,
            topLeftRadius = 15.dp,
            topRightRadius = 15.dp
        )
    )

    BarGraph(
        barGraph = barGraph,
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    )
}