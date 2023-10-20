package com.example.daytask.ui.screens.newtask

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.data.User
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.screens.tools.AddIcon
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.ui.screens.tools.MainButton
import com.example.daytask.ui.theme.SmallInputText
import com.example.daytask.ui.theme.White
import com.example.daytask.util.Status
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object NewTaskDestination : NavigationDestination {
    override val route = "new_task"
    override val titleRes = R.string.create_new_task
}

@Composable
fun NewTaskScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    viewModel: NewTaskViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.updateResult) navigateUp()
    if (uiState.status == Status.Loading) LoadingDialog()

    NewTaskBody(
        uiState = uiState,
        validCreate = viewModel.validNewTask(),
        updateUiState = viewModel::updateUiState,
        saveTask = { viewModel.uploadNewTask(context) },
        modifier = modifier
    )
}

@Composable
fun NewTaskBody(
    modifier: Modifier = Modifier,
    uiState: NewTaskUiState,
    updateUiState: (NewTaskUiState) -> Unit,
    saveTask: () -> Unit,
    validCreate: Boolean
) {
    val state = rememberLazyGridState()

    // Fake user
    val firebaseUser = Firebase.auth.currentUser!!
    val user = User(
        firebaseUser.uid,
        firebaseUser.displayName,
        firebaseUser.photoUrl.toString()
    )

    val spanCount = 7
    val maxCurrentLineSpan = GridItemSpan(spanCount)
    LazyVerticalGrid(
        columns = GridCells.Fixed(spanCount),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
        state = state,
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.big))
    ) {
        item(span = { maxCurrentLineSpan }) {
            ClearInputField(
                headlineText = stringResource(R.string.task_title),
                inputText = uiState.title,
                onValueChange = { updateUiState(uiState.copy(title = it)) },
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.medium),
                        bottom = dimensionResource(R.dimen.big)
                    )
            )
        }
        item(span = { maxCurrentLineSpan }) {
            ClearInputField(
                headlineText = stringResource(R.string.task_details),
                inputText = uiState.details,
                onValueChange = { updateUiState(uiState.copy(details = it)) },
                imeAction = ImeAction.Done,
                textStyle = SmallInputText.copy(White),
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.big))
            )
        }
        item(span = { maxCurrentLineSpan }) {
            NewTaskHeadline(stringResource(R.string.add_team_members))
        }
        items(
            items = uiState.memberList,
            span = { GridItemSpan(spanCount / 2) }
        ) {
            TeamGridCard(
                removeMember = {
                    /*TODO: real user remove*/
                    val mutList = uiState.memberList.toMutableList()
                    mutList.remove(it)
                    updateUiState(uiState.copy(memberList = mutList.toList()))
                },
                userName = it.displayName,
                userPhoto = Uri.parse(it.photoUrl)
            )
        }
        item {
            AddIcon(
                onClick = {
                    /*TODO: real user add*/
                    val mutList = uiState.memberList.toMutableList()
                    mutList.add(user)
                    updateUiState(uiState.copy(memberList = mutList.toList()))
                },
                sizeRes = R.dimen.image_small,
            )
        }
        item(span = { maxCurrentLineSpan }) {
            TimeDateRow(
                headlineText = stringResource(R.string.time_date),
                saveDate = { updateUiState(uiState.copy(date = it)) },
                currentDate = uiState.date,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.big))
            )
        }
        item(span = { maxCurrentLineSpan }) {
            MainButton(
                onClick = saveTask,
                text = stringResource(R.string.create),
                enabled = validCreate,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.big))
            )
        }
    }
}