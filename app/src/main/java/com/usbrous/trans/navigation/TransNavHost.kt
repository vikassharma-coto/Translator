package com.usbrous.trans.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.usbrous.trans.feature_translate.presentation.TranslateScreen
import com.usbrous.trans.feature_translate.presentation.TranslateViewModel
import com.usbrous.trans.ui.about.AboutScreen

private const val NAV_ANIM_DURATION = 300

@Composable
fun TransNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Translate.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Translate.route,
            enterTransition = {
                fadeIn(animationSpec = tween(NAV_ANIM_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(NAV_ANIM_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(NAV_ANIM_DURATION)
                ) + fadeIn(animationSpec = tween(NAV_ANIM_DURATION))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(NAV_ANIM_DURATION))
            }
        ) {
            val viewModel: TranslateViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            TranslateScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                }
            )
        }

        composable(
            route = Screen.About.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(NAV_ANIM_DURATION)
                ) + fadeIn(animationSpec = tween(NAV_ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(NAV_ANIM_DURATION)
                ) + fadeOut(animationSpec = tween(NAV_ANIM_DURATION))
            }
        ) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
