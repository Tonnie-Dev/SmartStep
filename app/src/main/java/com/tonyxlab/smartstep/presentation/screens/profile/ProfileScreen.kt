package com.tonyxlab.smartstep.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.BodySmallRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onSkipClick: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onGenderClick: () -> Unit = {},
    onHeightClick: () -> Unit = {},
    onWeightClick: () -> Unit = {}
) {
    Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                ProfileTopBar(onSkipClick = onSkipClick)
            },
            containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))

            Text(
                    text = stringResource(R.string.caption_text_profile_desc),
                    style = MaterialTheme.typography.BodyLargeRegular,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))

            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(MaterialTheme.spacing.spaceTwelve))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(MaterialTheme.spacing.spaceTwelve)
                            )
                            .padding(MaterialTheme.spacing.spaceMedium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)
            ) {
                SelectionField(
                        label = stringResource(R.string.label_text_gender),
                        value = stringResource(R.string.menu_text_female),
                        onClick = onGenderClick
                )
                SelectionField(
                        label = stringResource(R.string.menu_text_male),
                        value = stringResource(R.string.height_value),
                        onClick = onHeightClick
                )
                SelectionField(
                        label = stringResource(R.string.weight),
                        value = stringResource(R.string.weight_value),
                        onClick = onWeightClick
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                    onClick = onStartClick,
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.spacing.spaceFifty),
                    shape = RoundedCornerShape(MaterialTheme.spacing.spaceTwelve),
                    colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                    )
            ) {
                Text(
                        text = stringResource(R.string.start),
                        style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
            modifier = modifier,
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                            text = stringResource(R.string.my_profile),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                Text(
                        text = stringResource(R.string.skip),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                                .padding(end = MaterialTheme.spacing.spaceMedium)
                                .clickable { onSkipClick() }
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
            )
    )
}

@Composable
private fun SelectionField(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MaterialTheme.spacing.spaceSmall))
                    .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(MaterialTheme.spacing.spaceSmall)
                    )
                    .clickable { onClick() }
                    .padding(MaterialTheme.spacing.spaceTwelve),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                    text = label,
                    style = MaterialTheme.typography.BodySmallRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                    text = value,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
            )
        }
        Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    SmartStepTheme {
        ProfileScreen()
    }
}
