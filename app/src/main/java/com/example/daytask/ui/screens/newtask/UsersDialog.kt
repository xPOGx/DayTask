package com.example.daytask.ui.screens.newtask

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.data.User
import com.example.daytask.ui.screens.tools.AvatarImage
import com.example.daytask.ui.screens.tools.LoadingScreen

@Composable
fun UsersDialog(
    onDismissRequest: () -> Unit,
    memberList: List<User>,
    userList: List<User>,
    updateMemberList: (List<User>) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        if (userList.isEmpty()) LoadingScreen()
        else {
            LazyColumn {
                items(
                    items = userList,
                    key = { it.userId }
                ) {
                    val checked = memberList.contains(it)
                    val iconRes = if (checked)
                        R.drawable.ic_tick_circle else R.drawable.ic_notick_circle

                    Card(
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.small))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = {
                                    val newMemberList = if (checked)
                                        memberList.minus(it) else memberList.plus(it)
                                    updateMemberList(newMemberList)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(iconRes),
                                    contentDescription = null
                                )
                            }
                            AvatarImage(
                                userPhoto = it.photoUrl,
                                avatarSizeRes = R.dimen.image_small
                            )
                            Text(
                                text = it.displayName.toString(),
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            )
                        }
                    }
                }
            }
        }
    }
}