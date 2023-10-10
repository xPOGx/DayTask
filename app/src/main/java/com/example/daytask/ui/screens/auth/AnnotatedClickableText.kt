package com.example.daytask.ui.screens.auth

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.daytask.R
import com.example.daytask.ui.theme.HelpColor
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.PrivacyText

@Composable
fun AnnotatedClickableText(
    readPrivacy: () -> Unit,
    readTerms: () -> Unit
) {
    val agreedText = stringResource(R.string.agreed)
    val privacy = stringResource(R.string.privacy_policy)
    val coma = ", "
    val terms = stringResource(R.string.terms_condition)
    val style = SpanStyle(
        color = MainColor,
        textDecoration = TextDecoration.Underline
    )

    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = HelpColor,
            )
        ) {
            append(agreedText)
        }

        pushStringAnnotation(
            tag = privacy,
            annotation = privacy
        )
        withStyle(style) { append(privacy) }
        pop()

        withStyle(style.copy(textDecoration = null)) { append(coma) }

        pushStringAnnotation(
            tag = terms,
            annotation = terms
        )
        withStyle(style) { append(terms) }
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = privacy,
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                readPrivacy()
            }

            annotatedText.getStringAnnotations(
                tag = terms,
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                readTerms()
            }
        },
        style = PrivacyText
    )
}