package com.example.daytask.ui.screens.home

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.daytask.R
import com.example.daytask.data.Task
import com.example.daytask.ui.theme.Black
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.NewTaskHeadlineText
import com.example.daytask.ui.theme.PercentageText
import com.example.daytask.ui.theme.Secondary
import com.example.daytask.ui.theme.SeeAllText
import com.example.daytask.ui.theme.SmallPercentageText
import com.example.daytask.ui.theme.SmallTaskInfoText
import com.example.daytask.ui.theme.White
import com.example.daytask.util.MathManager
import com.example.daytask.util.Status


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    navigateToDetails: (String) -> Unit,
    updateUiState: (HomeUiState) -> Unit,
    updateQuery: (String) -> Unit
) {
    var searchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.padding(horizontal = dimensionResource(R.dimen.big))) {
        HomeSearchBox(
            uiState = uiState,
            updateUiState = updateUiState,
            updateQuery = updateQuery,
            updateSearchState = { searchActive = it },
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.big))
        )
        AnimatedVisibility(visible = !uiState.loadQueryResult) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.big)),
                modifier = Modifier.motionEventSpy { if (searchActive) focusManager.clearFocus() }
            ) {
                HomeContentCards(
                    headlineTextRes = R.string.completed_tasks,
                    onSeeAllClick = { /*TODO: see all Completed Tasks*/ }
                ) {
                    CompletedTasksListRow(
                        status = uiState.status,
                        tasksList = uiState.tasksList.filter { it.taskComplete },
                        navigateToDetails = navigateToDetails
                    )
                }
                HomeContentCards(
                    headlineTextRes = R.string.ongoing_projects,
                    onSeeAllClick = { /*TODO: see all Ongoing Projects*/ }
                ) {
                    ActiveTasksListColumn(
                        status = uiState.status,
                        tasksList = uiState.tasksList.filter { !it.taskComplete },
                        navigateToDetails = navigateToDetails
                    )
                }
            }
        }
        AnimatedVisibility(visible = uiState.loadQueryResult) {
            SearchedTasksListColumn(
                tasksList = uiState.filteredTasksList,
                navigateToDetails = navigateToDetails,
                modifier = Modifier.motionEventSpy { if (searchActive) focusManager.clearFocus() }
            )
        }
    }
}

@Composable
fun HomeContentCards(
    modifier: Modifier = Modifier,
    headlineTextRes: Int,
    onSeeAllClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        HomeHeadline(
            headlineTextRes = headlineTextRes,
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
        )
        content()
    }
}

@Composable
fun HomeHeadline(
    modifier: Modifier = Modifier,
    @StringRes headlineTextRes: Int,
    onSeeAllClick: () -> Unit = {},
    onSeeVisible: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(headlineTextRes),
            style = NewTaskHeadlineText
        )
        if (onSeeVisible) {
            Text(
                text = stringResource(R.string.see_all),
                style = SeeAllText,
                color = MainColor,
                modifier = Modifier
                    .clickable(
                        onClick = onSeeAllClick,
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    )
            )
        }
    }
}

@Composable
fun CompletedTasksListRow(
    modifier: Modifier = Modifier,
    status: Status,
    tasksList: List<Task>,
    navigateToDetails: (String) -> Unit
) {
    if (status == Status.Loading) HomeLoading(modifier)
    else {
        if (tasksList.isEmpty()) {
            EmptyText()
        } else {
            val screenWidth = LocalConfiguration.current.screenWidthDp
            val cardWidth = screenWidth / 2
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
                modifier = modifier
            ) {
                items(tasksList) { task ->
                    val first = task == tasksList.first()
                    CompletedCard(
                        onCardClick = { navigateToDetails(task.id) },
                        title = task.title,
                        memberList = task.memberList,
                        containerColor = if (first) MainColor else Secondary,
                        textColor = if (first) Black else White,
                        cardWidth = cardWidth
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveTasksListColumn(
    modifier: Modifier = Modifier,
    status: Status,
    tasksList: List<Task>,
    navigateToDetails: (String) -> Unit
) {
    if (status == Status.Loading) HomeLoading(modifier)
    else {
        if (tasksList.isEmpty()) {
            EmptyText(modifier)
        } else {
            LazyColumn(modifier = modifier) {
                items(tasksList) { task ->
                    OngoingCard(
                        onCardClick = { navigateToDetails(task.id) },
                        title = task.title,
                        memberList = task.memberList,
                        date = task.date,
                        percentage = MathManager.countCompletePercentage(task.subTasksList),
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
                    )
                }
            }
        }
    }
}

@Composable
fun SearchedTasksListColumn(
    modifier: Modifier = Modifier,
    tasksList: List<Task>,
    navigateToDetails: (String) -> Unit
) {
    Column(modifier) {
        HomeHeadline(
            headlineTextRes = R.string.searched_tasks,
            onSeeVisible = false,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
        )
        if (tasksList.isNotEmpty()) {
            LazyColumn {
                items(tasksList.sortedBy { !it.taskComplete }) { task ->
                    TaskCard(
                        onCardClick = { navigateToDetails(task.id) },
                        memberListSize = task.memberList.size,
                        completed = task.taskComplete,
                        percent = if (task.taskComplete) 100 else MathManager.countCompletePercentage(
                            task.subTasksList
                        )
                            .toInt(),
                        title = task.title,
                        date = task.date,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
                    )
                }
            }
        } else {
            EmptyText()
        }
    }
}

@Composable
fun EmptyText(
    modifier: Modifier = Modifier
) {
    /*TODO Nice empty Box*/
    Text(
        text = stringResource(R.string.empty),
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun CompleteLine(
    modifier: Modifier = Modifier,
    textColor: Color,
    lineWidth: Float
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.mini))
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.completed),
                style = SmallTaskInfoText,
                color = textColor
            )
            Text(
                text = stringResource(R.string._100),
                style = SmallPercentageText,
                color = textColor
            )
        }
        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawLine(
                color = textColor,
                start = Offset(0f, 0f),
                end = Offset(lineWidth, 0f),
                strokeWidth = 16f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun CompleteCircle(
    modifier: Modifier = Modifier,
    @DimenRes sizeRes: Int,
    percentage: Float
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(dimensionResource(sizeRes))
    ) {
        Canvas(modifier = Modifier.size(dimensionResource(sizeRes))) {
            drawArc(
                color = MainColor,
                startAngle = -30f,
                sweepAngle = -360f * percentage,
                useCenter = false,
                style = Stroke(
                    width = 8f,
                    cap = StrokeCap.Round
                )
            )
        }
        Text(
            text = stringResource(R.string.percentage, (percentage * 100).toInt()),
            style = PercentageText,
            color = White
        )
    }
}

@Composable
fun HomeLoading(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}