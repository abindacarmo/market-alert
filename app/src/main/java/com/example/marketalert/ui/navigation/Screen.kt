package com.example.marketalert.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object MarketList : Screen("market_list")
    object MarketForm : Screen("market_form")
    object AddShopping : Screen("add_shopping")

    object DetailList : Screen("detail_list/{storeName}") {
        fun createRoute(storeName: String): String {
            return "detail_list/$storeName"
        }
    }
}