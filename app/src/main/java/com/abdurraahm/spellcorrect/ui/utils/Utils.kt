package com.abdurraahm.spellcorrect.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

@Composable
fun @receiver:DrawableRes Int.imageVectorResource(): ImageVector {
    return ImageVector.vectorResource(id = this)
}

@Composable
fun @receiver:DrawableRes Int.imagePainterResource(): Painter {
    return painterResource(id = this)
}


