package com.example.daytask.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.daytask.R

// Set of Material typography styles to start with
val Typography = Typography(

)

val Pilat_Extended = FontFamily(
    Font(R.font.pilat_extended_regular), // W4
    Font(R.font.pilat_extended_semi_bold) // W6
)

val Inter = FontFamily(
    Font(R.font.inter_regular), // W4
    Font(R.font.inter_medium), // W5
    Font(R.font.inter_semi_bold) // W6
)

//PILAT_EXTENDED
val SplashLogoText = TextStyle(
    fontSize = 16.26.sp,
    lineHeight = 15.3.sp,
    fontFamily = Pilat_Extended,
    fontWeight = FontWeight.W600
)

val SplashLogoBigText = TextStyle(
    fontSize = 24.3.sp,
    lineHeight = 22.87.sp,
    fontFamily = Pilat_Extended,
    fontWeight = FontWeight.W600
)

val SplashText = TextStyle(
    fontSize = 51.sp,
    lineHeight = 50.sp,
    fontFamily = Pilat_Extended,
    fontWeight = FontWeight.W600
)

val UserNameText = TextStyle(
    fontSize = 22.29.sp,
    lineHeight = 27.5.sp,
    fontFamily = Pilat_Extended,
    fontWeight = FontWeight.W600
)

//INTER
val InputText = TextStyle(
    fontSize = 18.sp,
    lineHeight = 18.86.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W400
)

val PrivacyText = TextStyle(
    fontSize = 14.sp,
    lineHeight = 18.86.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W400,
)

val HelpText = TextStyle(
    fontSize = 16.sp,
    lineHeight = 18.86.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W500
)

val GoogleText = TextStyle(
    fontSize = 18.sp,
    lineHeight = 24.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W500
)

val WelcomeText = TextStyle(
    fontSize = 11.79.sp,
    lineHeight = 18.86.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W500,
)

val NavText = TextStyle(
    fontSize = 20.sp,
    lineHeight = 27.5.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W500
)

val ButtonText = TextStyle(
    fontSize = 18.sp,
    lineHeight = 38.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W600
)

val HeadlineText = TextStyle(
    fontSize = 26.sp,
    lineHeight = 15.3.sp,
    fontFamily = Inter,
    fontWeight = FontWeight.W600
)
