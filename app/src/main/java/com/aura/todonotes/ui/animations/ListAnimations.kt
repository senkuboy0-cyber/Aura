package com.aura.todonotes.ui.animations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedListItem(
    visible: Boolean,
    delayMillis: Int = 0,
    onAnimated: (alpha: Float, offsetY: Float) -> Unit
) {
    val animatedValue by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delayMillis),
        label = "listItem"
    )
    onAnimated(animatedValue, ((1f - animatedValue) * 50).dp.value)
}
