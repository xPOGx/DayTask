package com.example.daytask.ui.screens.tools

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.Constants.noDismissProperties

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { /*ignore*/ },
        properties = noDismissProperties,
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(dimensionResource(R.dimen.large))
        )
    }
}