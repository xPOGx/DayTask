package com.example.daytask.ui.screens.profile

import android.graphics.Bitmap
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    signOut: () -> Unit,
    saveImage: (Bitmap) -> Unit,
    changeButton: (ChangeKey) -> Unit,
    disabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.big)),
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.big))
    ) {
        ProfileAvatar(
            userPhoto = Firebase.auth.currentUser!!.photoUrl.toString(),
            saveImage = saveImage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
//        Display Name
        DisabledField(
            value = Firebase.auth.currentUser!!.displayName,
            placeholderText = stringResource(R.string.user_name),
            leadingIconRes = R.drawable.ic_useradd_profile,
            changerField = { changeButton(ChangeKey.NAME) }
        )
//        Email
        DisabledField(
            value = Firebase.auth.currentUser!!.email,
            placeholderText = stringResource(R.string.user_email),
            leadingIconRes = R.drawable.ic_usertag_profile,
            changerField = { changeButton(ChangeKey.EMAIL) },
            disabled = disabled
        )
//        Password
        DisabledField(
            value = "",
            placeholderText = stringResource(R.string.password),
            leadingIconRes = R.drawable.ic_lock_profile,
            changerField = { changeButton(ChangeKey.PASSWORD) },
            disabled = disabled
        )
//        My Tasks
        DisabledField(
            value = stringResource(R.string.my_tasks),
            leadingIconRes = R.drawable.ic_task,
            trailingIconRes = R.drawable.ic_arrow_down_2,
            changerField = { changeButton(ChangeKey.TASKS) }
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