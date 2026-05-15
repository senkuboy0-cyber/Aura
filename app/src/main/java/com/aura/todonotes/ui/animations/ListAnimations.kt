package com.aura.todonotes.ui.animations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

@Composable
fun AnimatedListItem(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable (alpha: Float, scale: Float) -> Unit
) {
    val animatedValue by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 350, delayMillis = delayMillis),
        label = "listItemAnim"
    )
    content(animatedValue, 0.8f + (0.2f * animatedValue))
}

fun Modifier.staggeredListItem(index: Int, isVisible: Boolean): Modifier = composed {
    val animatedValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 350, delayMillis = index * 50),
        label = "staggeredList_$index"
    )
    this
        .alpha(animatedValue)
        .scale(0.8f + (0.2f * animatedValue))
}