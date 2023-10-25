package com.example.daytask.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.SquareButton
import com.example.daytask.ui.theme.HelpText
import com.example.daytask.ui.theme.PlaceholderColor
import com.example.daytask.util.TextFieldManager
import com.example.daytask.util.TextFieldManager.clearFocusOnKeyboardDismiss

@Composable
fun HomeSearchBox(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    updateUiState: (HomeUiState) -> Unit,
    updateQuery: (String) -> Unit,
    updateSearchState: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
    val keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
    var showFilterSettings by remember { mutableStateOf(false) }
    val trailingView = @Composable {
        IconButton(
            onClick = {
                updateQuery("")
                focusManager.clearFocus()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close_square),
                contentDescription = null
            )
        }
    }

    Row(modifier) {
        OutlinedTextField(
            value = uiState.query,
            onValueChange = updateQuery,
            placeholder = {
                Text(
                    text = stringResource(R.string.search_tasks),
                    style = HelpText,
                    color = PlaceholderColor
                )
            },
            singleLine = true,
            textStyle = HelpText,
            shape = RectangleShape,
            colors = TextFieldManager.colors(),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search_normal),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            trailingIcon = if (uiState.loadQueryResult) trailingView else null,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.medium))
                .weight(1f)
                .size(dimensionResource(R.dimen.button_height))
                .onFocusEvent { updateSearchState(it.isFocused) }
                .clearFocusOnKeyboardDismiss()
        )
        SearchFilterBox(
            expanded = showFilterSettings,
            onExpandedChange = { showFilterSettings = it },
            uiState = uiState,
            updateUiState = {
                updateUiState(it)
                updateQuery(it.query)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterBox(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    uiState: HomeUiState,
    updateUiState: (HomeUiState) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) },
        modifier = modifier
    ) {
        SquareButton(
            onClick = { onExpandedChange(!expanded) },
            sizeRes = R.dimen.button_height,
            iconRes = R.drawable.ic_setting_4,
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(!expanded) },
            modifier = Modifier.width(dimensionResource(R.dimen.menu_item_width))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small))
            ) {
                HomeMenuItemRow(
                    onClick = { updateUiState(uiState.copy(titleCheck = !uiState.titleCheck)) },
                    textRes = R.string.title,
                    checked = uiState.titleCheck
                )
                HomeMenuItemRow(
                    onClick = { updateUiState(uiState.copy(detailCheck = !uiState.detailCheck)) },
                    textRes = R.string.details,
                    checked = uiState.detailCheck
                )
                HomeMenuItemRow(
                    onClick = { updateUiState(uiState.copy(memberCheck = !uiState.memberCheck)) },
                    textRes = R.string.members,
                    checked = uiState.memberCheck
                )
                HomeMenuItemRow(
                    onClick = { updateUiState(uiState.copy(subTaskCheck = !uiState.subTaskCheck)) },
                    textRes = R.string.subtasks,
                    checked = uiState.subTaskCheck
                )
            }
        }
    }
}

@Composable
fun HomeMenuItemRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @StringRes textRes: Int,
    checked: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(if (checked) R.drawable.ic_ticksquare else R.drawable.ic_square),
            contentDescription = null
        )
        Text(
            text = stringResource(textRes),
            style = HelpText
        )
    }
}