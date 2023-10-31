package com.example.daytask.ui.screens.newtask

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.TaskGrid
import com.example.daytask.util.FakeManager

@Composable
fun NewTaskBody(
    modifier: Modifier = Modifier,
    uiState: NewTaskUiState,
    updateUiState: (NewTaskUiState) -> Unit,
    saveTask: () -> Unit,
    validCreate: Boolean
) {
    val state = rememberLazyGridState()
    val spanCount = 7

    TaskGrid(
        spanCount = spanCount,
        state = state,
        firstInput = uiState.title,
        firstOnChange = { updateUiState(uiState.copy(title = it)) },
        secondInput = uiState.details,
        secondOnChange = { updateUiState(uiState.copy(details = it)) },
        teamHeadline = stringResource(R.string.add_team_members),
        membersList = uiState.memberList,
        addMember = {
            /*TODO: real user add*/
            updateUiState(
                uiState.copy(
                    memberList = uiState.memberList.plus(
                        FakeManager.fakeUser
                    )
                )
            )
        },
        removeMember = {
            /*TODO: real user remove*/
            updateUiState(
                uiState.copy(
                    memberList = uiState.memberList.minus(it)
                )
            )
        },
        date = uiState.date,
        saveDate = { updateUiState(uiState.copy(date = it)) },
        buttonHeadline = stringResource(R.string.create_new_task),
        buttonAction = saveTask,
        buttonEnable = validCreate,
        modifier = modifier
    )
}