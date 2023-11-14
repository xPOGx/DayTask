package com.example.daytask.ui.screens.newtask

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.AvatarImage
import com.example.daytask.ui.theme.Secondary
import com.example.daytask.ui.theme.TeamCardText
import com.example.daytask.ui.theme.White

@Composable
fun TeamGridCard(
    modifier: Modifier = Modifier,
    removeMember: () -> Unit,
    userName: String?,
    userPhoto: String?
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Secondary),
        shape = RectangleShape,
        modifier = modifier.height(dimensionResource(R.dimen.image_small))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarImage(
                userPhoto = userPhoto,
                avatarSizeRes = R.dimen.image_tiny,
                modifier = Modifier.padding(dimensionResource(R.dimen.small))
            )
            Text(
                text = userName ?: "",
                style = TeamCardText.copy(White),
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            )
            IconButton(onClick = removeMember) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_square),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}