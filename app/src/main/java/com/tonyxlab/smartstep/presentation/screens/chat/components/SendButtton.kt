package com.tonyxlab.smartstep.presentation.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.ButtonPrimary
import com.tonyxlab.smartstep.presentation.theme.ButtonSecondary
import com.tonyxlab.smartstep.presentation.theme.TextWhite

@Composable
fun SendButton(
    enabled: Boolean,
    onSend: () -> Unit = {}
) {
    val buttonBackground = remember(enabled) {
        if (enabled) ButtonPrimary else ButtonSecondary
    }
    Box(
            modifier = Modifier
                    .size(MaterialTheme.spacing.spaceDoubleDp * 22)
                    .clip(CircleShape)
                    .background(color = buttonBackground)
                    .clickable(enabled = enabled) { onSend() },
            contentAlignment = Alignment.Center
    ) {
        Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = null,

                tint = TextWhite
        )
    }
}
