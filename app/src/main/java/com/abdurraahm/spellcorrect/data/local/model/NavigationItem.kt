package com.abdurraahm.spellcorrect.data.local.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.abdurraahm.spellcorrect.ui.navigation.Screen

data class NavigationItem(
    val screen: Screen,
    val iconImageVector: ImageVector
) {
    val title
        get() = screen.route
}