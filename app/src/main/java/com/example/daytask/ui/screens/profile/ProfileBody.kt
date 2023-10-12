package com.example.daytask.ui.screens.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.MainButton

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    signOut: () -> Unit,
    userPhoto: Uri?,
    userName: String?,
    userEmail: String?,
    saveImage: (Bitmap) -> Unit,
    updateStatus: () -> Unit,
    changeName: () -> Unit,
    changeEmail: () -> Unit,
    changePassword: () -> Unit,
    disabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.big)),
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.big))
    ) {
        ProfileAvatar(
            userPhoto = userPhoto,
            saveImage = saveImage,
            updateStatus = updateStatus,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
//        Display Name
        DisabledField(
            value = userName ?: "",
            placeholderText = stringResource(R.string.user_name),
            leadingIconRes = R.drawable.ic_useradd_profile,
            changerField = changeName
        )
//        Email
        DisabledField(
            value = userEmail ?: "",
            placeholderText = stringResource(R.string.user_email),
            leadingIconRes = R.drawable.ic_usertag_profile,
            changerField = changeEmail,
            disabled = disabled
        )
//        Password
        DisabledField(
            value = "",
            placeholderText = stringResource(R.string.password),
            leadingIconRes = R.drawable.ic_lock_profile,
            changerField = changePassword,
            disabled = disabled
        )
//        My Tasks
        DisabledField(
            value = stringResource(R.string.my_tasks),
            leadingIconRes = R.drawable.ic_task,
            trailingIconRes = R.drawable.ic_arrow_down_2,
            changerField = { /*TODO: My tasks list?*/ }
        )
//        Privacy
        DisabledField(
            value = stringResource(R.string.privacy),
            leadingIconRes = R.drawable.ic_security_card,
            trailingIconRes = R.drawable.ic_arrow_down_2,
            changerField = { /*TODO: Privacy list?*/ }
        )
//        Setting
        DisabledField(
            value = stringResource(R.string.setting),
            leadingIconRes = R.drawable.ic_setting_2,
            trailingIconRes = R.drawable.ic_arrow_down_2,
            changerField = { /*TODO: Settings?*/ }
        )
        MainButton(
            onClick = signOut,
            text = stringResource(R.string.logout),
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.big))
                .fillMaxWidth()
        )
    }
}