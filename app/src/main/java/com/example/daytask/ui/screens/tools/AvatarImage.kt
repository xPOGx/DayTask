package com.example.daytask.ui.screens.tools

import androidx.annotation.DimenRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daytask.R
import com.example.daytask.util.ImageLoaderManager.getImageLoader

@Composable
fun AvatarImage(
    modifier: Modifier = Modifier,
    onImageClick: () -> Unit = { /*ignore*/ },
    userPhoto: String?,
    @DimenRes avatarSizeRes: Int
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(userPhoto)
        .diskCacheKey(userPhoto.toString())
        .memoryCacheKey(userPhoto.toString())
        .crossfade(true)
        .placeholder(R.drawable.ic_user)
        .error(R.drawable.ic_user)
        .build()
    val imageLoader = getImageLoader(context)
    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        imageLoader = imageLoader,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(dimensionResource(avatarSizeRes))
            .clip(CircleShape)
            .clickable(
                onClick = onImageClick,
                interactionSource = MutableInteractionSource(),
                indication = null
            )
    )
}