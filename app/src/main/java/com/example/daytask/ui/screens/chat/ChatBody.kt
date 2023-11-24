package com.example.daytask.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.data.Message
import com.example.daytask.ui.theme.Black
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.MessageUserNameText
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.White
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun ChatBody(
    modifier: Modifier = Modifier,
    messagesList: List<Message>
) {
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = messagesList.size)
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            lazyListState.scrollBy(keyboardHeight.toFloat())
        }
    }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.big)),
        modifier = modifier
            .padding(top = dimensionResource(R.dimen.medium))
            .padding(horizontal = dimensionResource(R.dimen.big))
    ) {
        items(messagesList) {
            MessageText(
                text = it.message,
                active = it.senderId == Firebase.auth.uid
            )
        }
        item { /*bottom padding*/ }
    }
}

@Composable
fun MessageText(
    modifier: Modifier = Modifier,
    text: String,
    active: Boolean
) {
    val textColor = if (active) Black else White
    val backgroundColor = if (active) MainColor else Tertiary

    Box(
        contentAlignment = if (active) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MessageUserNameText.copy(fontWeight = FontWeight.W400),
            color = textColor,
            modifier = Modifier
                .defaultMinSize(minWidth = (LocalConfiguration.current.screenWidthDp / 2).dp)
                .background(backgroundColor)
                .padding(dimensionResource(R.dimen.small))
        )
    }
}