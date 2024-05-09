package com.mryogip.compose_graphs.graphs.bar.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mryogip.compose_graphs.graphs.bar.model.BarGraphModel
import com.mryogip.compose_graphs.models.BarGraphType

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    barGraph: BarGraphModel
) {
    Surface(modifier) {
        when (barGraph.barGraphType) {
            BarGraphType.Vertical -> {
                VerticalBarGraph(
                    barGraph = barGraph,
                    modifier = modifier
                )
            }
            BarGraphType.Horizontal -> {

            }
        }
    }
}