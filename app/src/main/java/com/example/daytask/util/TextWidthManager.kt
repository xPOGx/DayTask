package com.example.daytask.util

import android.content.Context
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.Density

object TextWidthManager {
    fun calculateTextWidth(
        text: String,
        style: TextStyle,
        context: Context,
        density: Density
    ): Float {
        val calculate = {
            ParagraphIntrinsics(
                text = text,
                style = style,
                density = density,
                fontFamilyResolver = createFontFamilyResolver(context)
            )
        }
        val intrinsics = calculate()
        return intrinsics.maxIntrinsicWidth / density.density
    }
}