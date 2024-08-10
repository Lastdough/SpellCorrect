package com.abdurraahm.spellcorrect.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.data.local.model.NavigationItem
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource
import com.abdurraahm.spellcorrect.R.drawable as Drawable

@Composable
fun DefaultBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationItems = listOf(
        NavigationItem(
            screen = Screen.Home,
            iconImageVector = Drawable.home_icon_18px.imageVectorResource()
        ),

        NavigationItem(
            screen = Screen.Review,
            iconImageVector = Drawable.review_icon_18px.imageVectorResource()
        ),

        NavigationItem(
            screen = Screen.More,
            iconImageVector = Drawable.logo.imageVectorResource()
        ),
    )
    NavigationBar(modifier = modifier) {
        navigationItems.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        modifier = Modifier.size(19.dp),
                        imageVector = item.iconImageVector,
                        contentDescription = item.title
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    SpellCorrectTheme {
        DefaultBottomBar(navController = rememberNavController())
    }
}