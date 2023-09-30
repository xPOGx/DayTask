package com.example.daytask.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.MultiColorText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.SplashLogoText
import com.example.daytask.ui.theme.White

@Composable
fun LogoColumn(
    modifier: Modifier = Modifier,
    multi: Float = 1f,
    style: TextStyle = SplashLogoText
) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoImage(
                multi = multi
            )
            MultiColorText(
                arrayOf(
                    Pair(stringResource(R.string.logo_day), White),
                    Pair(stringResource(R.string.logo_task), MainColor)
                ),
                style = style
            )
        }
    }
}

@Composable
fun LogoImage(
    modifier: Modifier = Modifier,
    multi: Float
) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .width((61 * multi).dp)
            .height((48 * multi).dp)
    )
}
