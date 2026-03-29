package com.tonyxlab.smartstep.presentation.screens.onboarding.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import com.tonyxlab.smartstep.presentation.core.utils.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StandardWheelPicker(
    selectedValue: Int,
    values: List<Int>,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = MaterialTheme.spacing.spaceDoubleDp * 22,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    selectedTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
    ),
) {
    require(visibleItemsCount % 2 == 1) {
        "visibleItemsCount must be an odd number so the picker has a true center row."
    }

    require(values.isNotEmpty()) {
        "values must not be empty"
    }

    val items = remember(values) { values }
    val centerItemIndex = visibleItemsCount / 2

    val initialSelectedIndex = remember(selectedValue, items) {
        items.indexOf(selectedValue)
                .takeIf { it >= 0 } ?: 0
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
                    .fillMaxWidth()
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
                    key = { index -> "wheel_item_$index" }
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
                            text = item?.toString()
                                    .orEmpty(),
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

/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StandardWheelPicker(
    selectedValue: Int,
    valuesRange: IntRange,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = MaterialTheme.spacing.spaceDoubleDp * 22,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    selectedTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
    ),
) {
    val items = remember(valuesRange) { valuesRange.toList() }
    val initialSelectedIndex = (selectedValue - valuesRange.first).coerceIn(0, items.size - 1)

    StandardWheelPicker(
            modifier = modifier,
            items = items,
            initialSelectedIndex = initialSelectedIndex,
            onValueSelected = onValueSelected,
            visibleItemsCount = visibleItemsCount,
            itemHeight = itemHeight,
            textStyle = textStyle,
            selectedTextStyle = selectedTextStyle
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StandardWheelPicker(
    items: List<Int>,
    initialSelectedIndex: Int,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = MaterialTheme.spacing.spaceDoubleDp * 22,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    selectedTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
    ),
) {

    require(visibleItemsCount % 2 == 1) {
        "visibleItemsCount must be an odd number so the picker has a true center row."
    }

    require(items.isNotEmpty()) {
        "items must not be empty"
    }

    val centerItemIndex = visibleItemsCount / 2

    */
/**
 * We add empty spacer rows above and below the real items.
 * This makes the selected item naturally sit in the visual center.
 *//*

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

    */
/**
 * Since we added [centerItemIndex] spacer rows at the top,
 * the selected real item maps directly to firstVisibleItemIndex.
 *//*

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
                    .fillMaxWidth()
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
                    key = { index -> "wheel_item_$index" }
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
                            text = item?.toString()
                                    .orEmpty(),
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

@Preview(showBackground = true, backgroundColor = 0xFFEAF3FF)
@Composable
private fun SingleColumnStandardWheelPickerPreview() {

    SmartStepTheme {
        Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
        ) {

            PickerContainer(
                    pickerTitle = stringResource(id = R.string.label_text_height),
                    pickerDescription = stringResource(id = R.string.caption_text_calculate_distance),
                    unitOneText = stringResource(id = R.string.label_text_cm),
                    unitTwoText = stringResource(id = R.string.label_text_ft_in),
                    onCancel = {},
                    onConfirm = {},
                    wheelPicker = {
                        StandardWheelPicker(
                                selectedValue = 170,
                                valuesRange = 100..250,
                                onValueSelected = {},
                        )
                    },
                    onToggleUnitOne = {},
                    isUnitOneSelected = true,
                    onToggleUnitTwo = {}
            )
        }
    }
}
*/
