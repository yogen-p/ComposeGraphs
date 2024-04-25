package com.mryogip.composegraphs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.mryogip.composegraphs.demo.BarGraphDemo
import com.mryogip.composegraphs.ui.theme.ComposeGraphsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeGraphsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    /*val maxRange = 100
                    val yStepSize = 5

                    val barData = listOf(
                        BarData(
                            color = Color.Gray,
                            point = Point(0f, 8f, description = "one"),
                        ),
                        BarData(
                            color = Color.Gray,
                            point = Point(1f, 4f, description = "one"),
                        ),
                        BarData(
                            color = Color.Gray,
                            point = Point(2f, 2f, description = "one"),
                        ),
                        BarData(
                            color = Color.Gray,
                            point = Point(3f, 6f, description = "one"),
                        ),
                        BarData(
                            color = Color.Gray,
                            point = Point(4f, 10f, description = "one"),
                        )
                    )

                    val xAxisData = AxisData.Builder()
                        .axisStepSize(30.dp)
                        .startDrawPadding(50.dp)
                        .steps(barData.size - 1)
                        .bottomPadding(40.dp)
                        .labelData { index -> "I$index" }
                        .build()

                    val yAxisData = AxisData.Builder()
                        .steps(5)
                        .labelAndAxisLinePadding(20.dp)
                        .axisOffset(20.dp)
                        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
                        .build()

                    val chartData = BarChartData(
                        chartData = barData,
                        xAxisData = xAxisData,
                        yAxisData = yAxisData,
                        horizontalExtraSpace = 20.dp,
                        barStyle = BarStyle(
                            barWidth = 50.dp,
                            cornerRadius = 15.dp
                        )
                    )

                    BarChart(
                        barChartData = chartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )*/

                    BarGraphDemo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            }
        }
    }
}