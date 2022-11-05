package com.kostyhub.smartnfts

import androidx.compose.animation.core.animate
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


//@Preview(showBackground = true)
@Composable
fun CircularProgressIndicator_00(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    progress: Float = 0.5f,
    lineWidth: Float = 10f,
    content: @Composable () -> Unit = { }
) {
    //val size: Dp = 50.dp
    Canvas(
        modifier = modifier
            .size(size)
            .graphicsLayer(alpha = 0.99f)
    ) {
        val arcSize = size.toPx() - lineWidth
        val arcOffsetXY = lineWidth / 2 + 0.5f

        drawArc(
            color = Color.Blue,
            startAngle = 270f,
            sweepAngle = progress * 360,
            useCenter = false,
            topLeft = Offset(arcOffsetXY, arcOffsetXY),
            size = Size(arcSize, arcSize),
            style = Stroke(width = lineWidth, cap = StrokeCap.Round)
        )
    }
    Content()
}

/*
@Composable
fun CircularProgressBarPercentages_00(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    progress: Float = 0.5f,
    lineWidth: Float = 10f,
    content: @Composable () -> Unit = { }
) {
    CircularProgressBar_00(modifier, size, progress, lineWidth) {
        Text("${progress * 100}%")
    }
    content()
}*/
