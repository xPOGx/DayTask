package com.example.daytask.ui.screens.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.AvatarImage
import com.example.daytask.ui.theme.MainColor

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    userPhoto: Uri?,
    saveImage: (Bitmap) -> Unit,
    updateStatus: () -> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let {
            updateStatus()
            @Suppress("DEPRECATION")
            bitmap = if (Build.VERSION.SDK_INT < 28)
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            else
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
            saveImage(bitmap!!)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(
                top = dimensionResource(R.dimen.medium),
                bottom = dimensionResource(R.dimen.big)
            )
    ) {
        AvatarImage(
            avatarSizeRes = R.dimen.image_big,
            onImageClick = {
                launchImage.launch("image/*")
            },
            userPhoto = userPhoto
        )
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.large_ellipse))
                .border(
                    width = dimensionResource(R.dimen.mini),
                    color = MainColor,
                    shape = CircleShape
                )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(
                    bottom = 7.dp,
                    end = 7.dp
                )
                .size(dimensionResource(R.dimen.extra_big))
                .align(Alignment.BottomEnd)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_square_profile),
                contentDescription = null
            )
        }
    }
}
