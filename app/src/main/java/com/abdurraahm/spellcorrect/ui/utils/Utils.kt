package com.abdurraahm.spellcorrect.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

fun String.capitalizeFirstLetter(): String {
    return if (this.isNotEmpty()) {
        this.substring(0, 1).uppercase() + this.substring(1)
    } else {
        this
    }
}

fun Float.toPercent(): String {
    return "%.0f".format(this * 100)
}

fun Int.toRomanNumeral(): String {
    val map = listOf(
        1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
        100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
        10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
    )

    var num = this
    val romanNumeral = StringBuilder()

    for ((value, symbol) in map) {
        while (num >= value) {
            romanNumeral.append(symbol)
            num -= value
        }
    }

    return romanNumeral.toString()
}

@Composable
fun @receiver:DrawableRes Int.imageVectorResource(): ImageVector {
    return ImageVector.vectorResource(id = this)
}

@Composable
fun @receiver:DrawableRes Int.imagePainterResource(): Painter {
    return painterResource(id = this)
}


