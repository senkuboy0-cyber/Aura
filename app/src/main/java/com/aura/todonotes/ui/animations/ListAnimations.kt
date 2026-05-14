package com.aura.todonotes.ui.animations

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedListItem(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable (AnimatedFloat) -> Unit
) {
    val animatedValue by animateFloatAsState_Internal(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delayMillis),
        label = "listItem"
    )
    content(AnimatedFloat(animatedValue))
}

private class AnimatedFloat(private val value: Float) {
    val alpha: Float get() = value
    val scale: Float get() = value
    val offsetY: Dp get() = ((1f - value) * 50).dp
}

@androidx.compose.runtime.Composable
private fun animateFloatAsState_Internal(
    targetValue: Float,
    animationSpec: androidx.compose.animation.core.AnimationSpec<Float>,
    label: String
): androidx.compose.runtime.State<Float> {
    return androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue,
        animationSpec = animationSpec,
        label = label
    )
}
