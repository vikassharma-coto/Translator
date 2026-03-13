package com.usbrous.trans.navigation

sealed class Screen(val route: String) {
    data object Translate : Screen("translate")
    data object About : Screen("about")
}
