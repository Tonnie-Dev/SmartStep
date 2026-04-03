package com.tonyxlab.smartstep.presentation.screens.onboarding.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.components.DateSelector
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> StandardWheelPicker(
    selectedValue: T,
    values: List<T>,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = 44.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    selectedTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
    ),
    labelFormatter: (T) -> String = { it.toString() }
) {
    require(visibleItemsCount % 2 == 1) {
        "visibleItemsCount must be an odd number so the picker has a true center row."
    }

    val items = remember(values) { values }
    val centerItemIndex = visibleItemsCount / 2

    val initialSelectedIndex = remember(selectedValue, items) {
        items.indexOf(selectedValue).takeIf { it >= 0 } ?: 0
    }

    val displayItems = remember(items, centerItemIndex) {
        buildList {
            repeat(centerItemIndex) { add(null) }
            addAll(items)
            repeat(centerItemIndex) { add(null) }
        }
    }

    val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = initialSelectedIndex
    )

    val flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState
    )

    val selectedItemIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex.coerceIn(items.indices)
        }
    }

    val selectedItemValue by remember {
        derivedStateOf {
            items[selectedItemIndex]
        }
    }

    LaunchedEffect(selectedItemValue) {
        onValueSelected(selectedItemValue)
    }

    Box(
            modifier = modifier
                    .height(itemHeight * visibleItemsCount),
            contentAlignment = Alignment.Center
    ) {
        Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .align(Alignment.Center)
        )

        LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                    count = displayItems.size,
                    key = { index -> "wheel_item_${displayItems[index]?.hashCode()}_$index" }
            ) { index ->
                val item = displayItems[index]
                val actualIndex = index - centerItemIndex
                val isSelected = item != null && actualIndex == selectedItemIndex

                Box(
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(itemHeight),
                        contentAlignment = Alignment.Center
                ) {
                    Text(
                            text = item?.let(labelFormatter).orEmpty(),
                            style = if (isSelected) selectedTextStyle else textStyle,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.graphicsLayer {
                                alpha = if (item == null) 0f else 1f
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DateSelectorPreview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
        ) {
            DateSelector(
                    selectedYear = 2025,
                    selectedMonth = 11,
                    selectedDay = 30,
                    onEvent = {}
            )
        }
    }
}
