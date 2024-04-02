package com.myth.authy.core.utils

sealed class Screen(val route: String) {
    object SignUp : Screen("sign_up")
    object Home : Screen("home")
    object Detail : Screen("detail")

}