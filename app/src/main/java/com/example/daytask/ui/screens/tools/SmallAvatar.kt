package com.example.daytask.ui.screens.tools

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.data.User
import com.example.daytask.ui.theme.HelpColor
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.PercentageText

@Composable
fun SmallAvatar(
    modifier: Modifier = Modifier,
    photoUrl: String?
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(1.dp, MainColor, CircleShape)
    ) {
        AvatarImage(
            userPhoto = Uri.parse(photoUrl ?: ""),
            avatarSizeRes = R.dimen.image_tiny
        )
    }
}

@Composable
fun SmallAvatarsRow(
    modifier: Modifier = Modifier,
    memberList: List<User>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-7).dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (memberList.size <= 5) memberList.forEach { SmallAvatar(photoUrl = it.photoUrl) }
        else {
            var index = 0
            memberList.forEach {
                if (index == 5) return@forEach
                SmallAvatar(photoUrl = it.photoUrl)
                index++
            }
            Text(
                text = stringResource(R.string.members_count, memberList.size - index),
                style = PercentageText,
                color = HelpColor
            )
        }
    }
}